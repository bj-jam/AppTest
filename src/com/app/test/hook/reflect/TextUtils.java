package com.app.test.hook.reflect;

/**
 * 代替 android.text.TextUtils
 * 在 as 中直接运行 java 程序时，相关类可使用该 TextUtils
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
