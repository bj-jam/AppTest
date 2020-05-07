package com.app.test.williamchart.extensions

import com.app.test.williamchart.data.DataPoint

internal fun HashMap<String, Float>.toDataPoints(): List<DataPoint> {
    return entries.map {
        DataPoint(
            label = it.key,
            value = it.value,
            screenPositionX = 0f,
            screenPositionY = 0f
        )
    }
}
