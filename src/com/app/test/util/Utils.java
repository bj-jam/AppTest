package com.app.test.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ArrayRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;

import com.app.test.R;

import junit.framework.Assert;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lcx
 * Created at 2019.12.18
 * Describe:
 */
public class Utils {

    public Utils() {
    }

    public static String getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
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


    public static boolean trimToEmpty(String str) {
        return isEmpty(StringUtils.trimToEmpty(str));
    }

    public static boolean trimToEmptyNull(String str) {
        String trimToEmpty = StringUtils.trimToEmpty(str);
        return isEmpty(trimToEmpty) || StringUtils.equalsIgnoreCase(trimToEmpty, "null");
    }

    public static boolean isResIdValid(int resId) {
        return resId != 0 && resId != -1;
    }

    public static String keepTwoDecimalsDown(String value) {
        if (trimToEmpty(value)) {
            return value;
        } else {
            BigDecimal bg = (new BigDecimal(value)).setScale(2, 1);
            return String.valueOf(bg);
        }
    }

    public static String keepTwoDecimalsDownFit(String value) {
        if (trimToEmpty(value)) {
            return value;
        } else {
            BigDecimal bg = (new BigDecimal(value)).setScale(2, 1);
            return bg.toString();
        }
    }

    public static String keepTwoDecimalsUp(String value) {
        if (trimToEmpty(value)) {
            return value;
        } else {
            BigDecimal bg = (new BigDecimal(value)).setScale(2, 0);
            return String.valueOf(bg);
        }
    }

    public static String keepTwoDecimalsUpFit(String value) {
        if (trimToEmpty(value)) {
            return value;
        } else {
            BigDecimal bg = (new BigDecimal(value)).setScale(2, 0);
            return bg.toString();
        }
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return null == obj;
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    public static boolean isNotEmpty(Object obj) {
        return null != obj;
    }

    public static boolean isNotEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    public static boolean isNotEmpty(int[] objs) {
        return null != objs && objs.length > 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return null != charSequence && charSequence.length() > 0;
    }

    public static boolean hasPermission(Context ctx, String permission) {
        if (!isEmpty((Object) ctx) && !StringUtils.isEmpty(permission)) {
            int targetSdkVersion = ctx.getApplicationInfo().targetSdkVersion;
            if (targetSdkVersion >= 23) {
                return ContextCompat.checkSelfPermission(ctx, permission) == 0;
            } else {
                return PermissionChecker.checkSelfPermission(ctx, permission) == 0;
            }
        } else {
            return false;
        }
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

    public static boolean isIllegalPosition(List list, int position) {
        if (isEmpty(list)) {
            return true;
        } else {
            return position >= list.size() || position < 0;
        }
    }

    public static void initCacheBitmap(Context context, String path, View v) {
        try {
            // 扩展宽度
            int disW = DensityUtil.dp2px(20);
            // 扩展高度
            int disH = DensityUtil.dp2px(100);
            // 文字颜色
            int textColor = context.getResources()
                    .getColor(R.color.text_color3);

            // 获取控件的屏幕截图
            v.setDrawingCacheEnabled(true);
            Bitmap viewBitmap = v.getDrawingCache();
            int viewW = viewBitmap.getWidth();
            int viewH = viewBitmap.getHeight();
            Bitmap bm = Bitmap.createBitmap(viewW + disW, viewH + disH,
                    viewBitmap.getConfig());

            // 创建可修改的位图进行作图
            Canvas canvas = new Canvas(bm);
            // 设置背景颜色
            canvas.drawColor(context.getResources().getColor(R.color.white));
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(viewBitmap, disW / 2, disH * 2 / 5, paint);

            // 画顶部文字
            String text = "欢迎到此一游";
            Rect textRect = new Rect();
            paint.setColor(textColor);
            paint.setStrokeWidth(DensityUtil.dp2px(2));
            paint.setTextSize(DensityUtil.dp2px(16));
            paint.getTextBounds(text, 0, text.length(), textRect);
            // 文字左边距
            int x = (viewW + disW - textRect.width()) / 2;
            // 文字右边距
            int y = (disH * 2 / 5 - textRect.height()) / 2 + textRect.height();
            canvas.drawText(text, x, y, paint);

            // 画底部文字
            text = "欢迎到此一游";
            textRect = new Rect();
            paint.setColor(textColor);
            paint.setStrokeWidth(DensityUtil.dp2px(2));
            paint.setTextSize(DensityUtil.dp2px(16));
            paint.getTextBounds(text, 0, text.length(), textRect);
            // 文字左边距
            x = (viewW + disW - textRect.width()) / 2;
            // 文字右边距
            y = disH * 2 / 5 + viewH + ((disH * 3 / 5 - textRect.height()) / 2 + textRect.height());
            canvas.drawText(text, x, y, paint);

            // 保存图片
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bitmap转成byte数组
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 远程图片转成byte数组
     *
     * @param url
     * @return
     */

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    /**
     * @param is
     * @return
     */
    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 本地图片转成byte数组
     *
     * @param fileName
     * @param offset
     * @param len
     * @return
     */
    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }


        if (offset < 0) {
            return null;
        }
        if (len <= 0) {
            return null;
        }
        if (offset + len > (int) file.length()) {
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // ���������ļ���С������
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                return null;
            }

            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            options = null;
        }

        return null;
    }

    private String[] getImgSrcs(String htmlStr) {
        if (htmlStr == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        String[] strs = null;

        String imgRegex = "<img[^(>)(src)]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern p = Pattern.compile(imgRegex);
        Matcher m = p.matcher(htmlStr);

        while (m.find()) {
            String str = m.group(1);
            list.add(str);
        }

        if (list.size() != 0) {
            strs = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                strs[i] = list.get(i);
            }
        }
        return strs;
    }


    public void loadPicture(String picUrl) {
        Bitmap bitmapFact;
        try {
            bitmapFact = BitmapFactory.decodeStream(getImageStream(picUrl));
            saveFile(bitmapFact, "", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 从网络上获取图片,并返回输入流
     *
     * @param path 图片的完整地址
     * @return InputStream
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 保存文件
     *
     * @param bm         位图
     * @param fileName   文件名
     * @param modifyTime 修改时间
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName, String modifyTime)
            throws IOException {
        File myCaptureFile = new File(fileName);// 创建文件
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

    }

    public Bitmap getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        InputStream is = null;
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
        } else {
            is = null;
        }
        if (is == null) {
            throw new RuntimeException("stream is null");
        } else {
            try {
                byte[] data = readStream(is);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            is.close();
            return null;
        }
    }

    /*
     * 得到图片字节流 数组大小
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


}
