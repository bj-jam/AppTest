package com.app.test.hook.ui

import android.app.Activity
import android.os.Bundle
import com.app.test.R


/**
 * 目标 Activity，未在 androidManifest 中注册
 */
class TargetActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
    }

}
