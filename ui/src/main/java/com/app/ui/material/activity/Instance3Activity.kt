package com.app.ui.material.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.app.ui.material.adapter.CustomPagerAdapter
import com.app.ui.material.fragment.MainFragment
import com.app.ui.material.view.CoordinatorTabLayout
import java.util.*

class Instance3Activity : BaseActivity() {
    var vp: ViewPager? = null
    var coordinatortablayout: CoordinatorTabLayout? = null
    private val mTitles = arrayOf("1", "2", "3", "4")
    private var mFragments: MutableList<Fragment>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instance3)
        vp = findViewById(R.id.vp)
        coordinatortablayout = findViewById(R.id.coordinatortablayout)
        initFragments()
        initViewPager()
        val mImageArray = intArrayOf(
                R.mipmap.bg_2,
                R.mipmap.bg_1,
                R.mipmap.bg_4,
                R.mipmap.bg_3)
        val mColorArray = intArrayOf(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light)
        coordinatortablayout?.also {
            it.setTransulcentStatusBar(this)
                    .setBackEnable(true)
                    .setImageArray(mImageArray, mColorArray)
                    .setupWithViewPager(vp)
        }
    }

    private fun initViewPager() {
        vp?.offscreenPageLimit = 4
        vp?.adapter = CustomPagerAdapter(supportFragmentManager, mFragments!!, mTitles)
    }

    private fun initFragments() {
        mFragments = ArrayList()
        for (title in mTitles) {
            mFragments?.add(MainFragment.newInstance(title))
        }
    }
}