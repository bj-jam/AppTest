package com.app.test.williamchart.data

import com.app.test.williamchart.renderer.RendererConstants.Companion.notInitialized

data class Scale(val min: Float, val max: Float) {
    val size = max - min
}

fun Scale.notInitialized() = max == min && min == notInitialized
