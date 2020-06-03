package com.app.test.util;


import android.content.Context;

import com.app.test.base.App;

/**
 *
 */
public class DensityUtil {

    private static float density;

    static {
        density = App.context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(int dp) {
        return (int) (dp * density + 0.5);
    }

    public static int px2dp(float px) {
        return (int) (px / density + 0.5f);
    }

    public static int px2sp(float px) {
        return (int) (px / density + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) (sp * density + 0.5f);
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
