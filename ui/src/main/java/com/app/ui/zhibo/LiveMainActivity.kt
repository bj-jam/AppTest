package com.app.ui.zhibo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.ui.R

class LiveMainActivity : FragmentActivity() {
    private var viewPager: ViewPager? = null
    private val interactiveView: InteractiveView by lazy {
        InteractiveView(this@LiveMainActivity)
    }
    private val emptyView: FrameLayout by lazy { FrameLayout(this@LiveMainActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_main)
        initView()
        initData()
    }

    /**
     * 初始化View
     */
    fun initView() {
        // 直播fragment
        val videoFragment = VideoFragment()
        supportFragmentManager.beginTransaction().add(R.id.fl_root, videoFragment).commitAllowingStateLoss()
        viewPager = findViewById(R.id.vp)
    }

    /**
     * 初始化数据
     */
    fun initData() {
        viewPager?.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 2
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                return when (position) {
                    0 -> {
                        container.addView(emptyView)
                        emptyView
                    }
                    1 -> {

                        container.addView(interactiveView)
                        interactiveView
                    }
                    else -> { // 设置默认
                        container.addView(emptyView)
                        emptyView
                    }
                }
            }
        }
        viewPager?.currentItem = 1
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}