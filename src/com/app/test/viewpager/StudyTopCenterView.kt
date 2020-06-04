package com.app.test.viewpager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px

class StudyTopCenterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), OnPageChangeListener {
    private var views: Array<View?>? = null
    private lateinit var viewPager: ViewPager

    /**
     * 圆点的父容器
     */
    private lateinit var pointLayout: LinearLayout
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var viewList: Array<View?>? = null

    /**
     * 设置值
     */
    fun initValue(viewList: Array<View?>?) {
        this.viewList = viewList
        viewPagerAdapter = ViewPagerAdapter()
        viewPager.adapter = viewPagerAdapter
        notifyDateChanged()
    }

    /**
     * 初始化View
     */
    private fun initView() {
        View.inflate(context, R.layout.view_video_exam, this)
        pointLayout = findViewById<View>(R.id.pointLayout) as LinearLayout
        viewPager = findViewById<View>(R.id.examViewPager) as ViewPager
        viewPager.setOnPageChangeListener(this)
    }

    /**
     * 数据变化
     */
    private fun notifyDateChanged() {
        viewPagerAdapter.notifyDataSetChanged()
        viewPager.currentItem = 0 // 默认选中第一
        setPoint(viewList?.size ?: 0)
    }

    /**
     * 设置圆点
     */
    private fun setPoint(size: Int) {
        val width = dp2px(9) // 设置原点大小
        // 滑动的指示点图片
        views = arrayOfNulls(viewList?.size ?: 0)
        if (size != 0) {
            views?.also {
                pointLayout.removeAllViews()
                for (i in 0 until size) {
                    it[i] = View(context)
                    it[i]?.layoutParams = LayoutParams(width, width)
                    val params = it[i]?.layoutParams as LayoutParams
                    params.setMargins(0, 0, width, 0) // 设置两点见的间距
                    it[i]?.setBackgroundResource(R.drawable.code6)
                    pointLayout.addView(it[i])
                }
                // 默认选择第一个
                it[0]?.setBackgroundResource(R.drawable.shape_item_dynamic)
            }

        }
    }

    // 当滑动状态改变时调用
    override fun onPageScrollStateChanged(arg0: Int) {}

    // 当当前页面被滑动时调用
    override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

    // 当新的页面被选中时调用
    override fun onPageSelected(position: Int) {
        if (viewList != null) {
            for (i in viewList!!.indices) {
                if (i != position) { // 未选中
                    views!![i]!!.setBackgroundResource(R.drawable.shadow_selecter)
                } else { // 选中
                    views!![position]!!.setBackgroundResource(R.drawable.shape_black_cricle)
                }
            }
        }
    }

    /**
     * pagerView的适配器
     */
    private inner class ViewPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return if (viewList == null) 0 else viewList!!.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun getItemPosition(`object`: Any): Int {
            return super.getItemPosition(`object`)
        }

        override fun destroyItem(arg0: ViewGroup, arg1: Int, arg2: Any) {
            arg0.removeView(arg2 as View)
        }

        override fun instantiateItem(arg0: ViewGroup, arg1: Int): Any {
            val view = viewList!![arg1]
            arg0.addView(view)
            return view!!
        }
    }

    init {
        // TODO Auto-generated constructor stub
        if (!isInEditMode)
            initView()
    }
}