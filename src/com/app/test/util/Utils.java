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
    public static boolean isEmpty(Object object) {
        return object == null;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

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
