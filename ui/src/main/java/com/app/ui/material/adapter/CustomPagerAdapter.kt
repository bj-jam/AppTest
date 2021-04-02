package com.app.ui.material.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class CustomPagerAdapter(fm: FragmentManager?, fragments: List<Fragment>, titles: Array<String>) : FragmentPagerAdapter(fm!!) {
    private var mFragments: List<Fragment> = ArrayList()
    private val mTitles: Array<String>
    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    init {
        mFragments = fragments
        mTitles = titles
    }
}