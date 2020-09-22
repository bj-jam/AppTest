package com.app.test.all.model

import android.databinding.BaseObservable
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import com.app.test.extension.NonNullObservableField
import kotlin.random.Random

/**
 *
 * @author lcx
 * Created at 2020.9.21
 * Describe:
 */
class LoginModel(activity: FragmentActivity) : BaseObservable() {
    val name = NonNullObservableField("")
    val info = NonNullObservableField("")
    val visibility = NonNullObservableField(View.GONE)

    fun loginClick(view: View) {
        Log.e("jam", "loginClick: ")
        val count = Random.nextInt(100);
        info.set(count.toString())
        when (count % 2) {
            0 -> visibility.set(View.GONE)
            1 -> visibility.set(View.VISIBLE)
        }

    }

}