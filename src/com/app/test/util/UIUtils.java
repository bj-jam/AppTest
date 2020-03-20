package com.app.test.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;

/**
 * Created by admin on 2018/9/11.
 */

public class UIUtils {

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
