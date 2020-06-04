package com.app.test.game.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import com.app.test.R
import com.app.test.util.DensityUtil.getScreenWidth
import com.app.test.util.Utils

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
class OpenDoorView : LinearLayout {
    private lateinit var mLeft: ImageView
    private lateinit var mRight: ImageView
    private var openDoorListen: OpenDoorListen? = null
    private val screenWidth = getScreenWidth(context)
    private val animTime = 500
    private var closeAnim: ValueAnimator? = null
    private var openAnim: ValueAnimator? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    fun initView() {
        View.inflate(context, R.layout.view_open_door_anim, this)
        mLeft = findViewById(R.id.iv_left)
        mRight = findViewById(R.id.iv_right)
        visibility = View.INVISIBLE
        isClickable = false
    }

    fun setOpenDoorListen(openDoorListen: OpenDoorListen?) {
        this.openDoorListen = openDoorListen
    }

    /**
     * 开始关门动画
     */
    fun startCloseAnim() {
        stopAnim()
        visibility = View.VISIBLE
        isClickable = true
        if (closeAnim == null) {
            closeAnim = ValueAnimator()
            closeAnim?.setIntValues(-screenWidth, 0)
            closeAnim?.duration = animTime.toLong()
            closeAnim?.interpolator = LinearInterpolator()
            closeAnim?.addUpdateListener { animation ->
                val pointX = animation.animatedValue as Int
                mLeft.x = pointX.toFloat()
                mRight.x = -pointX.toFloat()
                if (pointX == 0) {
                    startOpenAnim()
                }
            }
        }
        closeAnim?.start()
        openDoorListen?.closeAnimStart()
    }

    /**
     * 开始开门动画
     */
    private fun startOpenAnim() {
        if (Utils.isEmpty(openAnim)) {
            openAnim = ValueAnimator()
            openAnim?.setIntValues(0, screenWidth)
            openAnim?.duration = animTime.toLong()
            openAnim?.interpolator = LinearInterpolator()
            openAnim?.addUpdateListener { animation ->
                val pointX = animation.animatedValue as Int
                mLeft.x = -pointX.toFloat()
                mRight.x = pointX.toFloat()
                if (pointX == screenWidth) {
                    visibility = View.GONE
                    openDoorListen?.openAnimEnd()
                }
            }
        }
        openAnim?.start()
        openDoorListen?.closeAnimEnd()
        openDoorListen?.openAnimStart()
    }

    private fun stopAnim() {
        if (openAnim?.isRunning == true) {
            openAnim?.cancel()
        }
        if (closeAnim?.isRunning == true) {
            closeAnim?.cancel()
        }
    }

    abstract class OpenDoorListen {
        open fun openAnimStart() {}
        fun openAnimEnd() {}
        fun closeAnimStart() {}
        fun closeAnimEnd() {}
    }
}