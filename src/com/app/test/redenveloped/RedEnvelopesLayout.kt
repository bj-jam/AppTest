package com.app.test.redenveloped

import android.animation.*
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.app.test.R
import com.app.test.redenveloped.RedEnvelopesHelper.RedPacketLister
import com.app.test.redenveloped.TimeCountDown.TimeCallback
import com.app.test.redenveloped.TimerUtil.TimerCallback
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.DensityUtil.getScreenHeight
import com.app.test.util.DensityUtil.getScreenWidth
import com.app.test.util.Utils

class RedEnvelopesLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var timeCountDown: TimeCountDown? = null
    private var timerUtil: TimerUtil? = null
    private lateinit var bitmap1: Bitmap
    private lateinit var bitmap3: Bitmap
    private lateinit var bitmap2: Bitmap
    private lateinit var bitmap4: Bitmap
    private lateinit var clickBitmap: Bitmap
    private lateinit var goldBitmap: Bitmap
    private lateinit var bitmaps: Array<Bitmap?>
    private var redEnvelopesView: RedEnvelopesView
    private var redEnvelopesList: List<RedEnvelopes>? = null

    //单位：秒
    private var redEnvelopesTime = 0
    private var tvMoney: TextView
    private var tvTime: TextView
    private var ivTime: ImageView
    private var ivReady: ImageView
    private var flRedEnvelopesTop: FrameLayout
    private val redEnvelopesEnd = 2000

    @get:Synchronized
    var money = 0
        private set
    private var valueAnim: ValueAnimator? = null

    //用于动画参数
    private var showMoney = 0

    private var mHandler: Handler? = null


    init {
        View.inflate(context, R.layout.layout_redpacket, this)
        redEnvelopesView = findViewById(R.id.rpvRedPacket)
        tvMoney = findViewById(R.id.tvMoney)
        tvTime = findViewById(R.id.tvTime)
        ivTime = findViewById(R.id.ivTime)
        ivReady = findViewById(R.id.ivReady)
        flRedEnvelopesTop = findViewById(R.id.flRedPacketTop)
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == RED_PACKET_END) {
                    if (msg.arg1 != redEnvelopesEnd) {
                        checkIsEnd()
                    }
                }
            }
        }
    }

    fun setData(list: List<RedEnvelopes>?, time: Int) {
        redEnvelopesList = list
        redEnvelopesTime = time
        tvTime.text = redEnvelopesTime.toString() + "s"
        ivReady.visibility = View.VISIBLE
    }

    fun onDestroy() {
        timeCountDown?.onDestroy()
        timeCountDown = null
        valueAnim?.cancel()
        valueAnim = null
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        timerUtil?.onDestroy()
        timerUtil = null

    }

    @Synchronized
    private fun addMoney(moneyNum: Int?) {
        money += moneyNum ?: 0
    }

    fun readyGo() {
        //因为activity过度动画设置了时间，然后延迟执行也能让动画一开始没有卡顿效果
        mHandler?.postDelayed({ timeDownStart(3) }, 1000)
    }

    private fun timeDownStart(time: Int) {
        var t = time
        ivTime.visibility = View.VISIBLE
        if (t >= 3 || t < 1) {
            ivTime.setImageResource(R.drawable.ic_three)
            t = 3
        }
        ivTime.scaleX = 0.5f
        ivTime.scaleX = 0.5f
        val scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.6f)
        val scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.6f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(ivTime, scaleXHolder, scaleYHolder)
        val finalTime = t
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                if (!Utils.isEmpty(ivTime)) {
                    ivTime.visibility = View.VISIBLE
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (Utils.isEmpty(ivTime)) {
                    return
                }
                ivTime.visibility = View.INVISIBLE
                when (finalTime - 1) {
                    2 -> ivTime.setImageResource(R.drawable.ic_two)
                    1 -> ivTime.setImageResource(R.drawable.ic_one)
                    else -> ivTime.setImageResource(R.drawable.ic_three)
                }
                if (finalTime == 1) {
                    ivReady.visibility = View.INVISIBLE
                    //倒计时完成，准备掉红包
                    startShowRedPacket()
                    return
                }
                timeDownStart(finalTime - 1)
            }
        })
        animator.duration = 1000
        animator.start()
    }

    private fun checkIsEnd() {
        if (isExit || Utils.isEmpty(mHandler)) {
            return
        }
        if (redEnvelopesView.isEnd) {
            val message = Message.obtain()
            message.what = RED_PACKET_END
            message.arg1 = redEnvelopesEnd
            mHandler?.sendMessage(message)
        } else {
            mHandler?.sendEmptyMessageDelayed(RED_PACKET_END, 300)
        }
    }

    private fun startShowRedPacket() {
        //重新玩的时候清除之前的子view检测以及是否结束
        mHandler?.removeMessages(RED_PACKET_END)
        tvTime.text = redEnvelopesTime.toString() + "s"
        timeCountDown = TimeCountDown()
        timeCountDown?.start(redEnvelopesTime, object : TimeCallback {
            override fun onNext(timeSecond: Int) {
                tvTime.text = timeSecond.toString() + "s"
            }

            override fun onComplete() {
                tvTime.text = "0s"
                mHandler?.sendEmptyMessageDelayed(RED_PACKET_END, 300)
            }
        })
        if (Utils.isEmpty(mHandler)) {
            return
        }
        if (Utils.isEmpty(redEnvelopesList)) {
            return
        }

        //产生红包的间隔时间
        val interval = redEnvelopesTime * 1000L / redEnvelopesList!!.size
        val tempIntervalTimeMillis = (maxMillis - minMillis) / redEnvelopesList!!.size.toLong()
        checkBitmap()
        //因为4个红包宽高一致，这里获取一个图片的宽度就行了
        val redEnvelopesWidth = getBitmapWidth(bitmap1, false)
        val clickRedPacketWidth = getBitmapWidth(clickBitmap, true)
        val clickRedPacketHeight = getClickBitmapHeight(clickBitmap)
        val dp10 = dp2px(10)
        val screenWidth = getScreenWidth(context)
        val screenHeight = getScreenHeight(context)
        val containerHeight = screenHeight + redEnvelopesWidth
        val goldDismissXOffset = 17
        val goldDismissYOffset = 17
        timerUtil = TimerUtil()
        timerUtil?.startTime(interval, object : TimerCallback {
            override fun onCheck(checkCount: Int): Boolean {
                if (Utils.isEmpty(context)) {
                    return true
                }
                if (isExit) {
                    return true
                }
                if (Utils.isEmpty(redEnvelopesList)) {
                    return true
                }
                var index = checkCount - 1
                if (index < 0) {
                    index = 0
                } else if (index >= redEnvelopesList!!.size) {
                    index = redEnvelopesList!!.size - 1
                }
                val bean = redEnvelopesList!![index]
                val randomRedPacket = RedEnvelopesLocation.getRandomRedPacket(bitmaps)
                val durationTimeMillis = maxMillis - tempIntervalTimeMillis * index
                //下一个红包的x方向位置
                val nextRedPacketX = RedEnvelopesLocation.getNextRedPacketX(dp10, screenWidth, redEnvelopesWidth)
                val redPacketHelper = RedEnvelopesHelper.produceRedPacket(index, context, redEnvelopesWidth,
                        redEnvelopesWidth, clickRedPacketWidth, clickRedPacketHeight, nextRedPacketX, durationTimeMillis)
                if (!Utils.isEmpty(redPacketHelper)) {
                    redPacketHelper?.goldDismissXOffset = goldDismissXOffset
                    redPacketHelper?.goldDismissYOffset = goldDismissYOffset
                    redPacketHelper?.money = bean.money
                    redPacketHelper?.redPacketLister = object : RedPacketLister {
                        override fun clickRedPacket(bean: RedEnvelopesHelper?) {
                            addMoney(bean?.money)
                            changeMoneyTextAnim()
                        }


                        override fun returnNormalBitmap(): Bitmap? {
                            return randomRedPacket
                        }

                        override fun returnClickBitmap(): Bitmap? {
                            return clickBitmap
                        }

                        override fun returnGoldBitmap(): Bitmap? {
                            return goldBitmap
                        }
                    }
                    if (Utils.isEmpty(redEnvelopesView)) {
                        return true
                    }
                    if (redPacketHelper != null) {
                        redEnvelopesView.addRedPacketData(checkCount - 1, redPacketHelper)
                    }
                    redPacketHelper?.startDown(containerHeight)
                    if (checkCount == 1) {
                        redEnvelopesView.startDraw()
                    }
                }
                return checkCount >= redEnvelopesList?.size ?: 0 || isExit
            }

            override fun onEnd() {}
        })
    }

    private fun checkBitmap() {
        bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.ic_14)
        bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.ic_16)
        bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.ic_19)
        bitmap4 = BitmapFactory.decodeResource(resources, R.drawable.ic_20)
        bitmaps = arrayOf(bitmap1, bitmap2, bitmap3, bitmap4)
        clickBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_redpacket_click)
        goldBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_redpacket_gold_active)
    }

    private fun getBitmapWidth(bitmap: Bitmap?, isClickBitmap: Boolean): Int {
        return if (bitmap == null || bitmap.isRecycled) {
            if (isClickBitmap) 258 else 204
        } else bitmap.width
    }

    private fun getGoldBitmapWidth(bitmap: Bitmap?): Int {
        return if (bitmap == null || bitmap.isRecycled) {
            150
        } else bitmap.width
    }

    private fun getClickBitmapHeight(bitmap: Bitmap?): Int {
        return if (bitmap == null || bitmap.isRecycled) {
            331
        } else bitmap.height
    }

    //获取金币数量改变动画
    private fun changeMoneyTextAnim() {
        if (Utils.isEmpty(valueAnim)) {
            valueAnim = ValueAnimator.ofInt(0, money)
        }
        if (valueAnim?.isRunning == true) {
            valueAnim?.cancel()
        }
        valueAnim = ValueAnimator.ofInt(showMoney, money).also {
            it.addUpdateListener(AnimatorUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                showMoney = animatedValue
                if (!Utils.isEmpty(tvMoney)) {
                    tvMoney.text = showMoney.toString()
                }
            })
            it.duration = 900
            it.start()
        }

    }

    //红包下落最长时间
    val maxMillis: Int
        get() = 3200

    //红包下落最短时间
    private val minMillis
        get() = 1400

    fun showOtherView() {
        flRedEnvelopesTop.visibility = View.VISIBLE
    }

    val isExit: Boolean
        get() {
            var activity: Activity? = null
            if (!Utils.isEmpty(context) && context is Activity) {
                activity = context as Activity
            }
            return activity == null || activity.isDestroyed || activity.isFinishing
        }

    companion object {
        const val RED_PACKET_END = 111
    }
}