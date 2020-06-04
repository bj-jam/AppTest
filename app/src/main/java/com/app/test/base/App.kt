package com.app.test.base

import android.app.Application
import android.content.Context
import com.app.test.util.DensityUtil
import com.app.test.util.FileUtil

class App : Application() {
    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()
        context = this
        FileUtil.init()
        sWidth = DensityUtil.getScreenWidth(this) // 屏幕宽度
        sHeight = DensityUtil.getScreenHeight(this) // 屏幕高度
        // hook
//        AMSPHookHelper.hookAMSP();
//        AMSPHookHelper.hookActivityThread();
//        AMSPHookHelper.hookInstrumentation(getBaseContext());
    }

    companion object {
        /**
         * 屏幕的宽
         */
        @JvmField
        var sWidth = 0

        /**
         * 屏幕的高
         */
        @JvmField
        var sHeight = 0

        @JvmField
        var context: Context? = null
    }


//public class App extends DefaultApplicationLike {
//    /**
//     * 屏幕的宽
//     */
//    public static int sWidth;
//    /**
//     * 屏幕的高
//     */
//    public static int sHeight;
//    public static Context context;
//
//    public RunApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//
//    @Override
//    public void onCreate() {
//        // TODO Auto-generated method stub
//        super.onCreate();
//        context = getApplication();
//        /** 初始化FileUtil,必须在LocalCache后初始化否则null对象 */
//        FileUtil.init();
//        Bugly.init(getApplication(), "96605a26bb", true);
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        MultiDex.install(base);
//
//
//        // 安装tinker
//        Beta.installTinker(this);
//    }
//
//    //    @Override
////    protected void attachBaseContext(Context base) {
////        super.attachBaseContext(base);
////
////    }
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
//        getApplication().registerActivityLifecycleCallbacks(callbacks);
//    }
//
//}
}