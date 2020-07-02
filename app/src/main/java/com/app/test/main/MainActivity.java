package com.app.test.main;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.floatwindow.FloatWindowManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity implements MainAdapter.OnLister {
    // http://www.smaxe.com/order.jsf#request_evaluation_key

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    // private SmsBroadcastReceiver recevier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
//        FileUtil.queryPicture(this);
//        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//        Log.e("jam", "onCreate: " + myDevice.getName());
//         isOPen(this);
//         openGPS(this);
        checkPermission();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel("2");
            if (channel != null && channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            }
        }
        fileOutput("com.app.test", "jdfkajdsfkasjdfk");
        fileInput("com.app.test");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        }, 0, 10, TimeUnit.SECONDS);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        WindowUtils.getInstance(this).setDetailActivity(this);
//        WindowUtils.getInstance(this).showPopupWindow();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        WindowUtils.getInstance(this).hidePopupWindow();
//    }


    private void jj(String name, String info) {
        try {
            OutputStream os = openFileOutput(name, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(info);
            osw.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String jj1(String name) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new BufferedInputStream(openFileInput(name));
            baos = new ByteArrayOutputStream();
            byte[] data = new byte[1024 * 3];
            int len = 0;
            while ((len = bis.read(data)) != -1) {
                baos.write(data, 0, len);
                baos.flush();
            }
            return baos.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private void fileOutput(String name, String info) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), name);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(info.getBytes());
            fos.close();
            System.out.println("写入成功：");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String fileInput(String name) {
        try {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), name);
                FileInputStream is = new FileInputStream(file);


                byte[] b = new byte[is.available()];
                is.read(b);
                Log.e("jam", b.toString());
                Log.e("jam", new String(b));
                return b.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(recevier);
    }

    private void initData() {
        // recevier = new SmsBroadcastReceiver();
        // IntentFilter filter = new IntentFilter(
        // "android.provider.Telephony.SMS_RECEIVED");
        // filter.setPriority(1000);
        // registerReceiver(recevier, filter);

        // long l1 = System.currentTimeMillis();
        // int i =
        // calculate(transform("2+6*7-5/1*7+2+6*7-5/1*7+2+6*7-5/1*7+2+6*7-5/1*7"));
        // System.out.println(System.currentTimeMillis() - l1 + "=========" +
        // i);
        //
        // l1 = System.currentTimeMillis();
        // i = 2 + 6 * 7 - 5 / 1 * 7 + 2 + 6 * 7 - 5 / 1 * 7 + 2 + 6 * 7 - 5 / 1
        // * 7 + 2 + 6 * 7 - 5 / 1 * 7;
        // System.out.println(System.currentTimeMillis() - l1 + "=========" +
        // i);

    }

    private void initView() {

        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        adapter = new MainAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * @param context
     */
    public boolean isOPen(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // System.out.println(gps + "===" + network);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * @param context
     */
    public void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pack = pm.getPackageInfo("com.app.test",
                    PackageManager.GET_PERMISSIONS);
            String[] permissions = pack.requestedPermissions;
            for (String permission : permissions) {
                // PackageManager.PERMISSION_DENIED�ܾ���PackageManager.PERMISSION_GRANTEDͬ��
                // int num2 = checkCallingOrSelfPermission(permission);
                boolean isPermission = (PackageManager.PERMISSION_GRANTED == pm
                        .checkPermission(permission, "com.app.test"));
                if (isPermission) {
                    // System.out.println(permission + "===��Ȩ��===" + num2);
                } else {
                    // System.out.println(permission + "===ûȨ��===" + num2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSetInfo() {
        final FloatWindowManager floatWindowManager = FloatWindowManager.getInstance().init(this.getApplication());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                floatWindowManager.show();
            }
        }, 5000);

//        WindowUtils.getInstance(this).setDetailActivity(this);
//        BitmapDrawable drawable = new BitmapDrawable(getCircularBitmap(BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.code6)));
//        int i = new Random().nextInt(10);
//        WindowUtils.getInstance(this).setInfo(drawable, i, 15);
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {


        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int radius = size / 2;

        Bitmap bitmap10 = Bitmap.createScaledBitmap(bitmap, radius, radius, false);

        Bitmap output = Bitmap.createBitmap(bitmap10.getWidth(), bitmap10.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap10.getWidth(), bitmap10.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(bitmap10.getWidth() / 2, bitmap10.getHeight() / 2,
                bitmap10.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap10, rect, rect, paint);
        return output;
    }
}
