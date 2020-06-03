package com.app.test.utils

import java.util.regex.Pattern

/**
 * 检测邮箱
 */
object AppCheck {
    /**
     * 正则邮箱
     */
    fun checkEmail(email: String): Boolean {
        val check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        val regex = Pattern.compile(check)
        val matcher = regex.matcher(email.trim { it <= ' ' })
        return matcher.matches()
    }
}