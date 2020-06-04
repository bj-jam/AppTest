package com.app.test.viewflipper

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.ViewFlipper
import com.app.test.R

/**
 * @author lcx
 * Created at 2020.2.26
 * Describe:
 */
class ViewFlipperActivity : Activity() {
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var view: View
    private lateinit var tvShow: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewflipper)
        viewFlipper = findViewById(R.id.viewFlipper)
        viewFlipper.clearFocus()
        for (i in 0..9) {
            view = LayoutInflater.from(this).inflate(R.layout.item_viewflipper, null)
            tvShow = view.findViewById(R.id.tv_show)
            tvShow.text = "立元$i"
            viewFlipper.addView(view)
        }
        viewFlipper.setInAnimation(this, R.anim.come_in)
        viewFlipper.setOutAnimation(this, R.anim.come_out)
        viewFlipper.setFlipInterval(2000)

        // 1、设置幻灯片的形式滚动
        // viewFlipper.startFlipping();

        // 2、设置自动翻页滚动
        viewFlipper.isAutoStart = true
        viewFlipper.isAutoStart
    }
}