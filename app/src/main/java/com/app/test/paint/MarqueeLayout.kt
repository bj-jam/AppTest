package com.app.test.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.app.test.R
import com.app.test.util.Utils

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:变颜色的线条移动
 */
class MarqueeLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {
    //圆角
    private var radius = 0f

    //边框宽度
    private var strokeWidth = 0f
    private lateinit var linearGradient: LinearGradient
    private lateinit var rectF: RectF
    private lateinit var mMatrix: Matrix
    private lateinit var bitmap: Bitmap
    private lateinit var paint: Paint
    private var porterDuffXcode: PorterDuffXfermode? = null
    private lateinit var pathMeasure: PathMeasure
    private var pathLength = 0f
    private var pathLength4 = 0f
    private var step = 0f
    private var tDx = 0f
    private var tDy = 0f
    private var startA = 0f
    private var stopA = 0f
    private lateinit var dstA: Path
    private var startB = 0f
    private var stopB = 0f
    private lateinit var dstB: Path
    private var startC = 0f
    private var stopC = 0f
    private lateinit var dstC: Path
    private var startD = 0f
    private var stopD = 0f
    private lateinit var dstD: Path
    private var mWidth = 0
    private var mHeight = 0
    private var percentStep = 0f
    private var isFill = false
    private val colors = intArrayOf(Color.parseColor("#FF64A1")
            , Color.parseColor("#A643FF")
            , Color.parseColor("#64EBFF")
            , Color.parseColor("#FFFE39")
            , Color.parseColor("#FF9964")
    )

    constructor(context: Context) : this(context, null) {}
    constructor(paramContext: Context, attrs: AttributeSet?) : this(paramContext, attrs, 0) {}

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeLayout)
        val tempStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.MarqueeLayout_ml_stroke_width, 20)
        val tempRadius = typedArray.getDimensionPixelSize(R.styleable.MarqueeLayout_ml_round_radius, 10)
        val tempPercentCircumference = typedArray.getFloat(R.styleable.MarqueeLayout_ml_percent_step, 0.01f)
        typedArray.recycle()
        percentStep = 0f.coerceAtLeast(tempPercentCircumference)
        mMatrix = Matrix()
        radius = tempRadius.toFloat()
        strokeWidth = tempStrokeWidth.toFloat()
        initPaint()
    }

    private fun initPaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = strokeWidth
        if (Utils.isEmpty(porterDuffXcode)) {
            porterDuffXcode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    private fun updatePathMeasure(pathMeasure: PathMeasure) {
        /**
         * startD:开始截取位置距离path起点的位置,这不是一个坐标值,是没有负数的
         * stopD:结束点距离path起点的位置,同理上,这个是小于等于path的总长度(pathmeasure.getLength())
         * dst:截取的图形成一个path对象,
         * startWidthMoveTo:表示起点位置是否使用moveTo()
         */
        pathMeasure.getSegment(startA, stopA, dstA, true)
        pathMeasure.getSegment(startB, stopB, dstB, true)
        pathMeasure.getSegment(startC, stopC, dstC, true)
        pathMeasure.getSegment(startD, stopD, dstD, true)
    }

    private fun updateDst() {
        if (stopA >= pathLength) {
            stopB = pathLength
            startB = startA
            startA = 0f
            stopA = 0f
        }
        if (startB >= pathLength) startA += step
        stopA += step
        startB += step
        startC += step
        stopC = startC + pathLength4
        if (stopC >= pathLength) stopD += step
        if (startC >= pathLength) {
            stopD = 0f
            startD = 0f
            startC = 0f
            stopC = startC + pathLength4
        }
    }

    private fun updateTranslate() {
        tDx += step
        tDy += step
        mMatrix.setTranslate(tDx, tDy)
        linearGradient.setLocalMatrix(mMatrix)
    }

    private fun updateCanvas(canvas: Canvas) {
        if (Utils.isEmpty(bitmap)) {
            return
        }
        val saveCount = canvas.saveLayer(0.0f, 0.0f, mWidth.toFloat(), mHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawPath(dstA, paint)
        canvas.drawPath(dstB, paint)
        canvas.drawPath(dstC, paint)
        canvas.drawPath(dstD, paint)
        paint.xfermode = porterDuffXcode
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveCount)
    }

    private fun reset() {
        dstA.reset()
        dstB.reset()
        dstC.reset()
        dstD.reset()
        dstA.lineTo(0.0f, 0.0f)
        dstB.lineTo(0.0f, 0.0f)
        dstC.lineTo(0.0f, 0.0f)
        dstD.lineTo(0.0f, 0.0f)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (Utils.isEmptyAny(pathMeasure, rectF)) {
            return
        }
        updatePathMeasure(pathMeasure)
        updateDst()
        updateTranslate()
        updateCanvas(canvas)
        reset()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initPath()
    }

    private fun initPath() {
        initPaint()
        val tempWidth = width
        val tempHeight = height
        if (tempWidth <= 0 || tempHeight <= 0) {
            return
        }
        mWidth = tempWidth
        mHeight = tempHeight
        val path = Path()
        dstA = Path()
        dstB = Path()
        dstC = Path()
        dstD = Path()
        pathMeasure = PathMeasure()
        rectF = RectF(0.0f, 0.0f, mWidth.toFloat(), mHeight.toFloat())
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
        pathMeasure.setPath(path, true)
        pathLength = pathMeasure.length
        pathLength4 = pathLength / 4
        Log.e(TAG, """
     pathLength>>$pathLength
     width>>${mWidth}
     height>>${mHeight}
     """.trimIndent())
        step = percentStep * pathLength
        if (!isFill) {
            startA = 0f
            stopA = startA + pathLength / 8
            startB = pathLength - pathLength / 8
            stopB = pathLength
            startC = pathLength / 2 - pathLength / 8
            stopC = startC + pathLength4
            startD = 0f
            stopD = 0f
            isFill = true
        }
        linearGradient = LinearGradient(0.0f, 0.0f, 0.0f, mWidth.toFloat(), colors, floatArrayOf(0.2f, 0.4f, 0.6f, 0.8f, 1.0f), Shader.TileMode.REPEAT)
        paint.shader = linearGradient
        bitmap = createBitmap(mWidth, mHeight)
    }

    private fun createBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        val path = Path()
        val rectF = RectF(0.0f, 0.0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#0000ff")
        canvas.drawPath(path, paint)
        return bitmap
    }

    companion object {
        private val TAG = MarqueeLayout::class.java.simpleName
    }

    init {
        init(context, attrs)
    }
}