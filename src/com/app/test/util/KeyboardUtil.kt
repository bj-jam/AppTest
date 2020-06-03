package com.app.test.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.util.Log
import android.view.View
import android.widget.EditText
import com.app.test.R

class KeyboardUtil(private val act: Activity, private val ctx: Context, examLayout: View?) {
    private val keyboardView: KeyboardView? = null
    private val k2: Keyboard = Keyboard(ctx, R.xml.symbols)// 数字键盘
    var isnun = false // 是否数据键盘
    var isupper = false // 是否大写
    private var ed: EditText? = null
    private val examLayout: View? = null
    private val screenHeight: Int
    private val listener: OnKeyboardActionListener = object : OnKeyboardActionListener {
        override fun swipeUp() {}
        override fun swipeRight() {}
        override fun swipeLeft() {}
        override fun swipeDown() {}
        override fun onText(text: CharSequence) {}
        override fun onRelease(primaryCode: Int) {}
        override fun onPress(primaryCode: Int) {}
        override fun onKey(primaryCode: Int, keyCodes: IntArray) {
            val editable = ed!!.text
            val start = ed!!.selectionStart
            if (primaryCode == Keyboard.KEYCODE_CANCEL) { // 完成
                hideKeyboard()
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) { // 回退
                if (editable != null && editable.length > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start)
                    }
                }
            } else {
                editable!!.insert(start, Character.toString(primaryCode.toChar()))
            }
        }
    }
    private val locations = IntArray(2)
    var y = 0f
    var isMove = false
    fun showKeyboard(edit: EditText?) {
        ed = edit
        val visibility = keyboardView?.visibility
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView?.visibility = View.VISIBLE
            ed!!.getLocationOnScreen(locations)
            y = screenHeight - locations[1].toFloat()
            Log.e("jam", locations[1].toString() + "------------>")
            // if (y > keyboardView.getHeight()) {
            // Toast.makeText(ctx, "不需要移动", Toast.LENGTH_LONG).show();
            // } else {
            // // animation(examLayout,);
            // isMove = true;
            // Toast.makeText(ctx,
            // "需要移动--->" + (keyboardView.getHeight() - y),
            // Toast.LENGTH_LONG).show();
            // animation(examLayout, 0, keyboardView.getHeight() - y);
            // }
        }
    }

    /**
     * 平移动画
     */
    private fun animation(v: Any?, f: Float, f1: Float) {
        val animator = ObjectAnimator.ofFloat(v, "translationY", f,
                f1)
        animator.duration = 200
        animator.start()
    }

    fun hideKeyboard() {
        val visibility = keyboardView?.visibility
        if (visibility == View.VISIBLE) {
            keyboardView?.visibility = View.GONE
            if (isMove) {
                isMove = false
                animation(examLayout, -((keyboardView?.height?.minus(y)) ?: 0f), 0f)
            }
        }
    }

    init {
        keyboardView?.keyboard = k2
        keyboardView?.isEnabled = true
        keyboardView?.isPreviewEnabled = false
        keyboardView?.setOnKeyboardActionListener(listener)
        screenHeight = 145
    }
}