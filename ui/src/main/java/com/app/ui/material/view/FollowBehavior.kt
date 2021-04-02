package com.app.ui.material.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 *
 */
class FollowBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<TextView>(context, attrs) {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: TextView, dependency: View): Boolean {
        return dependency is Button
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: TextView, dependency: View): Boolean {
        child.x = dependency.x + 200
        child.y = dependency.y + 200
        return true
    }
}