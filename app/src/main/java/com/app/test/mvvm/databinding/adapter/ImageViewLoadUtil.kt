package com.app.test.mvvm.databinding.adapter

import androidx.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.app.test.R
import com.bumptech.glide.Glide


/**
 *
 * @author lcx
 * Created at 2020.6.4
 * Describe:
 */
object ImageViewLoadUtil {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, bitmap: Bitmap?) {
        view.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setSrc(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url)
                .asBitmap()
                .placeholder(R.drawable.icon_pic2)
                .fitCenter()
                .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("app:imageUrl", "app:placeHolder", "app:error")
    fun loadImage(imageView: ImageView, url: String?, holderDrawable: Drawable?, errorDrawable: Drawable?) {
        Glide.with(imageView.context)
                .load(url)
                .placeholder(holderDrawable)
                .error(errorDrawable)
                .into(imageView)
    }
}