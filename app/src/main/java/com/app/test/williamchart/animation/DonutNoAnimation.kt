package com.app.test.williamchart.animation

import com.app.test.williamchart.data.DonutDataPoint

class DonutNoAnimation : ChartAnimation<DonutDataPoint>() {

    override fun animateFrom(
            startPosition: Float,
            entries: List<DonutDataPoint>,
            callback: () -> Unit
    ): ChartAnimation<DonutDataPoint> = this
}
