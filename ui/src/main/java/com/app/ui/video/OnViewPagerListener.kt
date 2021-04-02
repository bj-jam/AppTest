package com.app.ui.video

import android.view.View

interface OnViewPagerListener {
    //停止播放的监听方法
    fun onPageRelease(itemView: View?)

    //播放的监听方法
    fun onPageSelected(itemView: View?)
}