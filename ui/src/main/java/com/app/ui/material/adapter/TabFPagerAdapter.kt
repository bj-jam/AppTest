package com.app.ui.material.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.ui.R
import com.app.ui.material.activity.CustomTabView
import com.app.ui.material.fragment.PageFragment.Companion.newInstance

/**
 *
 */
class TabFPagerAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {
    private val count = 5
    private val titles = arrayOf("Tab1", "Tab2", "Tab3", "Tab4", "Tab5")
    private val resIds = intArrayOf(R.mipmap.ic_15, R.mipmap.ic_05, R.mipmap.ic_24, R.mipmap.ic_25, R.mipmap.ic_05)
    override fun getItem(position: Int): Fragment {
        return newInstance(position + 1)
    }

    override fun getCount(): Int {
        return count
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return getIcon(position)
        //return titles[position];
    }

    private fun getIcon(position: Int): SpannableString {
        val drawable: Drawable? = when (position) {
            0 -> ContextCompat.getDrawable(context, R.mipmap.ic_15)
            1 -> ContextCompat.getDrawable(context, R.mipmap.ic_05)
            2 -> ContextCompat.getDrawable(context, R.mipmap.ic_24)
            3 -> ContextCompat.getDrawable(context, R.mipmap.ic_25)
            4 -> ContextCompat.getDrawable(context, R.mipmap.ic_05)
            else -> ContextCompat.getDrawable(context, R.mipmap.ic_05)
        }
        drawable?.setBounds(0, 0, 30, 30)
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
        val spannableString = SpannableString(" ")
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun getTabView(position: Int): View {
        val customTabView = CustomTabView(context)
        customTabView.setData(resIds[position], titles[position])
        return customTabView
    }
}