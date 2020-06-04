package com.app.test.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.app.test.R;
import com.app.test.util.FileUtil;

import java.io.File;

/**
 *
 */

public class CameraActivity extends Activity implements View.OnClickListener {
    private ImageView showImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
    }

    private void initView() {
        showImage = (ImageView) findViewById(R.id.showImage);
        findViewById(R.id.goShot).setOnClickListener(this);
        findViewById(R.id.goShot1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goShot) {
            goShot();
        } else if (v.getId() == R.id.goShot1) {
            Intent intent = new Intent(this, CutCameraActivity.class);
            startActivity(intent);

        }
    }


    private void goShot() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //将剪切的图片保存到目标Uri中
        // 存在SD
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(FileUtil.imageDir, "temp.png")));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
//            Bitmap bmp = BitmapFactory.decodeFile(FileUtil.imageDir + "temp.png");
//            showImage.setImageBitmap(bmp);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(new File(FileUtil.imageDir, "temp.png")), "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 180);
            intent.putExtra("outputY", 180);
//            intent.putExtra("scale", true);
            //将剪切的图片保存到目标Uri中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FileUtil.imageDir, "111111.png")));
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, 100);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        File file = new File(FileUtil.imageDir + "temp.png");
        if (file.exists()) {
            file.delete();
        }
        File file1 = new File(FileUtil.imageDir + "111111.png");
        if (file1.exists()) {
            file1.delete();
        }
        super.onDestroy();
    }
}
