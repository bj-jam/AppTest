package com.app.ui.material.listener

interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    fun onItemSelected()

    /**
     * Item在拖拽结束/滑动结束后触发
     */
    fun onItemFinish()
}