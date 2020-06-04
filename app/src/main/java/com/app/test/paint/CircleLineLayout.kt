package com.app.test.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.Utils
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.abs

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:线上圆点变颜色
 */
class CircleLineLayout(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attributeSet, defStyleAttr) {
    private var during = DEFAULT_DURING
    private var strokeWidth = 0f
    private var strokeRadius = 0f
    private var strokeColor = 0
    private var lineColor = 0
    private var lineWidth = 0f
    private var avaBallSpace = 0f
    private var ballRadius = 0f
    private lateinit var paint: Paint
    private lateinit var path: Path
    private var pathMeasure: PathMeasure? = null
    private val pointList: MutableList<Point> = CopyOnWriteArrayList()
    private var point: Point? = null
    private var next: Long = 0
    private var colorList: List<Int> = CopyOnWriteArrayList()

    @JvmOverloads
    constructor(context: Context, paramAttributeSet: AttributeSet? = null) : this(context, paramAttributeSet, 0) {
    }

    private fun init(paramContext: Context, paramAttributeSet: AttributeSet?) {
        val typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CircleLineLayout)
        during = typedArray.getInt(R.styleable.CircleLineLayout_mcll_during, during)
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_stroke_width, dp2px(10)).toFloat()
        strokeColor = typedArray.getColor(R.styleable.CircleLineLayout_mcll_stroke_color, Color.TRANSPARENT)
        strokeRadius = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_stroke_radius, dp2px(13)).toFloat()
        lineColor = typedArray.getColor(R.styleable.CircleLineLayout_mcll_line_color, Color.parseColor("#fd7813"))
        lineWidth = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_line_width, dp2px(2)).toFloat()
        avaBallSpace = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_ball_space, dp2px(15)).toFloat()
        ballRadius = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_ball_radius, dp2px(3)).toFloat()
        typedArray.recycle()
        if (ballRadius * 2 > strokeWidth) {
            ballRadius = strokeWidth / 2
        }
        if (during > DEFAULT_DURING) {
            during = DEFAULT_DURING
        }
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        path = Path()
        colorList = defaultColors.getColorList()
    }

    private fun IntArray.getColorList(): List<Int> {
        var c = this
        if (Utils.isEmpty(c)) {
            c = defaultColors
        }
        val colorList: MutableList<Int> = CopyOnWriteArrayList()
        for (i in c.indices) {
            colorList.add(c[i])
        }
        return colorList
    }

    private fun drawBallStroke(canvas: Canvas) {
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
        paint.color = lineColor
        paint.strokeWidth = lineWidth
        canvas.drawPath(path, paint)
    }

    private fun drawBall(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        if (Utils.isEmpty(colorList)) {
            return
        }
        val n = colorList.size
        if (next >= Long.MAX_VALUE / 2) {
            next = 0
        }
        ++next
        for (i in pointList.indices) {
            point = pointList[i]
            val color = abs(((i + next) % n).toInt())
            paint.color = colorList[color]
            canvas.drawCircle(point?.x?.toFloat() ?: 0f, point?.y?.toFloat()
                    ?: 0f, ballRadius, paint)
        }
    }

    private val runnable = Runnable { postInvalidate() }

    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawBallStroke(canvas)
        drawBall(canvas)
        cycle()
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
        val pathLength = pathMeasure?.length ?: 0f
        if (avaBallSpace <= 0) {
            avaBallSpace = pathLength * 1.0f / (pathLength / (ballRadius * 2))
        }
        var tempBallSpace = avaBallSpace
        val item = tempBallSpace + 2 * ballRadius
        val count = (pathLength / item + 0.5f).toInt()
        tempBallSpace = pathLength * 1.0f / count + 2 * ballRadius
        val pos = FloatArray(2)
        var distance = 0f
        while (distance <= pathLength) {
            if (pathMeasure?.getPosTan(distance, pos, null) == true) {
                if (pathLength - distance >= tempBallSpace) {
                    pointList.add(Point(pos[0].toInt(), pos[1].toInt()))
                }
            }
            distance += tempBallSpace
        }
    }

    private fun cycle() {
        removeCallbacks(runnable)
        postDelayed(runnable, during.toLong())
    }

    companion object {
        private const val DEFAULT_DURING = 300
        private val defaultColors = intArrayOf(
                Color.parseColor("#f63a0c")
                , Color.parseColor("#46de4d")
                , Color.parseColor("#5d8dd4")
                , Color.parseColor("#f7b90c"))
    }

    init {
        init(context, attributeSet)
    }
}