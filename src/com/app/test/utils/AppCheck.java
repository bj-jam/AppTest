package com.app.test.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测邮箱
 */
public class AppCheck {


    /**
     * 正则邮箱
     */
    public static boolean checkEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email.trim());
        boolean isMatched = matcher.matches();
        return isMatched;
    }
}
