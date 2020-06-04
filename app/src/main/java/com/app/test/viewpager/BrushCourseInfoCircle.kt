package com.app.test.viewpager

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by dell on 2017/10/12.
 * 圆圈进度条
 */
class BrushCourseInfoCircle : View {
    private var mPaint: Paint? = null
    private var mKeduLength = 0
    private var mProgressRingThick = 0
    private var mIndicatorBmp: Bitmap? = null
    private var mKedu2ProgressRingDistance = 0
    private var mEdge2KeduDistance = 0
    private var mPointDiscreteDistance = 0
    private var mWidth = 0
    private var mHeight = 0
    private var rate = 0f
    private var KeduCount = 0
    private var mPointRadio = 0
    private var mMaxRadious = 0f
    private var mOnSeekArcChangeListener: OnSeekChangeListener? = null
    private var thumbX = 0f
    private var thumbY = 0f
    private var mTouchIgnoreRadius = 0f
    private var ignoreContinueMoveTouch = false
    private var currentAngle = -1f
    private var mThumbnailMatrix: Matrix? = null
    private var isTracking: Boolean = false//用户是否在拖动 = false
    private var mTrackingPath: Path? = null
    private var mTrackingPaint: Paint? = null
    private var beginTrackingAngle = 0f
    private var mMirrorRadious = 0
    private var mMirrorRate = 0f
    private var mMirrorPath: Path? = null
    private var mMPoint2EdgeDistance = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.isDither = true

        //虚线
        mTrackingPaint = Paint(mPaint)
        mTrackingPaint!!.style = Paint.Style.STROKE
        mTrackingPaint!!.color = Color.YELLOW
        mTrackingPaint!!.strokeWidth = dp2px(1).toFloat()
        mTrackingPaint!!.pathEffect = DashPathEffect(floatArrayOf(dp2px(2).toFloat(), dp2px(2).toFloat()), 0f)
        mTrackingPath = Path()

        //放大镜
        mMirrorRadious = dp2px(20) //放大镜半斤
        mMirrorRate = 1.5f //放大镜放大的倍速
        mMirrorPath = Path()

        //整体缩放比率
        rate = 1f
        //指示器触摸偏差
        mTouchIgnoreRadius = dp2px(20).toFloat()
        //刻度数
        KeduCount = 36
        //刻度线长度
        mKeduLength = dp2px(5)
        //进度环厚度
        mProgressRingThick = dp2px(15)

        //视频点的半径
        mPointRadio = dp2px(5)
        mPointDiscreteDistance = 0
        //视频点圆形 到 最外灰圈的距离
        mMPoint2EdgeDistance = dp2px(7)
        //最外圈灰线到刻度内圆边距的距离
        mEdge2KeduDistance = dp2px(5) + mKeduLength
        //刻度线内圆边距到进度环外圆边距的距离
        mKedu2ProgressRingDistance = mProgressRingThick / 2 + dp2px(3)
        //进度指示器
        mIndicatorBmp = BitmapFactory.decodeResource(resources, R.drawable.cg_indicator)
        mThumbnailMatrix = Matrix()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mWidth = measuredWidth
        mHeight = measuredHeight
        mMaxRadious = Math.min(mWidth, mHeight) / 2 * rate - dp2px(20) //最大半径 (20:指示器超出的长度 )
        canvas.translate(mWidth / 2.toFloat(), mHeight / 2.toFloat()) //平移圆心
        drawBg(canvas)
        drawKeDu(canvas)
        drawPoint(canvas)

