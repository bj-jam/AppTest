package com.app.test.williamchart.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.app.test.R
import com.app.test.williamchart.ChartContract
import com.app.test.williamchart.animation.NoAnimation
import com.app.test.williamchart.data.*
import com.app.test.williamchart.extensions.drawChartBar
import com.app.test.williamchart.extensions.obtainStyledAttributes
import com.app.test.williamchart.renderer.BarChartRenderer

class BarChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AxisChartView(context, attrs, defStyleAttr), ChartContract.BarView {

    @Suppress("MemberVisibilityCanBePrivate")
    var spacing = defaultSpacing

    @ColorInt
    @Suppress("MemberVisibilityCanBePrivate")
    var barsColor: Int = defaultBarsColor

    @Suppress("MemberVisibilityCanBePrivate")
    var barRadius: Float = defaultBarsRadius

    @Suppress("MemberVisibilityCanBePrivate")
    var barsBackgroundColor: Int = -1

    override val chartConfiguration: ChartConfiguration
        get() =
            BarChartConfiguration(
                    width = measuredWidth,
                    height = measuredHeight,
                    paddings = Paddings(
                            paddingLeft.toFloat(),
                            paddingTop.toFloat(),
                            paddingRight.toFloat(),
                            paddingBottom.toFloat()
                    ),
                    axis = axis,
                    labelsSize = labelsSize,
                    scale = scale,
                    barsBackgroundColor = barsBackgroundColor,
                    labelsFormatter = labelsFormatter
            )

    init {
        renderer = BarChartRenderer(this, painter, NoAnimation())
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.BarChartAttrs))
        handleEditMode()
    }

    override fun drawBars(
            points: List<DataPoint>,
            innerFrame: Frame
    ) {

        val halfBarWidth =
                (innerFrame.right - innerFrame.left - (points.size + 1) * spacing) / points.size / 2

        painter.prepare(color = barsColor, style = Paint.Style.FILL)
        points.forEach {
            val bar = RectF(
                    it.screenPositionX - halfBarWidth,
                    it.screenPositionY,
                    it.screenPositionX + halfBarWidth,
                    innerFrame.bottom
            )
            canvas.drawChartBar(bar, barRadius, painter.paint)
        }
    }

    override fun drawBarsBackground(points: List<DataPoint>, innerFrame: Frame) {

        val halfBarWidth =
                (innerFrame.right - innerFrame.left - (points.size + 1) * spacing) / points.size / 2

        painter.prepare(color = barsBackgroundColor, style = Paint.Style.FILL)
        points.forEach {
            val bar = RectF(
                    it.screenPositionX - halfBarWidth,
                    innerFrame.top,
                    it.screenPositionX + halfBarWidth,
                    innerFrame.bottom
            )
            canvas.drawChartBar(bar, barRadius, painter.paint)
        }
    }

    override fun drawLabels(xLabels: List<Label>) {

        painter.prepare(
                textSize = labelsSize,
                color = labelsColor,
                font = labelsFont
        )
        xLabels.forEach {
            canvas.drawText(
                    it.label,
                    it.screenPositionX,
                    it.screenPositionY,
                    painter.paint
            )
        }
    }

    override fun drawDebugFrame(outerFrame: Frame, innerFrame: Frame, labelsFrame: List<Frame>) {
        painter.prepare(color = -0x1000000, style = Paint.Style.STROKE)
        canvas.drawRect(outerFrame.toRect(), painter.paint)
        canvas.drawRect(innerFrame.toRect(), painter.paint)
        labelsFrame.forEach { canvas.drawRect(it.toRect(), painter.paint) }
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {
            spacing = getDimension(R.styleable.BarChartAttrs_chart_spacing, spacing)
            barsColor = getColor(R.styleable.BarChartAttrs_chart_barsColor, barsColor)
            barRadius = getDimension(R.styleable.BarChartAttrs_chart_barsRadius, barRadius)
            barsBackgroundColor =
                    getColor(R.styleable.BarChartAttrs_chart_barsBackgroundColor, barsBackgroundColor)
            recycle()
        }
    }

    companion object {
        private const val defaultSpacing = 10f
        private const val defaultBarsColor = Color.BLACK
        private const val defaultBarsRadius = 0F
    }
}
