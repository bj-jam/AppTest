package com.app.test.redenveloped

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.animation.LinearInterpolator
import com.app.test.R

class RedEnvelopesHelper(private val context: Context) {

    interface RedPacketLister {
        fun clickRedPacket(bean: RedEnvelopesHelper?)
        fun returnNormalBitmap(): Bitmap?
        fun returnClickBitmap(): Bitmap?
        fun returnGoldBitmap(): Bitmap?
    }

    var redPacketLister: RedPacketLister? = null

    var index = -1
    var money = 0

    //x,y代表当前红包图片绘制位置
    var moveX = 0
    var moveY = 0

    //初始化时的Y坐标
    private var initY = 0

    //点击时的x,y坐标，防止动画执行刷新x，y坐标值，在做逻辑判断时提前记录
    private var currentX = 0
    private var currentY = 0
    var isCanClick = true
        private set
    var normalWidth = 0
    var normalHeight = 0
    var clickBitmapWidth = 0
    var clickBitmapHeight = 0
    private var durationTime: Long = 3000
    private val showDismissTime: Long = 500

    //金币移动动画执行时间
    private val moneyMoveTime = 800
    private var valueAnimator: ValueAnimator? = null
    var isEnd = false
    private var matrix: Matrix? = null

    //3：红包下落,2:显示被点击红包，1显示金币，0：消失
    var redPacketStatus = 3

//    @IntDef(status_3, status_2, status_1, status_0)
//    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
//    annotation class status

    //金币消失x方向偏移量
    var goldDismissXOffset = 0

    //金币消失y方向偏移量
    var goldDismissYOffset = 0

    fun setInitY(initY: Int) {
        this.initY = initY
        moveY = initY
    }

    fun saveCurrentXY() {
        currentX = moveX
        currentY = moveY
    }

    val bitmap: Bitmap?
        get() = if (redPacketLister == null) null else redPacketLister?.returnNormalBitmap()

    val clickBitmap: Bitmap?
        get() = if (redPacketLister == null) null else redPacketLister?.returnClickBitmap()

    val goldBitmap: Bitmap?
        get() = if (redPacketLister == null) null else redPacketLister?.returnGoldBitmap()

    fun getMatrix(): Matrix {
        return (if (matrix == null) Matrix() else matrix!!)
    }

    fun constant(x: Int, y: Int): Boolean {
        return x >= currentX && x <= currentX + normalWidth && y >= currentY && y <= normalHeight + currentY
    }

    fun setDurationTime(time: Long) {
        var t = time
        if (t <= 0) {
            t = 3000
        }
        durationTime = t
    }

    fun startDown(containerHeight: Int) {
        val valueAnimator = ValueAnimator.ofFloat(initY.toFloat(), containerHeight.toFloat())
        valueAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
            if (redPacketStatus != status_3) {
                return@AnimatorUpdateListener
            }
            val animatedValue = animation.animatedValue as Float
            moveY = animatedValue.toInt()
        })
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                redPacketStatus = status_3
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (isCanClick) {
                    isEnd = true
                }
            }
        })
        valueAnimator.duration = durationTime
        valueAnimator.repeatCount = 0
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.start()
    }

    private fun startChangeToRedPacket() {
        //防止重复点击
        isCanClick = false
        if (valueAnimator?.isRunning == true) {
            valueAnimator?.cancel()
        }
        val translateX: Int
        val scaleX: Int = clickBitmapWidth / 2
        val scaleY: Int = clickBitmapHeight / 2
        translateX = scaleX - normalWidth / 2
        valueAnimator = ValueAnimator.ofFloat(0.8f, 1.2f)
        valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            if (redPacketStatus != status_2) {
                return@AnimatorUpdateListener
            }
            if (matrix == null) {
                matrix = Matrix()
            } else {
                matrix?.reset()
            }
            val animatedValue = animation.animatedValue as Float
            matrix?.setTranslate(moveX - translateX.toFloat(), moveY - 20.toFloat())
            matrix?.preScale(animatedValue, animatedValue, scaleX.toFloat(), scaleY.toFloat())
        })
        valueAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                redPacketStatus = status_2
            }

            override fun onAnimationEnd(animation: Animator) {
                redPacketStatus = status_1
                startChangeToGold()
            }
        })
        valueAnimator?.duration = showDismissTime
        valueAnimator?.repeatCount = 0
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.start()
    }

    private fun startChangeToGold() {
        if (valueAnimator?.isRunning == true) {
            valueAnimator?.cancel()
        }
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_redpacket_gold_active)
        val scaleX: Int
        val scaleY: Int
        if (bitmap == null) {
            scaleX = 150 / 2
            scaleY = 150 / 2
        } else {
            scaleX = bitmap.width / 2
            scaleY = bitmap.height / 2
        }
        //        setBitmap(bitmap);
        val holder1 = PropertyValuesHolder.ofFloat("x", moveX.toFloat(), goldDismissXOffset - scaleX / 2.toFloat())
        val holder2 = PropertyValuesHolder.ofFloat("y", moveY.toFloat(), goldDismissYOffset.toFloat())
        val holder3 = PropertyValuesHolder.ofFloat("scale", 0.8f, 0.5f)
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(holder1, holder2, holder3)
        valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            if (redPacketStatus != status_1) {
                return@AnimatorUpdateListener
            }
            val xPosition = animation.getAnimatedValue("x") as Float
            val yPosition = animation.getAnimatedValue("y") as Float
            val scale = animation.getAnimatedValue("scale") as Float
            if (matrix == null) {
                matrix = Matrix()
            } else {
                matrix?.reset()
            }
            matrix?.setTranslate(moveX + scaleX / 3.toFloat(), moveY.toFloat())
            matrix?.preScale(scale, scale, scaleX.toFloat(), scaleY.toFloat())
            moveX = xPosition.toInt()
            moveY = yPosition.toInt()
        })
        valueAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                redPacketStatus = status_1
            }

            override fun onAnimationEnd(animation: Animator) {
                redPacketStatus = status_0
                isEnd = true
            }
        })
        valueAnimator?.duration = moneyMoveTime.toLong()
        valueAnimator?.repeatCount = 0
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.start()
    }

    fun clickRedPacket() {
        redPacketLister?.clickRedPacket(this)
        startChangeToRedPacket()
    }

    companion object {
        const val status_3 = 3
        const val status_2 = 2
        const val status_1 = 1
        const val status_0 = 0
        fun produceRedPacket(index: Int, context: Context?, normalBitmapWidth: Int, normalBitmapHeight: Int, clickBitmapWidth: Int, clickBitmapHeight: Int, redPacketX: Int, durationTimeMillis: Long): RedEnvelopesHelper? {
            if (context == null) {
                return null
            }
            val redPacket = RedEnvelopesHelper(context)
            redPacket.setDurationTime(durationTimeMillis)
            redPacket.index = index
            redPacket.normalWidth = normalBitmapWidth
            redPacket.normalHeight = normalBitmapHeight
            redPacket.clickBitmapWidth = clickBitmapWidth
            redPacket.clickBitmapHeight = clickBitmapHeight
            redPacket.moveX = redPacketX
            redPacket.setInitY(-redPacket.normalHeight)
            return redPacket
        }
    }

}