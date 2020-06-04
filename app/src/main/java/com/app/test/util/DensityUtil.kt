package com.app.test.util

import android.content.Context
import com.app.test.base.App

/**
 *
 */
object DensityUtil {
    private var density = 0f

    @JvmStatic
    fun dp2px(dp: Int): Int {
        return (dp * density + 0.5).toInt()
    }

    @JvmStatic
    fun px2dp(px: Float): Int {
        return (px / density + 0.5f).toInt()
    }

    @JvmStatic
    fun px2sp(px: Float): Int {
        return (px / density + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(sp: Float): Int {
        return (sp * density + 0.5f).toInt()
    }

    @JvmStatic
    fun getScreenHeight(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 0
    }

    @JvmStatic
    fun getScreenWidth(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 0
    }

    init {
        density = App.context?.resources?.displayMetrics?.density ?: 0f
    }
}