        //放大镜
        if (isTracking) {
            canvas.save()
            //放大镜圆形离大圆盘圆心的距离
            val mirrorDiscreteRadio = mMaxRadious - 2.0f * mPointRadio.toFloat() - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - mProgressRingThick.toFloat() / 2.0f
            mMirrorPath!!.reset()
            mMirrorPath!!.addCircle(getRealCosX(currentAngle, mirrorDiscreteRadio), getRealSinY(currentAngle, mirrorDiscreteRadio), mMirrorRadious.toFloat(), Path.Direction.CW)
            canvas.clipPath(mMirrorPath)
            drawBg(canvas) //画没放大的背景
            drawKeDu(canvas) //画没放大的刻度
            //画放大的进度
            val scaleOneRadious = mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - mProgressRingThick.toFloat() / 2.0f
            val X = getRealCosX(currentAngle, scaleOneRadious * (mMirrorRate - 1.0f))
            val Y = getRealSinY(currentAngle, scaleOneRadious * (mMirrorRate - 1.0f))
            canvas.translate(-X, -Y)
            canvas.scale(mMirrorRate, mMirrorRate)
            canvas.restore()
        }
        drawThumbnail(canvas)
    }

    /*画指示器*/
    private fun drawThumbnail(canvas: Canvas) {
        if (currentAngle >= 0) {
            //画瞄准的虚线
            if (isTracking) {
                canvas.save()
                canvas.rotate(currentAngle)
                mTrackingPath!!.reset()
                mTrackingPath!!.moveTo(0f, -mMaxRadious)
                mTrackingPath!!.lineTo(0f, -mMaxRadious + (mPointDiscreteDistance + mEdge2KeduDistance + mEdge2KeduDistance + mKedu2ProgressRingDistance + mProgressRingThick))
                canvas.drawPath(mTrackingPath, mTrackingPaint)
                canvas.restore()
            }
            thumbX = getRealCosX(currentAngle, mMaxRadious)
            thumbY = getRealSinY(currentAngle, mMaxRadious)
            if (mIndicatorBmp != null) {
                val bmpWidth = mIndicatorBmp!!.width
                val bmpHeight = mIndicatorBmp!!.height
                canvas.save()
                canvas.translate(thumbX - bmpWidth.toFloat() / 2.0f, thumbY - bmpHeight.toFloat() / 2.0f)
                mThumbnailMatrix!!.setRotate(currentAngle, bmpWidth.toFloat() / 2.0f, bmpHeight.toFloat() / 2.0f)
                canvas.drawBitmap(mIndicatorBmp, mThumbnailMatrix, null)
                canvas.restore()
            }
        }
    }

    private fun drawKeDu(canvas: Canvas) {
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = dp2px(1).toFloat()
        canvas.save()
        val startY = -(mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance)
        val stopY = startY - mKeduLength
        val angle = 360.toFloat() / KeduCount
        var accuAngle = 0f
        for (i in 0..KeduCount) {
            if (accuAngle <= currentAngle) {
                mPaint!!.color = Color.parseColor("#ffffff")
            } else {
                mPaint!!.color = Color.parseColor("#1affffff")
            }
            canvas.drawLine(0f, startY, 0f, stopY, mPaint)
            canvas.rotate(angle)
            accuAngle += angle
        }
        canvas.restore()
    }

    private fun drawPoint(canvas: Canvas) {
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = Color.parseColor("#22CA92")
        val angle = 0f
        if (angle >= 0 && angle <= 360) {
            canvas.save()
            canvas.rotate(angle)
            canvas.drawCircle(0f, -(mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mMPoint2EdgeDistance - mPointRadio), mPointRadio.toFloat(), mPaint)
            canvas.restore()
        }
    }

    /*画静态背景*/
    private fun drawBg(canvas: Canvas) {
        //画背景
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = Color.parseColor("#21ffffff")
        canvas.drawCircle(0f, 0f, mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance, mPaint)
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = Color.parseColor("#1affffff")
        mPaint!!.strokeWidth = dp2px(1).toFloat()

        //话最外面的灰圈
        canvas.drawCircle(0f, 0f, mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance, mPaint)
        //画最里边的圈
        canvas.drawCircle(0f, 0f, mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - mProgressRingThick, mPaint)

        //画进度环
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = Color.parseColor("#333333")
        mPaint!!.strokeWidth = mProgressRingThick.toFloat()
        canvas.drawCircle(0f, 0f, mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - mProgressRingThick.toFloat() / 2.0f, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled) {
            this.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> updateOnTouch(event)
                MotionEvent.ACTION_MOVE -> updateOnTouch(event)
                MotionEvent.ACTION_UP -> {
                    if (!ignoreContinueMoveTouch) {
                        onStopTrackingTouch()
                    }
                    ignoreContinueMoveTouch = true
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (!ignoreContinueMoveTouch) {
                        onStopTrackingTouch()
                    }
                    ignoreContinueMoveTouch = true
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return true
        }
        return false
    }

    private fun updateOnTouch(event: MotionEvent) {
        val ignoreTouch = ignoreTouch(event.x, event.y)
        if (event.action == MotionEvent.ACTION_DOWN) {
            ignoreContinueMoveTouch = ignoreTouch
            if (ignoreTouch) {
                return
            }
            onStartTrackingTouch()
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            //down事件忽略,后面的move事件也忽略
            if (ignoreContinueMoveTouch) {
                return
            }
        }
        isPressed = true
        val touchAngle = getTouchDegrees(event.x, event.y)
        onProgressRefresh(touchAngle, true)
    }

    fun setCurrentAngle(angle: Float) {
        if (isTracking) {
            return
        }
        currentAngle = angle
    }

    fun getCurrentAngle(): Float {
        return currentAngle
    }

    /*还原到上一次track的位置*/
    fun revertTrack() {
        setCurrentAngle(beginTrackingAngle)
        invalidate()
    }

    private fun onProgressRefresh(angle: Float, isFromUser: Boolean) {
        if (angle < 0 || angle > 360) {
            return
        }
        currentAngle = angle
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener!!.onProgressChanged(this, angle, isFromUser)
        }
        invalidate()
    }

    private fun getTouchDegrees(xPos: Float, yPos: Float): Float {
        val x = xPos - mWidth / 2
        val y = yPos - mHeight / 2
        var angle = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble()) + Math.PI / 2).toFloat()
        if (angle < 0) {
            angle = 360 + angle
        }
        return angle
    }

    private fun ignoreTouch(xPos: Float, yPos: Float): Boolean {
        var ignore = false
        val diffX = xPos - mWidth / 2 - thumbX
        val diffY = yPos - mHeight / 2 - thumbY
        val touchRadius = Math.sqrt((diffY * diffY + diffX * diffX).toDouble()).toFloat()
        //        System.out.println("触摸距离========" + DisplayUtil.px2dip(getContext(),touchRadius));
        if (touchRadius > mTouchIgnoreRadius) {
            ignore = true
        }
        return ignore
    }

    private fun onStopTrackingTouch() {
        isTracking = false
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener!!.onStopTrackingTouch(this)
        }
        invalidate()
    }

    private fun onStartTrackingTouch() {
        isTracking = true
        beginTrackingAngle = currentAngle
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener!!.onStartTrackingTouch(this)
        }
    }

    private fun getRealCosX(angle: Float, radius: Float): Float {
        if (angle < 0 || angle > 360) {
            return 0f
        }
        return when {
            angle < 90 -> { // 一
                getCosX(90 - angle, radius)
            }
            angle < 180 -> { //四
                getCosX(angle - 90, radius)
            }
            angle < 270 -> { //三
                -getCosX(270 - angle, radius)
            }
            else -> { //二
                -getCosX(angle - 270, radius)
            }
        }
    }

    private fun getRealSinY(angle: Float, radius: Float): Float {
        if (angle < 0 || angle > 360) {
            return 0f
        }
        return when {
            angle < 90 -> { // 一
                -getSinY(90 - angle, radius)
            }
            angle < 180 -> { //四
                getSinY(angle - 90, radius)
            }
            angle < 270 -> { //三
                getSinY(270 - angle, radius)
            }
            else -> { //二
                -getSinY(angle - 270, radius)
            }
        }
    }

    private fun getCosX(diggre: Float, radius: Float): Float { //diggre  0-90 第一象限角度计算
        return (radius * cos(Math.toRadians(diggre.toDouble()))).toFloat()
    }

    private fun getSinY(diggre: Float, radius: Float): Float { //diggre  0-90
        return (radius * sin(Math.toRadians(diggre.toDouble()))).toFloat()
    }

    fun setOnSeekArcChangeListener(l: OnSeekChangeListener?) {
        mOnSeekArcChangeListener = l
    }

    fun setPointList() {}

    /*设置作业点的位置*/
    fun setWorkPointList() {}
    fun updateView() {
        invalidate()
    }

    interface OnSeekChangeListener {
        fun onProgressChanged(seekCircle: BrushCourseInfoCircle?, angle: Float, fromUser: Boolean)
        fun onStartTrackingTouch(seekCircle: BrushCourseInfoCircle?)
        fun onStopTrackingTouch(seekCircle: BrushCourseInfoCircle?)
    }
}