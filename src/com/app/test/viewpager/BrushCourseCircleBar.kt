package com.app.test.viewpager

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Align
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px
import kotlin.math.cos
import kotlin.math.sin

class BrushCourseCircleBar : View {
    private val infoString = "本周累计学习"
    private val timeTextString = "分钟"

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
     * 指针的bitmap
     */
    private var mBitmap: Bitmap? = null

    /**
     * 指正转向的
     */
    private lateinit var mMatrix: Matrix

    /**
     * VIEW的宽度
     */
    private var mWidth = 0

    /**
     * VIEW的高度
     */
    private var mHeight = 0

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
    private var mMaxRadius = 0

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

    /**
     * 缩放
     */
    private lateinit var mScaleMatrix: Matrix
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
        circleWidth = dp2px(10).toFloat()
        textWidth = dp2px(14).toFloat()
        infoWidth = dp2px(16).toFloat()
        mTimeSize = dp2px(54)

        mColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mColorPaint.color = -0x1
        mColorPaint.style = Paint.Style.STROKE
        mColorPaint.strokeWidth = circleWidth
        mColorPaint.pathEffect = DashPathEffect(floatArrayOf(3f, 10f), 0f)


        mDefaultPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mDefaultPaint.color = -0x78746c
        mDefaultPaint.style = Paint.Style.STROKE
        mDefaultPaint.strokeWidth = circleWidth
        mDefaultPaint.pathEffect = DashPathEffect(floatArrayOf(3f, 10f), 0f)


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

        //进度指示器
        val b = BitmapFactory.decodeResource(resources, R.drawable.cg_indicator)
        mMatrix = Matrix()
        mScaleMatrix = Matrix()
        mScaleMatrix.postScale(0.7f, 0.7f)
        // 得到新的图片
        mBitmap = Bitmap.createBitmap(b, 0, 0, b.width, b.height, mScaleMatrix, true)
        if (!b.isRecycled) {
            b.recycle()
        }
        mText = "999"
        mMaxAngle = 270f
        mPaintAngle = 199f
        mCurrentAngle = 45f
        anim = BarAnimation()
        anim.duration = 2000
    }

    override fun onDraw(canvas: Canvas) {
        mWidth = measuredWidth
        mHeight = measuredHeight
        mMaxRadius = (Math.min(mWidth, mHeight) - (circleWidth * 3).toInt()) / 2
        // 计算圆的起点
        // 左边背景圆起点
        circleRadiusX = (mWidth - mMaxRadius * 2) / 2.toFloat()
        // 顶部背景圆起点
        circleRadiusY = (mHeight - mMaxRadius * 2) / 2.toFloat()
        // 画图矩形区域——左边起点，底部起点，左边起点+直径，顶部起点+直径
        mCircleX = circleRadiusX + mMaxRadius
        mCircleY = circleRadiusY + mMaxRadius
        mRectangle = RectF(circleRadiusX, circleRadiusY, 2 * mMaxRadius + circleRadiusX, 2 * mMaxRadius + circleRadiusY)
        canvas.drawArc(mRectangle, -225f, mMaxAngle, false, mDefaultPaint)
        canvas.drawArc(mRectangle, -225f, mCurrentAngle, false, mColorPaint)
        drawThumbnail(canvas)
        timeString = mCount.toString() + ""
        timePaint.getTextBounds(timeString, 0, timeString.length, timeRect)
        timeTextPaint.getTextBounds(timeTextString, 0, timeTextString.length, timeTextRect)
        infoPaint.getTextBounds(infoString, 0, infoString.length, infoTextRect)
        //画时间
        canvas.drawText(timeString, mCircleX - (timeRect.width() + timeTextRect.width()) / 2, mCircleY + timeRect.height() / 2, timePaint)
        //画分钟
        canvas.drawText(timeTextString, mCircleX + timeRect.width() / 3, mCircleY + timeRect.height() / 2, timeTextPaint)
        //画描述
        canvas.drawText(infoString, mCircleX - infoTextRect.width() / 2, mCircleY - (textWidth + infoWidth), infoPaint)
    }

    /**
     * 画指示器
     */
    private fun drawThumbnail(canvas: Canvas) {
        val p = mCurrentAngle - 135
        thumbX = mCircleX + getRealCosX(mCurrentAngle, mMaxRadius + dp2px(5).toFloat())
        thumbY = mCircleY + getRealSinY(mCurrentAngle, mMaxRadius + dp2px(5).toFloat())
        if (mBitmap != null) {
            //计算
            val bmpWidth = mBitmap?.width ?: 0
            val bmpHeight = mBitmap?.height ?: 0
            canvas.save()
            canvas.translate(thumbX - bmpWidth.toFloat() / 2.0f, thumbY - bmpHeight.toFloat() / 2.0f)
            mMatrix.setRotate(p, bmpWidth.toFloat() / 2.0f, bmpHeight.toFloat() / 2.0f)
            canvas.drawBitmap(mBitmap, mMatrix, null)
            canvas.restore()
        }
    }

    /**
     * 计算x轴的偏移位置
     *
     * @param angle
     * @param radius
     * @return
     */
    private fun getRealCosX(angle: Float, radius: Float): Float {
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
    private fun getRealSinY(angle: Float, radius: Float): Float {
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

    private fun getCosX(diggre: Float, radius: Float): Float { //diggre  0-90 第一象限角度计算
        return (radius * cos(Math.toRadians(diggre.toDouble()))).toFloat()
    }

    private fun getSinY(diggre: Float, radius: Float): Float { //diggre  0-90
        return (radius * sin(Math.toRadians(diggre.toDouble()))).toFloat()
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
}