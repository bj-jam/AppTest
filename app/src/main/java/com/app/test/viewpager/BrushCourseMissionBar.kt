package com.app.test.viewpager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import kotlin.math.cos
import kotlin.math.sin

class BrushCourseMissionBar : View {
    private val infoString = "目标完成36%"
    private val timeTextString = "关"

    /**
     * 圆弧的范围框
     */
    private var mRectangle = RectF()

    /**
     * 时间的范围
     */
    private val timeRect = Rect()

    /**
     * 描述文字的范围
     */
    private val infoTextRect = Rect()

    /**
     * 时间文字的范围
     */
    private val timeTextRect = Rect()

    /**
     * 背景画笔
     */
    private lateinit var mDefaultPaint: Paint

    /**
     * 圆弧的画笔
     */
    private lateinit var mColorPaint: Paint

    /**
     * 分钟时间的画笔
     */
    private lateinit var timePaint: Paint

    /**
     * "本周累计学习"的画笔
     */
    private lateinit var infoPaint: Paint

    /**
     * 分钟文字的画笔
     */
    private lateinit var timeTextPaint: Paint

    /**
     * 分钟文字的画笔
     */
    private lateinit var circlePaint: Paint

    /**
     * 分钟文字的画笔
     */
    private lateinit var backPaint: Paint

    /**
     * 圆弧画笔的宽度
     */
    private var circleWidth = 0f

    /**
     * 分钟字体的大小
     */
    private var infoWidth = 0f
    private var textWidth = 0f
    private lateinit var mText: String
    private var mCount = 0

    /**
     * 当前弧度
     */
    private var mCurrentAngle = 0f

    /**
     * 画的最大角度
     */
    private var mPaintAngle = 0f

    /**
     * 最大弧度
     */
    private var mMaxAngle = 0f

    /**
     * 分钟的大小
     */
    private var mTimeSize = 0

    /**
     * 动画
     */
    private lateinit var anim: BarAnimation

    /**
     * VIEW的宽度
     */
    private var mWidth = 0f

    /**
     * VIEW的高度
     */
    private var mHeight = 0f

    /**
     * 中心X轴的位置
     */
    private var mCircleX = 0f

    /**
     * 中心Y轴的位置
     */
    private var mCircleY = 0f

    /**
     * 半径
     */
    private var mMaxRadius = 0f

    /**
     * 背景圆形左边画图起点
     */
    private var circleRadiusX = 0f

    /**
     * 背景圆形顶部画图起点
     */
    private var circleRadiusY = 0f

    /**
     * 指针的X轴的位置
     */
    private var thumbX = 0f

    /**
     * 指针的Y轴的位置
     */
    private var thumbY = 0f
    private var timeString = ""

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        circleWidth = dip2px(context, 5f).toFloat()
        textWidth = dip2px(context, 15f).toFloat()
        infoWidth = dip2px(context, 16f).toFloat()
        mTimeSize = dip2px(context, 54f)
        mColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mColorPaint.color = -0xf71b4d
        mColorPaint.style = Paint.Style.STROKE
        mColorPaint.strokeWidth = circleWidth


        mDefaultPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mDefaultPaint.color = -0x948e86
        mDefaultPaint.style = Paint.Style.STROKE
        mDefaultPaint.strokeWidth = circleWidth


        timePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        timePaint.color = -0x1
        timePaint.style = Paint.Style.FILL_AND_STROKE
        timePaint.textAlign = Align.LEFT
        timePaint.textSize = mTimeSize.toFloat()


        timeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        timeTextPaint.color = -0x33000001
        timeTextPaint.style = Paint.Style.FILL_AND_STROKE
        timeTextPaint.textAlign = Align.LEFT
        timeTextPaint.textSize = infoWidth


        infoPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        infoPaint.color = -0x33000001
        infoPaint.style = Paint.Style.FILL_AND_STROKE
        infoPaint.textAlign = Align.LEFT
        infoPaint.textSize = textWidth

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        circlePaint.color = -0xf71b4d
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        circlePaint.textAlign = Align.LEFT
        circlePaint.textSize = textWidth

        backPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        backPaint.color = -0xc0bcb5
        backPaint.style = Paint.Style.FILL_AND_STROKE
        backPaint.textAlign = Align.LEFT
        backPaint.textSize = circleWidth
        mText = "12"
        mMaxAngle = 270f
        mPaintAngle = 190f
        mCurrentAngle = 45f
        anim = BarAnimation()
        anim.duration = 2000
    }

    override fun onDraw(canvas: Canvas) {
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        mMaxRadius = mWidth.coerceAtMost(mHeight) / 2 - circleWidth * 2
        // 计算圆的起点
        // 左边背景圆起点
        circleRadiusX = (mWidth - mMaxRadius * 2) / 2
        // 顶部背景圆起点
        circleRadiusY = (mHeight - mMaxRadius * 2) / 2
        // 画图矩形区域——左边起点，底部起点，左边起点+直径，顶部起点+直径
        mCircleX = mWidth / 2
        mCircleY = mHeight / 2
        mRectangle = RectF(circleRadiusX, circleRadiusY, 2 * mMaxRadius + circleRadiusX, 2 * mMaxRadius + circleRadiusY)
        canvas.drawCircle(mCircleX, mCircleY, mMaxRadius, backPaint)
        canvas.drawArc(mRectangle, -225f, mMaxAngle, false, mDefaultPaint)
        canvas.drawArc(mRectangle, -225f, mCurrentAngle, false, mColorPaint)
        timeString = mCount.toString() + ""
        timePaint.getTextBounds(timeString, 0, timeString.length, timeRect)
        timeTextPaint.getTextBounds(timeTextString, 0, timeTextString.length, timeTextRect)
        infoPaint.getTextBounds(infoString, 0, infoString.length, infoTextRect)
        //画时间
        canvas.drawText(timeString, mCircleX - (timeRect.width() + timeTextRect.width()) / 2, mCircleY + timeRect.height() / 4, timePaint)
        //画"关"
        canvas.drawText(timeTextString, mCircleX + timeRect.width() / 2, mCircleY + timeRect.height() / 4, timeTextPaint)
        //画描述
        canvas.drawText(infoString, mCircleX - infoTextRect.width() / 2, mCircleY - textWidth * 3, infoPaint)
        drawThumbnail(canvas)
    }

    /**
     * 画指示器
     */
    private fun drawThumbnail(canvas: Canvas) {
        thumbX = (mCircleX + getRealCosX(mCurrentAngle, mMaxRadius)).toFloat()
        thumbY = (mCircleY + getRealSinY(mCurrentAngle, mMaxRadius)).toFloat()
        circlePaint.color = 0x2608E4B3
        canvas.drawCircle(thumbX, thumbY, 18f, circlePaint)
        circlePaint.color = -0x7ff71b4d
        canvas.drawCircle(thumbX, thumbY, 13f, circlePaint)
        circlePaint.color = -0xf71b4d
        canvas.drawCircle(thumbX, thumbY, 8f, circlePaint)
    }

    /**
     * 计算x轴的偏移位置
     *
     * @param angle
     * @param radius
     * @return
     */
    private fun getRealCosX(angle: Float, radius: Float): Double {
        return when {
            angle < 45 -> {
                -getCosX(45 - angle, radius)
            }
            angle < 135 -> {
                -getCosX(angle - 45, radius)
            }
            angle < 225 -> {
                getCosX(225 - angle, radius)
            }
            angle <= 270 -> {
                getCosX(angle - 225, radius)
            }
            else -> {
                -getCosX(45 - angle, radius)
            }
        }
    }

    /**
     * 计算Y轴的偏移位置
     *
     * @param angle
     * @param radius
     * @return
     */
    private fun getRealSinY(angle: Float, radius: Float): Double {
        return when {
            angle < 45 -> {
                getSinY(45 - angle, radius)
            }
            angle < 135 -> {
                -getSinY(angle - 45, radius)
            }
            angle < 225 -> {
                -getSinY(225 - angle, radius)
            }
            angle <= 270 -> {
                getSinY(angle - 225, radius)
            }
            else -> {
                getSinY(45 - angle, radius)
            }
        }
    }

    private fun getCosX(diggre: Float, radius: Float): Double { //diggre  0-90 第一象限角度计算
        return radius * cos(Math.toRadians(diggre.toDouble()))
    }

    private fun getSinY(diggre: Float, radius: Float): Double { //diggre  0-90
        return radius * sin(Math.toRadians(diggre.toDouble()))
    }

    fun startCustomAnimation() {
        startAnimation(anim)
    }

    fun setText(text: String) {
        mText = text
        startAnimation(anim)
    }

    fun setSweepAngle(sweepAngle: Float) {
        mMaxAngle = sweepAngle
    }

    inner class BarAnimation
    /**
     * Initializes expand collapse animation, has two types, collapse (1)
     * and expand (0).
     * The view to animate
     * The type of animation: 0 will expand from gone and 0 size
     * to visible and layout size defined in xml. 1 will collapse
     * view and set to gone
     */
        : Animation() {
        override fun applyTransformation(interpolatedTime: Float,
                                         t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            if (interpolatedTime < 1.0f) {
                mCurrentAngle = interpolatedTime * mPaintAngle
                mCount = (interpolatedTime * mText.toFloat()).toInt()
            } else {
                mCurrentAngle = mPaintAngle
                mCount = mText.toInt()
            }
            postInvalidate()
        }
    }

    companion object {
        fun dip2px(context: Context, dipValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }
}