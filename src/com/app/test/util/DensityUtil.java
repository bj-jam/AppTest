package com.app.test.util;


import android.content.Context;

import com.app.test.base.App;

/**
 *
 */
public class DensityUtil {

    private static float density;

    static {
        density = App.getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param dip
     * @return
     */
    public static int dp2px(int dip) {
        return (int) (dip * density + 0.5);
    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        return 0;
    }
}
