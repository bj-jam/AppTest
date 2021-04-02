package com.app.ui.video

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class CustomLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout), RecyclerView.OnChildAttachStateChangeListener {
    //传进来的监听接口类
    private var onViewPagerListener: OnViewPagerListener? = null

    //解决吸顶或者洗低的对象
    private var pagerSnapHelper: PagerSnapHelper? = null

    init {
        pagerSnapHelper = PagerSnapHelper()
    }

    /**
     * 当CustomLayoutManager完全放入到RecyclerView中的时候会被调用
     */
    override fun onAttachedToWindow(view: RecyclerView) {
        view.addOnChildAttachStateChangeListener(this)
        pagerSnapHelper?.attachToRecyclerView(view)
        super.onAttachedToWindow(view)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    /**
     * 将Item添加进来的时候  调用这个方法
     */
    override fun onChildViewAttachedToWindow(view: View) {
        onViewPagerListener?.onPageSelected(view)
    }

    /**
     * 监听滑动的状态
     */
    override fun onScrollStateChanged(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                //现在拿到的就是当前显示的这个item
                val snapView = pagerSnapHelper?.findSnapView(this)
                snapView?.also { onViewPagerListener?.onPageSelected(it) }

            }
        }
        super.onScrollStateChanged(state)
    }

    /**
     * 将Item移除出去的时候  调用这个方法
     */
    override fun onChildViewDetachedFromWindow(view: View) {
        onViewPagerListener?.onPageRelease(view)
    }

    fun setOnViewPagerListener(onViewPagerListener: OnViewPagerListener?) {
        this.onViewPagerListener = onViewPagerListener
    }
}