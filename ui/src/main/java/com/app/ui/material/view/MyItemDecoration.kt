package com.app.ui.material.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R

/**
 */
class MyItemDecoration(context: Context, space: Int) : RecyclerView.ItemDecoration() {
    private val mDrawable: Drawable
    private val space: Int
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect[0, 0, 0] = mDrawable.intrinsicHeight
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.top = space
        }
    }

    init {
        mDrawable = context.resources.getDrawable(R.drawable.item_decoration)
        this.space = space
    }
}