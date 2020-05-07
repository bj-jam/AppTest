package com.app.test.williamchart.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.app.test.R
import com.app.test.williamchart.ChartContract
import com.app.test.williamchart.animation.ChartAnimation
import com.app.test.williamchart.animation.DefaultDonutAnimation
import com.app.test.williamchart.animation.DonutNoAnimation
import com.app.test.williamchart.data.*
import com.app.test.williamchart.extensions.obtainStyledAttributes
import com.app.test.williamchart.renderer.DonutChartRenderer


class DonutChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ChartContract.DonutView {

    @Suppress("MemberVisibilityCanBePrivate")
    var donutThickness = defaultThickness

    @Suppress("MemberVisibilityCanBePrivate")
    var donutColors = intArrayOf(defaultColor)

    @Suppress("MemberVisibilityCanBePrivate")
    var donutBackgroundColor = defaultBackgroundColor

    @Suppress("MemberVisibilityCanBePrivate")
    var donutRoundCorners = false

    @Suppress("MemberVisibilityCanBePrivate")
    var donutTotal = defaultDonutTotal

    var animation: ChartAnimation<DonutDataPoint> = DefaultDonutAnimation()

    private lateinit var canvas: Canvas

    private var renderer: ChartContract.DonutRenderer =
            DonutChartRenderer(this, DonutNoAnimation())

    private val paint: Paint = Paint()

    private val configuration: DonutChartConfiguration
        get() =
            DonutChartConfiguration(
                    width = measuredWidth,
                    height = measuredHeight,
                    paddings = Paddings(
                            paddingLeft.toFloat(),
                            paddingTop.toFloat(),
                            paddingRight.toFloat(),
                            paddingBottom.toFloat()
                    ),
                    thickness = donutThickness,
                    total = donutTotal,
                    colorsSize = donutColors.size,
                    barBackgroundColor = donutBackgroundColor
            )

    init {
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.DonutChartAttrs))
    }

    override fun drawArc(degrees: List<Float>, innerFrame: Frame) {

        if (donutRoundCorners)
            paint.strokeCap = Paint.Cap.ROUND

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = donutThickness

        degrees.forEachIndexed { index, degree ->
            paint.color = donutColors[index]
            canvas.drawArc(
                    innerFrame.toRectF(),
                    defaultStartAngle,
                    degree,
                    false,
                    paint
            )
        }
    }

    override fun drawBackground(innerFrame: Frame) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = donutThickness
        paint.color = donutBackgroundColor

        val radius = (innerFrame.bottom - innerFrame.top) / 2
        canvas.drawCircle(
                innerFrame.left + radius,
                innerFrame.top + radius,
                radius,
                paint
        )
    }

    override fun drawDebugFrame(innerFrame: Frame) {
        canvas.drawRect(innerFrame.toRect(), paint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        renderer.draw()
    }

    fun show(values: List<Float>) {
        viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        viewTreeObserver.removeOnPreDrawListener(this)
                        renderer.preDraw(configuration)
                        return true
                    }
                })
        renderer.render(values)
    }

    fun animate(values: List<Float>) {
        viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        viewTreeObserver.removeOnPreDrawListener(this)
                        renderer.preDraw(configuration)
                        return true
                    }
                })
        renderer.anim(values, animation)
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {
            donutThickness =
                    getDimension(R.styleable.DonutChartAttrs_chart_donutThickness, donutThickness)
            donutBackgroundColor = getColor(
                    R.styleable.DonutChartAttrs_chart_donutBackgroundColor,
                    donutBackgroundColor
            )
            donutRoundCorners =
                    getBoolean(R.styleable.DonutChartAttrs_chart_donutRoundCorners, donutRoundCorners)
            donutTotal = getFloat(R.styleable.DonutChartAttrs_chart_donutTotal, donutTotal)
            recycle()
        }
    }

    companion object {
        private const val defaultThickness = 50f
        private const val defaultColor = 0
        private const val defaultBackgroundColor = Color.TRANSPARENT
        private const val defaultStartAngle = 90f
        private const val defaultDonutTotal = 100f
    }
}
