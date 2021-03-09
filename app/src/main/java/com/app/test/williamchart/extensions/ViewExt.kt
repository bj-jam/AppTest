package com.app.test.williamchart.extensions

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.view.View

fun View.getDrawable(drawableRes: Int): Drawable? =
        ContextCompat.getDrawable(this.context, drawableRes)
