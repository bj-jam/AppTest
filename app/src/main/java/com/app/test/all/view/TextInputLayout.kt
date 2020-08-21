package com.app.test.all.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.app.test.R

/**
 *
 * @author lcx
 * Created at 2020.8.21
 * Describe:
 */
class TextInputLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RelativeLayout(context, attrs, defStyle) {


    init {
        View.inflate(context, R.layout.layout_text_input, this)
    }

    companion object {

    }
}