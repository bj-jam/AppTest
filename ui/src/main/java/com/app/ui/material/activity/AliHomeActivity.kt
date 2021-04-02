package com.app.ui.material.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs

class AliHomeActivity : BaseActivity(), OnOffsetChangedListener {
    var bgContent: View? = null
    var bgToolbarClose: View? = null
    var appBar: AppBarLayout? = null
    var bgToolbarOpen: View? = null
    private var toolbarOpen: View? = null
    private var toolbarClose: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zfb_home)
        bgContent = findViewById(R.id.bg_content)
        bgToolbarClose = findViewById(R.id.bg_toolbar_close)
        appBar = findViewById(R.id.app_bar)
        bgToolbarOpen = findViewById(R.id.bg_toolbar_open)
        toolbarOpen = findViewById(R.id.include_toolbar_open)
        toolbarClose = findViewById(R.id.include_toolbar_close)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        appBar?.addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        //垂直方向偏移
        val offset = abs(verticalOffset)
        //最大偏移距离
        val scrollRange = appBarLayout.totalScrollRange
        //当滑动没超过一半时，展开状态下toolbar显示内容，根据收缩位置，改变透明值
        if (offset <= scrollRange / 2) {
            toolbarOpen?.visibility = View.VISIBLE
            toolbarClose?.visibility = View.GONE
            //根据偏移百分比，计算透明值
            val scale2 = offset.toFloat() / (scrollRange / 2)
            val alpha2 = (255 * scale2).toInt()
            bgToolbarOpen?.setBackgroundColor(Color.argb(alpha2, 25, 131, 209))
        } else { //当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
            toolbarClose?.visibility = View.VISIBLE
            toolbarOpen?.visibility = View.GONE
            val scale3 = (scrollRange - offset).toFloat() / (scrollRange / 2)
            val alpha3 = (255 * scale3).toInt()
            bgToolbarClose!!.setBackgroundColor(Color.argb(alpha3, 25, 131, 209))
        }
        //根据偏移值百分比计算扫一扫布局的透明度值
        val scale = offset.toFloat() / scrollRange
        val alpha = (255 * scale).toInt()
        bgContent?.setBackgroundColor(Color.argb(alpha, 25, 131, 209))
    }

    override fun onDestroy() {
        super.onDestroy()
        appBar?.removeOnOffsetChangedListener(this)
    }
}