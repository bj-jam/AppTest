package com.app.test.williamchart.animation

import com.app.test.williamchart.data.DataPoint

class NoAnimation : ChartAnimation<DataPoint>() {

    override fun animateFrom(
            startPosition: Float,
            entries: List<DataPoint>,
            callback: () -> Unit
    ): ChartAnimation<DataPoint> = this
}
