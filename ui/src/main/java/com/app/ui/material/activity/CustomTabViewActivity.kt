package com.app.ui.material.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.app.ui.material.Constant
import com.app.ui.material.adapter.TabFPagerAdapter
import com.google.android.material.tabs.TabLayout

class CustomTabViewActivity : BaseActivity() {
    lateinit var mTabLayout: TabLayout
    lateinit var mViewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tb_custome_tab_view)
        mTabLayout = findViewById(R.id.tablayout)
        mViewPager = findViewById(R.id.viewpager)
        val flag = intent.getStringExtra(Constant.JUMP_FLAG)
        val adapter = TabFPagerAdapter(supportFragmentManager, this)
        mViewPager.adapter = adapter
        mTabLayout.setupWithViewPager(mViewPager)

        for (i in 0 until mTabLayout.tabCount) {
            mTabLayout.getTabAt(i)?.customView = adapter.getTabView(i)
        }
    }
}