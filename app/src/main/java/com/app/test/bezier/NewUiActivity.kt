package com.app.test.bezier

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.app.test.R


/**
 *
 */
class NewUiActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ui)

        val tempControl: TempControlView = findViewById(R.id.temp_control)
        // 设置三格代表温度1度
        // 设置三格代表温度1度
        tempControl.setAngleRate(1)
        tempControl.setTemp(16, 37, 20)
        //设置旋钮是否可旋转
        //设置旋钮是否可旋转
        tempControl.canRotate = true

        tempControl.setOnTempChangeListener { temp -> Toast.makeText(this@NewUiActivity, "$temp°", Toast.LENGTH_SHORT).show() }

        tempControl.setOnClickListener(TempControlView.OnClickListener { temp -> Toast.makeText(this@NewUiActivity, "$temp°", Toast.LENGTH_SHORT).show() })
    }
}