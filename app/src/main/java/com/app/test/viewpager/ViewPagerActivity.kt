package com.app.test.viewpager

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.PageTransformer
import android.view.View
import android.view.ViewGroup
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px

/**
 * Created by jam on 16/9/29.
 */
class ViewPagerActivity : Activity() {
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var studyTopCenter: StudyTopCenterView
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var date: Array<String?>
    private lateinit var viewList: Array<View?>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)
        intDate()
        intView()
    }

    private fun intView() {
        mViewPager = findViewById<View>(R.id.mViewPager) as androidx.viewpager.widget.ViewPager
        studyTopCenter = findViewById<View>(R.id.studyTopCenter) as StudyTopCenterView
        mViewPager.offscreenPageLimit = 3
        mViewPager.pageMargin = dp2px(-70)
        mViewPager.setPageTransformer(true, ScalePageTransformer())
        mViewPager.adapter = adapter
        studyTopCenter.initValue(viewList)
    }

    private fun intDate() {
        adapter = ViewPagerAdapter()
        date = arrayOfNulls(10)
        date[0] = ""
        viewList = arrayOfNulls(2)
        val view = View.inflate(this, R.layout.view_brush_course_info, null)
        val brushCourseCircleBar = view.findViewById<View>(R.id.brushCourseCircleBar) as BrushCourseCircleBar
        brushCourseCircleBar.startCustomAnimation()
        brushCourseCircleBar.setOnClickListener { brushCourseCircleBar.startCustomAnimation() }
        val view1 = View.inflate(this, R.layout.view_brush_course_count_info, null)
        val brushCourseMissionBar = view1.findViewById<View>(R.id.brushCourseMissionBar) as BrushCourseMissionBar
        //        brushCourseMissionBar.startCustomAnimation();
        brushCourseMissionBar.setOnClickListener { brushCourseMissionBar.startCustomAnimation() }
        viewList[0] = view1
        viewList[1] = view
    }

    private inner class ViewPagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view === o
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = View.inflate(this@ViewPagerActivity, R.layout.item_viewpager, null)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            return date.size
        }
    }

    /**
     * viewPager实现中间放大,两边缩小
     */
    inner class ScalePageTransformer : PageTransformer {
        /**
         * 核心就是实现transformPage(View page, float position)这个方法
         */
        override fun transformPage(page: View, position: Float) {
            var position = position
            if (position < -1) {
                position = -1f
            } else if (position > 1) {
                position = 1f
            }
            val tempScale = if (position < 0) 1 + position else 1 - position
            val slope = (Companion.MAX_SCALE - Companion.MIN_SCALE) / 1
            //一个公式
            val scaleValue = Companion.MIN_SCALE + tempScale * slope
            page.scaleX = scaleValue
            page.scaleY = scaleValue
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                page.parent.requestLayout()
            }
        }


    }

    companion object {
        /**
         * 最大的item
         */
        const val MAX_SCALE = 1f

        /**
         * 最小的item
         */
        const val MIN_SCALE = 0.8f
    }
}