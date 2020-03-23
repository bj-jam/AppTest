package com.app.test.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;

/**
 * @author lcx
 * Created at 2019.12.18
 * Describe:
 */
public class Utils {
    /**
     * 判断对象是否为空
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        return object == null;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串去掉前后空格 是否为空且是否为“null”
     *
     * @param str
     * @return
     */
    public static boolean trimToEmptyNull(String str) {
        String trimToEmpty = StringUtils.trimToEmpty(str);
        return StringUtils.isEmpty(trimToEmpty) || StringUtils.equalsIgnoreCase(trimToEmpty, "null");
    }

    public static boolean trimToEmpty(String str) {
        return StringUtils.isEmpty(StringUtils.trimToEmpty(str));
    }

    public static boolean isEmptyAny(Object... objs) {
        if (isEmpty(objs)) {
            return true;
        }
        for (Object obj : objs) {
            if (isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取需要播放的动画资源
     */
    public static int[] getRes(Context ctx, @ArrayRes int arrayResId) {
        if (Utils.isEmpty(ctx)) {
            return null;
        }
        TypedArray typedArray = ctx.getResources().obtainTypedArray(arrayResId);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }
}
