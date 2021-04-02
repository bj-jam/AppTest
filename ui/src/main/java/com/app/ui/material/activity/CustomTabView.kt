package com.app.ui.material.activity

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.app.ui.R

/**
 *
 */
class CustomTabView : FrameLayout {
    var title_tv: TextView? = null
    var heart_iv: ImageView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.custome_tab_view, this)
        title_tv = findViewById(R.id.title_tv)
        heart_iv = findViewById(R.id.heart_iv)
    }

    fun setData(iconResId: Int, title: String?) {
        heart_iv?.setImageResource(iconResId)
        title_tv?.text = title
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            title_tv?.setTextColor(Color.RED)
        } else {
            title_tv?.setTextColor(Color.BLACK)
        }
    }
}