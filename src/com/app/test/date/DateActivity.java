package com.app.test.date;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.app.test.R;
import com.zhy.m.permission.MPermissions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;


public class DateActivity extends Activity implements View.OnClickListener {
    private DatePicker datePicker;
    private SwitchButton switchButton;
    private DiamondBackView dvb_view;
    //    private BarrageView barrageView;
    private static final int REQUECT_CODE_SDCARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_date);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

        }
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        dvb_view = (DiamondBackView) findViewById(R.id.dvb_view);
        switchButton = (SwitchButton) findViewById(R.id.switchButton);
//        barrageView = (BarrageView) findViewById(R.id.barrageView);

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, final boolean isChecked) {
                Log.e("jam", "onCheckedChanged: " + isChecked);
            }
        });
        switchButton.setManualChecked(true);
        findViewById(R.id.permissionBtn).setOnClickListener(this);

        // mDelegate mDaySpinner setVisibility
        if (Build.VERSION.SDK_INT > 20) {
            try {
                Field field = datePicker.getClass().getDeclaredField(
                        "mDelegate");
                field.setAccessible(true);

                Object object = field.get(datePicker);
                Class<?> class1 = object.getClass();
                Field dfField = class1.getDeclaredField("mDaySpinner");
                dfField.setAccessible(true);

                Object iObject = dfField.get(object);
                Method method = iObject.getClass().getDeclaredMethod(
                        "setVisibility", int.class);
                method.invoke(iObject, View.GONE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                Field field = datePicker.getClass().getDeclaredField(
                        "mDaySpinner");
                field.setAccessible(true);
                ((View) field.get(datePicker)).setVisibility(View.GONE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.permissionBtn) {
            if (dvb_view != null) {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dvb_view.getLayoutParams();
//                params.height = 400;
//                params.width = 600;
//                dvb_view.setLayoutParams(params);
                dvb_view.setHorizontalCount(new Random().nextInt(15)).setDiamondColor(0xff00ff00).updateView();
            }
//            if (barrageView != null)
//                barrageView.addDanmaku(true);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
////                    PermissionGen.with(DateActivity.this)
////                            .addRequestCode(100)
////                            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
////                            .request();
//
//
//                    if (!MPermissions.shouldShowRequestPermissionRationale(DateActivity.this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUECT_CODE_SDCARD)) {
//                        MPermissions.requestPermissions(DateActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    }
//                }
//            } else {
//                if (FileUtil.isSDCard()) {
//                    FileUtil.makeFile(FileUtil.SDCardPath + "/MyJJJJJJJJJJ");
//                }
//            }


//            PermissionGen.needPermission(DateActivity.this, 100,
//                    new String[] {
//                            Manifest.permission.READ_CONTACTS,
//                            Manifest.permission.RECEIVE_SMS,
//                            Manifest.permission.WRITE_CONTACTS
//                    }
//            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (barrageView != null)
//            barrageView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (barrageView != null)
//            barrageView.on();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (barrageView != null)
//            barrageView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    @ShowRequestPermissionRationale(REQUECT_CODE_SDCARD)
//    public void whyNeedSdCard() {
//        Toast.makeText(this, "I need write news to sdcard!", Toast.LENGTH_SHORT).show();
//        MPermissions.requestPermissions(DateActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//    }
//
//    @PermissionGrant(REQUECT_CODE_SDCARD)
//    public void requestSdcardSuccess() {
//        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
//    }
//
//    @PermissionDenied(REQUECT_CODE_SDCARD)
//    public void requestSdcardFailed() {
//        Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
//    }

//    @PermissionSuccess(requestCode = 100)
//    public void doSomething() {
//        Toast.makeText(this, "获取成功", Toast.LENGTH_SHORT).show();
//    }
//
//    @PermissionFail(requestCode = 100)
//    public void doFailSomething() {
//        Toast.makeText(this, "获取失败", Toast.LENGTH_SHORT).show();
//    }
}
