package com.app.test.extension

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.load(context: Context, url: String) {
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(this)
}
