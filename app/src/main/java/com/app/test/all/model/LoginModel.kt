package com.app.test.all.model

import androidx.databinding.BaseObservable
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.View
import com.app.test.annotation.net.CheckNetAnnotation
import com.app.test.extension.NonNullObservableField
import java.util.*

/**
 *
 * @author lcx
 * Created at 2020.9.21
 * Describe:
 */
class LoginModel(activity: androidx.fragment.app.FragmentActivity) : BaseObservable() {
    val name = NonNullObservableField("")
    val info = NonNullObservableField("")
    val visibility = NonNullObservableField(View.GONE)
    val hashMap = HashMap<String, String>()

    @CheckNetAnnotation
    fun loginClick(view: View) {
        Log.e("jam", "loginClick: ")
//        val count = Random.nextInt(100);
//        info.set(count.toString())
//        when (count % 2) {
//            0 -> visibility.set(View.GONE)
//            1 -> visibility.set(View.VISIBLE)
//        }
        hashMap()
    }

    fun add(view: View) {
        addMap()
    }

    private fun hashMap() {
        hashMap.put("app", "刺客传说")
        hashMap.get("")
        hashMap.remove("")
    }

    private fun addMap() {
        hashMap.put("app1", "刺客传说")
        hashMap.put("app2", "刺客传说")
        hashMap.put("app3", "刺客传说")
        hashMap.put("app4", "刺客传说")
        hashMap.put("app5", "刺客传说")
        hashMap.put("app6", "刺客传说")
        hashMap.put("app7", "刺客传说")
        hashMap.put("app8", "刺客传说")
        hashMap.put("app9", "刺客传说")
        hashMap.put("app10", "刺客传说")
        hashMap.put("app11", "刺客传说")
        hashMap.put("app12", "刺客传说")
        hashMap.put("app13", "刺客传说")
        hashMap.put("app14", "刺客传说")
        hashMap.put("app15", "刺客传说")
        hashMap.put("app16", "刺客传说")
        hashMap.put("app17", "刺客传说")
        hashMap.put("app18", "刺客传说")
        hashMap.put("app19", "刺客传说")
    }
}