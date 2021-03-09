package com.app.test.vector

import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.view.View
import android.widget.ImageView
import com.app.test.R

class VectorActivity : AppCompatActivity() {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector)
    }

    fun startAnim(view: View) {
        val imageView = view as ImageView
        val drawable = imageView.drawable
        (drawable as Animatable).start()
    }
}