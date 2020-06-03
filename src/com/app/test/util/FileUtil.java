package com.app.test.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.app.test.util.LocalCache.FileKey;
import com.app.test.util.LocalCache.Key;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class FileUtil {

    /**
     * SD卡路径
     */
    public static final String SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String downFile = SDCardPath + "/AppTest/image/";
    public static final String imageDir = SDCardPath + "/AppTest/ImageFile/";
    public static final String imagePath = SDCardPath + "/AppTest/ImageFile/";

    public static void init() {
        FileUtil.makeFile(LocalCache.getInstance().getCache(FileKey.ABORT_PICTURE, Key.imageDir));
    }

    public static boolean isSDCard() {
        // TODO SD卡是否存在,true为存在
        String sdState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(sdState);
    }

    /**
     * 创建文件或文件夹
     */
    public static void makeFile(String path) {
        // TODO 创建文件或文件夹
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }


    /**
     * 删除文件
     */
    public static void deleteFile(String path) {
        // TODO 删除文件
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                delete(file);
            }
        }
    }

    private static void delete(File f) {
        if (Utils.isEmpty(f))
            return;
        if (f.isFile()) {
            f.delete();
        } else {
            File[] fs = f.listFiles();
            if (fs == null || fs.length == 0) {
                f.delete();
            } else {
                for (File file : fs) {
                    delete(file);
                }
            }
            f.delete();
        }
    }


    /**
     * 根据系统返回的UIR查询图片库里的图片资源
     */
    public static String queryPicture(Context context, Intent data) {
        Uri uri = data.getData();
        String path = uri.getPath();
        if (!Utils.isEmpty(path) && !path.contains(".")) {
            String[] paths = {MediaColumns.DATA};
            Cursor c = context.getContentResolver().query(uri, paths, null,
                    null, null);
            if (!Utils.isEmpty(c)) {
                c.moveToFirst();
                path = c.getString(c.getColumnIndex(paths[0]));
                c.close();
            }
        }
        return path;
    }

    /**
     * 图片按大小压缩
     */
    public static Bitmap getBitmap(Bitmap bm, float w, float h) {
        Matrix m = new Matrix();
        m.postScale(w / bm.getWidth(), h / bm.getHeight());
        // resize后的Bitmap对象
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m,
                true);
    }

    /**
     * 图片按大小压缩
     */
    public static Bitmap getBitmap(String path, int w, int h)
            throws FileNotFoundException {
        // TODO 图片按大小压缩
        BitmapFactory.Options options = new Options();
        // 如果我们把options.inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String
        // path, Options
        // opt)并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float ow = options.outWidth;
        float oh = options.outHeight;
        int inSampleSize = 1;
        float sw = ow / w;
        float sh = oh / h;
        if (sw > sh) {
            inSampleSize = (int) Math.ceil(sw);
        } else {
            inSampleSize = (int) Math.ceil(sh);
        }
        inSampleSize = inSampleSize % 2 == 0 ? inSampleSize : inSampleSize + 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        // 允许可清除
        // 如果inPurgeable设为True的话表示使用BitmapFactory创建的Bitmap用于存储Pixel的内存空间在系统内存不足时可以被
        // 回收，在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel），系统会再次调用BitmapFactory
        // decoder重新生成Bitmap的Pixel数组.
        // 为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据.在inPurgeable为false时表示创建的Bitmap的Pixel内存空间不能被回收，
        // 这样BitmapFactory在不停decodeByteArray创建新的Bitmap对象，不同设备的内存不同，因此能够同时创建的Bitmap个数可能有所不同，
        // 200个bitmap足以使大部分的设备重新OutOfMemory错误.当isPurgable设为true时，系统中内存不足时，可以回收部分Bitmap占据的内存空间，
        // 这时一般不会出现OutOfMemory 错误.
        options.inPurgeable = true;
        // 和inPurgeable一起使用才有作用
        options.inInputShareable = true;
        // 使图片变成原来的1/inSampleSize
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(new FileInputStream(new File(path)),
                new Rect(), options);
    }

    /**
     * 获取压缩的图片文件路径
     */
    public static String getCompressImagePath(String path, int w, int h) {
        try {
            BitmapFactory.Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int ow = options.outWidth;
            int oh = options.outHeight;
            if (ow <= w && oh <= h) {
                return "";
            }
            Bitmap bm = getBitmap(path, w, h);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath));
            bm.compress(CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取压缩的图片文件路径
     */
    public static String getCompressImagePathSmall(String path, int w, int h) {
        try {
            BitmapFactory.Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int ow = options.outWidth;
            int oh = options.outHeight;
            if (ow <= w && oh <= h) {
                return "";
            }
            Bitmap bm = getBitmap(path, w, h);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(imagePath));
            // ---------压缩后的新图片
            int sample = 80, size = 50 * 1024;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            for (int os = 100; baos.toByteArray().length > size && os > 0; ) {
                os = os - 5;
                baos.reset();
                bm.compress(Bitmap.CompressFormat.JPEG, os, baos);
                sample = os;
            }
            baos.close();

            bm.compress(CompressFormat.JPEG, sample, bos);
            bos.flush();
            bos.close();
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取带白边圆图<br>
     * imgId图片资源ID<br>
     * dis为白边宽度<br>
     * color格式为(#RRGGBB，#AARRGGBB，'red'，'blue'，'green'，'black'，'white'，
     * 'gray'，'cyan'，'magenta'，'yellow'，'lightgray'，'darkgray')
     */
    public static Bitmap getImageCircle(Context context, int imgId, int dis,
                                        String color) {
        // TODO 获取带白边圆图
        try {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                    imgId);
            return getImageCircle(context, bm, dis, color);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取带白边圆图<br>
     * path为图片地址<br>
     * dis为白边宽度<br>
     * color格式为(#RRGGBB，#AARRGGBB，'red'，'blue'，'green'，'black'，'white'，
     * 'gray'，'cyan'，'magenta'，'yellow'，'lightgray'，'darkgray')
     */
    public static Bitmap getImageCircle(Context context, String path, int dis,
                                        String color, int defId) {
        // TODO 获取带白边圆图
        try {
            if (!isSDCard()) {
                return getImageCircle(context, defId, dis, color);
            }
            if (path.startsWith("http://") || path.startsWith("https://")) {
                path = downFile + path.hashCode();
            }
            Bitmap bm = BitmapFactory.decodeFile(path);
            return getImageCircle(context, bm, dis, color);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取带白边圆图<br>
     * dis为白边宽度<br>
     * color格式为(#RRGGBB，#AARRGGBB，'red'，'blue'，'green'，'black'，'white'，
     * 'gray'，'cyan'，'magenta'，'yellow'，'lightgray'，'darkgray')
     */
    public static Bitmap getImageCircle(Context context, Bitmap bm, int dis,
                                        String color) throws Exception {
        // TODO 获取带白边圆图
        if (bm == null) {
            return null;
        }
        int radius = 0;
        dis = dis <= 0 ? 5 : dis;
        int w = bm.getWidth();
        int h = bm.getHeight();
        radius = h / 2;
        if (w > h) {
            bm = Bitmap.createBitmap(bm, (w - h) / 2, 0, h, h);
        } else if (w < h) {
            radius = w / 2;
            bm = Bitmap.createBitmap(bm, 0, (h - w) / 2, w, w);
        }
        // Bitmap里边有两个枚举类，一个是Bitmap.CompressFormat，另一个是Bitmap.Config。第一个包含的是Bitmap可用于指定的压缩格式，有JPEG，PNG，WEBP三种格式。第二个包含的是Bitamap的有可能用到的颜色组合，有ALPHA_8
        // ，ARGB_4444，ARGB_8888，RGB_565。
        Bitmap bw = Bitmap.createBitmap(radius * 2, radius * 2, bm.getConfig());
        Canvas c = new Canvas(bw);
        Paint p = new Paint();
        p.setAntiAlias(true);
        c.drawCircle(radius, radius, radius, p);
        // 设置图像的叠加模式
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        c.drawBitmap(bm, 0, 0, p);

        bm = Bitmap.createBitmap((radius + dis) * 2, (radius + dis) * 2,
                bm.getConfig());
        c = new Canvas(bm);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.parseColor(color));
        c.drawCircle(radius + dis, radius + dis, radius + dis, p);
        c.drawBitmap(bw, dis, dis, p);

        if (bw != null && !bw.isRecycled()) {
            bw.recycle();
            bw = null;
        }
        return bm;
    }

    /**
     * 翻转图片 <br>
     * id 图片id<br>
     * sx,sy 缩放比例<br>
     * rotate 旋转角度
     */
    public static Bitmap getBitmapScale(Context context, int id, int sx,
                                        int sy, int rotate) {
        // TODO 翻转图片
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
        Matrix mx = new Matrix();
        mx.setScale(sx, sy);
        Bitmap bw = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                bm.getHeight(), mx, true);
        mx.setRotate(rotate);
        Bitmap rbw = Bitmap.createBitmap(bw, 0, 0, bw.getWidth(),
                bw.getHeight(), mx, true);
        if (bm != null) {
            bm.recycle();
            bm = null;
        }
        if (bw != null) {
            bw.recycle();
            bw = null;
        }
        if (mx != null) {
            mx = null;
        }
        return rbw;
    }


    private static String[] num = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};

    public static String getText4Num(int i) {
        if (i < 0) {
            return "" + i;
        }
        if (i < 9) {
            return num[i];
        } else if (i == 9) {
            return "十";
        } else if (i < 19) {
            return "十" + num[i % 10];
        } else if (i < 99) {
            i = i + 1;
            return num[i / 10 - 1] + "十" + (i % 10 == 0 ? "" : num[i % 10 - 1]);
        } else if (i == 99) {
            return "一百";
        } else if (i < 999) {
            i = i + 1;
            if (i % 100 == 0) {
                return num[i / 100 - 1] + "百";
            }
            return num[i / 100 - 1] + "百"
                    + (i % 100 / 10 == 0 ? "零" : num[i % 100 / 10 - 1] + "十")
                    + (i % 100 % 10 == 0 ? "" : num[i % 100 % 10 - 1]);
        } else if (i == 999) {
            return "一千";
        }
        return "";
    }

    /**
     * 获取日期-几月几日 星期几
     */
    @SuppressWarnings("deprecation")
    public static String getShowDate(Date d) {
        // TODO 获取日期-几月几日 星期几
        String showDate = (d.getMonth() > 8 ? d.getMonth() + 1 : "0"
                + (d.getMonth() + 1))
                + "-" + (d.getDate() > 9 ? d.getDate() : "0" + d.getDate());
        if (d.getDay() == 0) {
            showDate += "\t星期天";
        } else if (d.getDay() == 1) {
            showDate += "\t星期一";
        } else if (d.getDay() == 2) {
            showDate += "\t星期二";
        } else if (d.getDay() == 3) {
            showDate += "\t星期三";
        } else if (d.getDay() == 4) {
            showDate += "\t星期四";
        } else if (d.getDay() == 5) {
            showDate += "\t星期五";
        } else if (d.getDay() == 6) {
            showDate += "\t星期六";
        }
        return showDate;
    }

    /**
     * 格式化时间（分秒/时分秒）-- time为秒
     */
    public static String formatTime(long time, boolean haveHour) {
        long mt = time > 0 ? time : 0;
        long s = mt % 60;
        long h = mt / 3600;
        long m = (mt - s - h * 3600) / 60;
        if (haveHour) {
            return (h > 9 ? h : "0" + h) + ":" + (m > 9 ? m : "0" + m) + ":"
                    + (s > 9 ? s : "0" + s);
        }
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

    /**
     * 将时长转成秒
     */
    public static int formatSize(String videoSize) {
        // TODO 将时长转成秒
        try {
            if (videoSize.contains(":")) {
                String[] vs = videoSize.split(":");
                int size = 0;
                if (vs.length == 2) {
                    size += Integer.parseInt(vs[0]) * 60;
                    size += Integer.parseInt(vs[1]);
                } else if (vs.length == 3) {
                    size = Integer.parseInt(vs[0]) * 3600;
                    size += Integer.parseInt(vs[1]) * 60;
                    size += Integer.parseInt(vs[2]);
                }
                return size;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    private static ConnectivityManager manager;

    /*
     * 获取手机当前的网络连接状态
     */
    private static int getConnectedType(Context context) {
        if (manager == null) {
            manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                return 2;
            }
        }
        return 0;
    }

    public static String getIP(Context context) {
        try {
            int ct = getConnectedType(context);
            if (ct == 1) {
                // 取得WifiManager对象
                WifiManager wm = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                // 取得WifiInfo对象
                WifiInfo wi = wm.getConnectionInfo();
                return initIP(wi.getIpAddress());
            } else if (ct == 2) {
                for (Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0.0.0";
        }
        return "0.0.0.0";
    }

    /**
     * 获取IP地址
     */
    private static String initIP(int ip) {
        return (ip & 0xFF) + "." + (ip >> 8 & 0xFF) + "." + (ip >> 16 & 0xFF)
                + "." + (ip >> 24 & 0xFF);
    }

    /**
     * 获取IMEI号，IESI号，手机型号，手机品牌，手机号码
     */
    public static String getPhoneInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String str2 = "";
        String[] cpu = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpu[0] = cpu[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpu[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String info = "IMEI=" + tm.getDeviceId();
        info += "/IMSI=" + tm.getSubscriberId();
        info += "/Number=" + tm.getLine1Number();// 手机号码
        info += "/CPU_MODEL=" + cpu[0];// cpu型号
        info += "/CPU_HZ=" + cpu[1];// cpu功率
        info += "/MODEL=" + android.os.Build.MODEL;// 手机型号
        info += "/BRAND=" + android.os.Build.BRAND;// 手机品牌
        info += "/CODENAME=" + android.os.Build.VERSION.CODENAME;// 当前开发代号
        info += "/INCREMENTAL=" + android.os.Build.VERSION.INCREMENTAL;// 源码控制版本号
        info += "/RELEASE=" + android.os.Build.VERSION.RELEASE;// 版本字符串
        info += "/SDK=" + android.os.Build.VERSION.SDK_INT;// 版本号

        return info;
    }

    public static String getLogTime() {
        Calendar c = Calendar.getInstance();
        String h = c.get(Calendar.HOUR_OF_DAY) + "";
        int mi = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        int m = c.get(Calendar.MILLISECOND);
        return h + mi + s + m;
    }

    /**
     * 把一个view已图片的方式保存在本地
     */
    public static void addFile(Context ctx, View cutView, String path,
                               String name) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(path + name + ".png");
        if (file.exists()) {
            file.delete();
        }
        try {
            // 设置view可以缓存
            cutView.setDrawingCacheEnabled(true);
            // 获取view的bitmap
            Bitmap bmp = Bitmap.createBitmap(cutView.getDrawingCache());
            cutView.setDrawingCacheEnabled(false);
            FileOutputStream fos = new FileOutputStream(file);
            if (bmp != null) {
                bmp.compress(CompressFormat.PNG, 100, fos);
            }
            fos.flush();
            fos.close();
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(path + name + ".png"))));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * MD5加密 输入一个String(需要加密的文本)，得到一个加密输出String（加密后的文本）
     */
    public static String getMD5(String oldPwd) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(oldPwd.getBytes());
        byte[] m = md5.digest();// 加密
        return getString(m);
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    /**
     * MD5加密 输入一个String(需要加密的文本)，得到一个加密输出String（加密后的文本）
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取系统语言
     */
    public static String getSysLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    /**
     * 图片模糊
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {

        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * @param sentBitmap       bitmap
     * @param radius           模糊度 值越大越模糊
     * @param canReuseInBitmap 是否操作传过来的bitmap
     * @return
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius,
                                boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /***
     * 数字分割
     * @param num
     * @return
     */
    public static String showWatchNum(int num) {
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            return df.format(num);
        } catch (Exception e) {
            e.printStackTrace();
            return num + "";
        }
    }
}
