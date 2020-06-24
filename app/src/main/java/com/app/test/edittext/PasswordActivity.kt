package com.app.test.edittext

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.app.test.R
import com.app.test.edittext.DigitKeyboard.DigitKeyboardClickListener
import com.app.test.edittext.PasswordEditText.PasswordFullListener

/**
 * @author lcx
 * Created at 2020.6.24
 * Describe:
 */
class PasswordActivity : Activity(), DigitKeyboardClickListener, PasswordFullListener {
    private lateinit var pwdEdit: PasswordEditText
    private lateinit var keyboard: DigitKeyboard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        initView()
        initListener()
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        pwdEdit = findViewById(R.id.et_password)
        keyboard = findViewById(R.id.custom_key_board)
    }

    /**
     * 初始化事件监听
     */
    private fun initListener() {
        keyboard.setOnDigitKeyboardClickListener(this)
        pwdEdit.setOnPasswordFullListener(this)
        pwdEdit.isEnabled = true
        pwdEdit.isFocusable = false
        pwdEdit.isFocusableInTouchMode = false
        pwdEdit.setOnClickListener { keyboard.visibility = View.VISIBLE }
    }

    override fun click(number: String) {
        pwdEdit.addPassword(number)
    }

    override fun delete() {
        pwdEdit.deleteLastPassword()
    }

    override fun passwordFull(password: String?) {
//        setButtonStatus(true);
    }

    override fun passwordChanged(password: String?) {
//        setButtonStatus(false);
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val isConsum = keyboard.dispatchKeyEventInFullScreen(event)
        return if (isConsum) isConsum else super.onKeyDown(keyCode, event)
    }
}