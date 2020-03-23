package com.app.test.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.app.test.R;
import com.app.test.hook.AMSPHookHelper;
import com.app.test.util.DisplayUtil;
import com.app.test.util.FileUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class App extends Application {
    /**
     * 屏幕的宽
     */
    public static int sWidth;
    /**
     * 屏幕的高
     */
    public static int sHeight;
    /**
     * ImageLoader
     */
    public static ImageLoader iLoader;
    public static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = this;
        /** 初始化DP、SP 转换为 PX 的工具 */
        DisplayUtil.getInstance().init(this);
        FileUtil.init();
        initImageLoader(this);
        // 获得屏幕宽度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sWidth = dm.widthPixels;// 屏幕宽度
        sHeight = dm.heightPixels;// 屏幕高度

        // hook
        AMSPHookHelper.hookAMSP();
        AMSPHookHelper.hookActivityThread();
//        AMSPHookHelper.hookInstrumentation(getBaseContext());
    }


    /**
     * 初始化图片加载工具
     */
    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "Test/image/");
        // 默认配置对象
        DisplayImageOptions.Builder oBuilder = new DisplayImageOptions.Builder()
                // 图片的缩放类型
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                // 缓存到内存
                .cacheInMemory(true)
                // 缓存到SD卡
                .cacheOnDisc(true)
                // 加载开始前的默认图片
                .showImageOnLoading(R.drawable.ic_launcher)
                // URL为空会显示该图片
                .showImageForEmptyUri(R.drawable.ic_launcher)
                // 加载图片出现问题，会显示该图片
                .showImageOnFail(R.drawable.ic_launcher)
                // 图片质量，防止内存溢出
                .bitmapConfig(Bitmap.Config.RGB_565);
        // 图片圆角显示，值为整数，不建议使用容易内存溢出
        // .displayer(new RoundedBitmapDisplayer(5));
        DisplayImageOptions options = oBuilder.build();

        ImageLoaderConfiguration.Builder cBuilder = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(options);

        ImageLoaderConfiguration config = cBuilder
                // 缓存在内存的图片的最大宽和高度，超过了就缩小
                .memoryCacheExtraOptions(400, 400)
                // CompressFormat.PNG类型，70质量（0-100）
                .discCacheExtraOptions(400, 400, CompressFormat.PNG, 70, null)
                // 线程池大小
                .threadPoolSize(5)
                // 线程优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 弱引用内存
                .memoryCache(new WeakMemoryCache())
                // 内存缓存大小
                // .memoryCacheSize(20 * 1024 * 1024)
                // 本地缓存大小
                // .discCacheSize(50 * 1024 * 1024)
                // 禁止同一张图多尺寸缓存
                .denyCacheImageMultipleSizesInMemory()
                // 设置硬盘缓存路径并不限制硬盘缓存大小
                .discCache(new UnlimitedDiscCache(cacheDir))
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        iLoader = ImageLoader.getInstance();
        // 禁用输出log功能
        L.disableLogging();
        iLoader.init(config);
    }

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
//    /**
//     * ImageLoader
//     */
//    public static ImageLoader iLoader;
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
//        /** 初始化DP、SP 转换为 PX 的工具 */
//        DisplayUtil.getInstance().init(getApplication());
//        /** 初始化FileUtil,必须在LocalCache后初始化否则null对象 */
//        FileUtil.init();
//        initImageLoader(getApplication());
//        // 获得屏幕宽度
//        WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//
//        Bugly.init(getApplication(), "96605a26bb", true);
//        sWidth = dm.widthPixels;// 屏幕宽度
//        sHeight = dm.heightPixels;// 屏幕高度
//    }
//
//
//    /**
//     * 初始化图片加载工具
//     */
//    private void initImageLoader(Context context) {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//                "Test/image/");
//        // 默认配置对象
//        DisplayImageOptions.Builder oBuilder = new DisplayImageOptions.Builder()
//                // 图片的缩放类型
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                // 缓存到内存
//                .cacheInMemory(true)
//                // 缓存到SD卡
//                .cacheOnDisc(true)
//                // 加载开始前的默认图片
//                .showImageOnLoading(R.drawable.ic_launcher)
//                // URL为空会显示该图片
//                .showImageForEmptyUri(R.drawable.ic_launcher)
//                // 加载图片出现问题，会显示该图片
//                .showImageOnFail(R.drawable.ic_launcher)
//                // 图片质量，防止内存溢出
//                .bitmapConfig(Bitmap.Config.RGB_565);
//        // 图片圆角显示，值为整数，不建议使用容易内存溢出
//        // .displayer(new RoundedBitmapDisplayer(5));
//        DisplayImageOptions options = oBuilder.build();
//
//        ImageLoaderConfiguration.Builder cBuilder = new ImageLoaderConfiguration.Builder(
//                context).defaultDisplayImageOptions(options);
//
//        ImageLoaderConfiguration config = cBuilder
//                // 缓存在内存的图片的最大宽和高度，超过了就缩小
//                .memoryCacheExtraOptions(400, 400)
//                // CompressFormat.PNG类型，70质量（0-100）
//                .discCacheExtraOptions(400, 400, CompressFormat.PNG, 70, null)
//                // 线程池大小
//                .threadPoolSize(5)
//                // 线程优先级
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                // 弱引用内存
//                .memoryCache(new WeakMemoryCache())
//                // 内存缓存大小
//                // .memoryCacheSize(20 * 1024 * 1024)
//                // 本地缓存大小
//                // .discCacheSize(50 * 1024 * 1024)
//                // 禁止同一张图多尺寸缓存
//                .denyCacheImageMultipleSizesInMemory()
//                // 设置硬盘缓存路径并不限制硬盘缓存大小
//                .discCache(new UnlimitedDiscCache(cacheDir))
//                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        iLoader = ImageLoader.getInstance();
//        // 禁用输出log功能
//        L.disableLogging();
//        iLoader.init(config);
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
////        // you must install multiDex whatever tinker is installed!
////
////    }
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
//        getApplication().registerActivityLifecycleCallbacks(callbacks);
//    }
//
//}

