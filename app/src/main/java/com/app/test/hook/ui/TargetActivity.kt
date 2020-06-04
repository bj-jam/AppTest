package com.app.test.hook.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.app.test.R
import kotlinx.android.synthetic.main.activity_target.*


/**
 * 目标 Activity，未在 androidManifest 中注册
 */
class TargetActivity : Activity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        lv.setOnClickListener(this)
        tv_info.setOnClickListener(this)
        sb_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_progress?.text = progress.toString()
                cv?.setRadius(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        cv.setOnClickListener { }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lv -> Log.e("jam", "click")
            R.id.tv_info -> Log.e("jam", "tv_info")
        }
        lv.startAnim()
    }

}
