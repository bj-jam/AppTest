package com.app.test.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.base.App;
import com.app.test.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 */

public class CutCameraActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private TextView cameraInfo;
    private SurfaceHolder mSurfaceHolder = null;
    private Camera mCamera = null;
    private Camera.Parameters mParameters;
    //    private static final String IMG_PATH = "temp.png";
    private boolean isPreviewing = false;
    private File file;
    private Uri uri;
    private CameraTopRectView rectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_cut_camera);
        initView();
    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(App.sHeight, App.sWidth));
        mSurfaceView.setZOrderOnTop(false);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        cameraInfo = (TextView) findViewById(R.id.cameraInfo);
        findViewById(R.id.changeCamera).setOnClickListener(this);
        findViewById(R.id.shotPhone).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        initCamera();
        rectView = (CameraTopRectView) findViewById(R.id.rectView);
    }

    private void initDate() {

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mCamera == null) {
            initCamera();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mCamera != null) {
            isPreviewing = false;
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    // 初始化摄像头
    public void initCamera() {
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        if (mCamera != null) {
            mParameters = mCamera.getParameters();
            mParameters.setPictureFormat(PixelFormat.JPEG);
            String pictureSizeValueString = mParameters.get("picture-size-values");

            // saw this on Xperia
            if (pictureSizeValueString == null) {
                pictureSizeValueString = mParameters.get("picture-size-value");
            }

            Log.e("jam", pictureSizeValueString);
//            mParameters.set("rotation", 90);
            if (getCameraFocusable() != null) {
                mParameters.setFocusMode(getCameraFocusable());
            }
            Camera.Size size = getCloselyPreSize(App.sHeight, App.sWidth, mParameters.getSupportedPreviewSizes());
            mParameters.setPreviewSize(size.width, size.height);
            Camera.Size size1 = getCloselyPreSize(App.sHeight, App.sWidth, mParameters.getSupportedPictureSizes());
            mParameters.setPictureSize(size1.width, size1.height);
            mCamera.setParameters(mParameters);
        } else {
            Toast.makeText(this, "相机错误！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.changeCamera) {

        } else if (v.getId() == R.id.shotPhone) {
            // 拍照
            mCamera.takePicture(myShutterCallback, null,
                    myjpegCalback);
        } else if (v.getId() == R.id.cancel) {
            finish();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera == null) {
                return;
            }
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            isPreviewing = true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    private String getCameraFocusable() {
        List<String> focusModes = mParameters.getSupportedFocusModes();
        if (focusModes
                .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            return Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            return Camera.Parameters.FOCUS_MODE_AUTO;
        }
        return null;
    }


    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    Camera.PictureCallback myjpegCalback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                isPreviewing = false;
                FileOutputStream fout;
                file = new File(FileUtil.imageDir + (System.currentTimeMillis() + "").hashCode() + ".png");

                Bitmap bm1 = Bitmap.createBitmap(bitmap, (int) (rectView.getTopX() * 1.5), (int) (rectView.getTopY() * 1.5),
                        rectView.getViewWidth(), rectView.getViewHeight());// 截取
                try {
                    fout = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(
                            fout);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    bm1.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            if (getCameraId() >= 0) {
                c = Camera.open(getCameraId()); // attempt to get a Camera
                // instance
            } else {
                return null;
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("getCameraInstance", e.toString());
        }
        return c; // returns null if camera is unavailable
    }

    private int getCameraId() {
        if (!checkCameraHardware(this)) {
            return -1;
        }
        int cNum = Camera.getNumberOfCameras();
        int defaultCameraId = -1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < cNum; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
        return defaultCameraId;
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a File for saving an image or video
     */
//    private File getOutputMediaFile() {
//        File mediaStorageDir = null;
//        if (android.os.Environment.getExternalStorageState().equals(
//                android.os.Environment.MEDIA_MOUNTED)) {
//            mediaStorageDir = new File(FileUtil.imageDir, IMG_PATH);
//            if (!mediaStorageDir.exists()) {
//                if (!mediaStorageDir.mkdirs()) {
//                    return null;
//                }
//            }
//        } else {
//            Toast.makeText(this, "没有sd卡", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date());
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_" + timeStamp + ".jpg");
//
//        return mediaFile;
//    }
//    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
//        final double ASPECT_TOLERANCE = 0.1;
//        double targetRatio = (double) w / h;
//        if (sizes == null) return null;
//
//        Camera.Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        int targetHeight = h;
//
//        // Try to find an size match aspect ratio and size
//        for (Camera.Size size : sizes) {
//            double ratio = (double) size.width / size.height;
//            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
//            if (Math.abs(size.height - targetHeight) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - targetHeight);
//            }
//        }
//
//        // Cannot find the one match the aspect ratio, ignore the requirement
//        if (optimalSize == null) {
//            minDiff = Double.MAX_VALUE;
//            for (Camera.Size size : sizes) {
//                if (Math.abs(size.height - targetHeight) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - targetHeight);
//                }
//            }
//        }
//        return optimalSize;
//    }


    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> preSizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
//        if (mIsPortrait) {
//            ReqTmpWidth = surfaceHeight;
//            ReqTmpHeight = surfaceWidth;
//        } else {
        ReqTmpWidth = surfaceWidth;
        ReqTmpHeight = surfaceHeight;
//        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }
}
