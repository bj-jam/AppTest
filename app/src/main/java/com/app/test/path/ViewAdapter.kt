package com.app.test.path

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author lcx
 * Created at 2020.3.31
 * Describe:
 */
class ViewAdapter() : PagerAdapter() {
    private var dataList: ArrayList<View>? = null;

    constructor(list: ArrayList<View>?) : this() {
        dataList = list
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return dataList?.size ?: 0
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View? = dataList?.get(position)
        view?.also {
            container.addView(view)
            return view
        }
        return View(container.context)
    }
}