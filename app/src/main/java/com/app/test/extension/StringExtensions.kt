package com.app.test.extension

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.app.test.base.App

fun String.logD(TAG: String = "FFF") {
    Log.d(TAG, this)
}

fun String.toast(length: Int = Toast.LENGTH_SHORT): Unit {
    Toast.makeText(App.context, this, length).show()
}

fun String.decode(): String {
    var decodeString = this
    if (decodeString.contains("&amp;")) {
        decodeString = decodeString.replace("&amp;", "&")
    }
    if (decodeString.contains("&quot;")) {
        decodeString = decodeString.replace("&quot;", "\"")
    }
    if (decodeString.contains("&gt;")) {
        decodeString = decodeString.replace("&gt;", ">")
    }
    if (decodeString.contains("&lt;")) {
        decodeString = decodeString.replace("&lt;", "<")
    }
    return decodeString
}

fun String.getSharedPreference(): SharedPreferences? = App.context?.getSharedPreferences(this, 0)