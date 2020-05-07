package com.app.test.williamchart.extensions

import com.app.test.williamchart.data.DonutDataPoint

fun Float.toDonutDataPoint(offset: Float): DonutDataPoint = DonutDataPoint(this + offset)
