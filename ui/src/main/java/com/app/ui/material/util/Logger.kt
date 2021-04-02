package com.app.ui.material.util

import android.util.Log

/**
 */
object Logger {
    const val TAG = "jam"
    fun i(content: String?) {
        Log.i(TAG, content)
    }

    fun d(content: String?) {
        Log.d(TAG, content)
    }

    @JvmStatic
    fun e(content: String?) {
        Log.e(TAG, content)
    }
}