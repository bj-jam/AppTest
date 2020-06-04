package com.app.test.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.Utils
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.abs

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:双线中间圆点移动
 */
class SpotMoveLayout(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attributeSet, defStyleAttr) {
    private var strokeWidth = 0f
    private var strokeRadius = 0f
    private var innerStrokeRadius = 0f
    private var strokeColor = 0
    private var during = DEFAULT_DURING
    private var lineColor = 0
    private var lineWidth = 0f
    private var ballSpace = 0f
    private var ballRadius = 0f
    private var ballInnerRadius = 0f
    private var ballInnerColor = 0
    private var isBallInnerColorFellowOut = false
    private lateinit var paint: Paint
    private lateinit var ballPaint: Paint
    private lateinit var ballInnerPaint: Paint
    private lateinit var path: Path
    private lateinit var pathMeasure: PathMeasure
    private val pointList: MutableList<Point?> = CopyOnWriteArrayList()
    private var point: Point? = null
    private var next: Long = 0
    private var colorList: List<Int> = CopyOnWriteArrayList()

    @JvmOverloads
    constructor(context: Context, paramAttributeSet: AttributeSet? = null) : this(context, paramAttributeSet, 0) {
    }

    private fun init(paramContext: Context, paramAttributeSet: AttributeSet?) {
        val typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SpotMoveLayout)
        during = typedArray.getInt(R.styleable.SpotMoveLayout_mdll_during, DEFAULT_DURING)
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_stroke_width, dp2px(10)).toFloat()
        strokeColor = typedArray.getColor(R.styleable.SpotMoveLayout_mdll_stroke_color, Color.TRANSPARENT)
        strokeRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_stroke_radius, dp2px(16)).toFloat()
        innerStrokeRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_stroke_radius, dp2px(8)).toFloat()
        lineColor = typedArray.getColor(R.styleable.SpotMoveLayout_mdll_line_color, Color.parseColor("#fb7812"))
        lineWidth = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_line_width, dp2px(1)).toFloat()
        ballSpace = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_ball_space, dp2px(17)).toFloat()
        ballRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_ball_radius, dp2px(4)).toFloat()
        ballInnerRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_ball_inner_radius, dp2px(2)).toFloat()
        ballInnerColor = typedArray.getColor(R.styleable.SpotMoveLayout_mdll_ball_inner_color, Color.WHITE)
        isBallInnerColorFellowOut = typedArray.getBoolean(R.styleable.SpotMoveLayout_mdll_ball_inner_fellow_out, isBallInnerColorFellowOut)
        typedArray.recycle()
        ballSpace /= 2f
        if (innerStrokeRadius > strokeRadius) {
            innerStrokeRadius = strokeRadius / 2
        }
        if (ballRadius * 2 > strokeWidth) {
            ballRadius = strokeWidth / 2
        }
        if (ballInnerRadius > ballRadius) {
            ballInnerRadius = ballRadius / 2
        }
        if (during > DEFAULT_DURING) {
            during = DEFAULT_DURING
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        ballPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        ballInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        path = Path()
        colorList = defaultColors.getColorList()
    }

    private fun IntArray.getColorList(): List<Int> {
        var colors = this
        if (Utils.isEmpty(colors)) {
            colors = defaultColors
        }
        val colorList: MutableList<Int> = CopyOnWriteArrayList()
        for (i in colors.indices) {
            colorList.add(colors[i])
        }
        return colorList
    }

    private fun updateDraw(canvas: Canvas) {
        if (next >= Long.MAX_VALUE / 2) {
            next = 0
        }
        ++next
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
        if (Utils.isEmpty(pointList)) {
            return
        }
        val n = colorList.size
        var colorIndex = abs((next % n).toInt())
        colorIndex = 0.coerceAtLeast(colorIndex)
        val color = colorList[colorIndex]
        drawBallStroke(canvas, color)
        drawBall(canvas, color)
    }

    private val runnable = Runnable { postInvalidate() }

    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        updateDraw(canvas)
        cycle()
    }

    private fun cycle() {
        removeCallbacks(runnable)
        postDelayed(runnable, during.toLong())
    }

    private fun drawBallStroke(canvas: Canvas, color: Int) {
        lineColor = color
        val width = width
        val height = height
        val outStrokeWidthSpace = lineWidth / 2
        val out = RectF(outStrokeWidthSpace, outStrokeWidthSpace, width - outStrokeWidthSpace, height - outStrokeWidthSpace)
        paint.color = lineColor
        paint.strokeWidth = lineWidth
        canvas.drawRoundRect(out, strokeRadius, strokeRadius, paint)
        val innerStrokeWidthSpace = strokeWidth - lineWidth / 2 + 1
        val inner = RectF(innerStrokeWidthSpace, innerStrokeWidthSpace, width - innerStrokeWidthSpace, height - innerStrokeWidthSpace)
        paint.color = lineColor
        paint.strokeWidth = lineWidth
        canvas.drawRoundRect(inner, innerStrokeRadius, innerStrokeRadius, paint)
    }

    private fun drawBall(canvas: Canvas, color: Int) {
        if (Utils.isEmpty(colorList)) {
            return
        }
        var i = if (next % 2 == 0L) 0 else 1
        while (i < pointList.size) {
            point = pointList[i]
            ballPaint.color = color
            ballPaint.style = Paint.Style.FILL
            ballPaint.maskFilter = BlurMaskFilter(ballRadius / 4, BlurMaskFilter.Blur.NORMAL)
            canvas.drawCircle(point?.x?.toFloat() ?: 0f, point?.y?.toFloat()
                    ?: 0f, ballRadius, ballPaint)
            if (!isBallInnerColorFellowOut) {
                ballInnerPaint.color = ballInnerColor
                ballInnerPaint.style = Paint.Style.FILL
                canvas.drawCircle(point?.x?.toFloat()
                        ?: 0f, point?.y?.toFloat() ?: 0f, ballInnerRadius, ballInnerPaint)
            }
            i += 2
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            initPath()
        }
    }

    private fun initPath() {
        val width = width
        val height = height
        if (width <= 0 || height <= 0) {
            return
        }
        path.reset()
        pointList.clear()
        val ballStrokeWidthSpace = strokeWidth / 2
        val localRectf = RectF(ballStrokeWidthSpace, ballStrokeWidthSpace, width - ballStrokeWidthSpace, height - ballStrokeWidthSpace)
        path.addRoundRect(localRectf, strokeRadius, strokeRadius, Path.Direction.CW)
        pathMeasure = PathMeasure(path, false)
        val pathLength = pathMeasure.length
        if (ballSpace <= 0) {
            ballSpace = pathLength * 1.0f / (pathLength / (ballRadius * 2))
        }
        var tempBallSpace = avaBallSpace
        val item = tempBallSpace + 2 * ballRadius
        val count = (pathLength / item + 0.5f).toInt()
        tempBallSpace = pathLength * 1.0f / count + 2 * ballRadius
        val pos = FloatArray(2)
        var distance = 0f
        while (distance <= pathLength) {
            if (pathMeasure.getPosTan(distance, pos, null)) {
                if (pathLength - distance >= tempBallSpace) {
                    pointList.add(Point(pos[0].toInt(), pos[1].toInt()))
                }
            }
            distance += tempBallSpace
        }
    }

    private val avaBallSpace: Float
        get() = (ballSpace - 2 * ballRadius) / 2

    companion object {
        const val DEFAULT_DURING = 300
        private val defaultColors = intArrayOf(
                Color.parseColor("#ff0012")
                , Color.parseColor("#0060ff"))
    }

    init {
        init(context, attributeSet)
    }
}