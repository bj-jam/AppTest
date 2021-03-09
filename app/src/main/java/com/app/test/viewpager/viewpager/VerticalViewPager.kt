package com.app.test.viewpager.viewpager

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Uses a combination of a PageTransformer and swapping X & Y coordinates
 * of touch events to create the illusion of a vertically scrolling ViewPager.
 *
 *
 * Requires API 11+
 */
class VerticalViewPager : androidx.viewpager.widget.ViewPager {

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    private fun init() {
        // The majority of the magic happens here
        setPageTransformer(false, VerticalPageTransformer())
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : PageTransformer {
        override fun transformPage(view: View, position: Float) {
            if (position <= 0) {
                view.alpha = 1f
                view.translationY = (-view.height * (1 - Math.pow(0.9, -position.toDouble()))).toFloat()
                //设置缩放中点
                view.pivotX = view.width / 2f
                view.pivotY = view.height / 2f
                //设置缩放的比例 此
                // 处设置两个相邻的卡片的缩放比率为0.9f
                val Scale = Math.pow(0.9, -position.toDouble()).toFloat()
                if (Scale > 0.7f) {
                    view.scaleX = Scale
                    view.scaleY = Scale
                } else {
                    view.alpha = 0f
                }
            } else { //(0,++)
                view.pivotY = view.height.toFloat()
                setCameraDistance(view)
                view.rotationX = 180 * -position
                view.alpha = 1 - position
            }
            view.translationX = view.width * -position
        }
    }

    /**
     * 改变视角距离, 贴近屏幕
     */
    private fun setCameraDistance(view: View) {
        val distance = 10000
        val scale = resources.displayMetrics.density * distance
        view.cameraDistance = scale
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev) // return touch coordinates to original reference frame for any child views
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }
}