package com.app.test.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020.1.20
 * Describe:
 */
public class MatrixImageView extends android.support.v7.widget.AppCompatImageView {
    private Matrix mMatrix;
    private Bitmap mBitmap;

    public MatrixImageView(Context context) {
        super(context);
        mMatrix = new Matrix();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pic17);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("---> onDraw");
        //画原图
        canvas.drawBitmap(mBitmap, 0, 0, null);
        //画经过Matrix变化后的图
        canvas.drawBitmap(mBitmap, mMatrix, null);
        super.onDraw(canvas);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        System.out.println("---> setImageMatrix");
        this.mMatrix.set(matrix);
        super.setImageMatrix(matrix);
    }

    public Bitmap getBitmap() {
        System.out.println("---> getBitmap");
        return mBitmap;
    }

}
