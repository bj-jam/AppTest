package com.app.test.hook.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.test.R
import kotlinx.android.synthetic.main.activity_target.*


/**
 * 目标 Activity，未在 androidManifest 中注册
 */
class TargetActivity : Activity(), View.OnClickListener, View.OnLongClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        lv.setOnClickListener(this);
        tv_info.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lv -> Log.e("jam", "click")
            R.id.tv_info -> Log.e("jam", "tv_info")
        }
        lv.startAnim()
    }

    override fun onLongClick(v: View?): Boolean {
        return false;
    }

}
