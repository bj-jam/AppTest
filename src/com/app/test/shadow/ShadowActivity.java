package com.app.test.shadow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.test.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by jam on 16/11/23.
 */

public class ShadowActivity extends Activity {

    private ImageView imageView1;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //必须卸载setContentView之前
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透状态明栏 
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏 //
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_shadow);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        imageView1.setBackgroundResource(R.drawable.code6);


        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.code6);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        Log.e("jam", byteArray.toString());


        String s = new String(byteArray);

        Log.e("jam", s + "<--------");

        byte[] jj = s.getBytes();

        Log.e("jam", jj.toString());

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(jj, 0, jj.length);

        imageView2.setImageBitmap(bitmap1);
    }
}
