package com.app.test.rotation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.app.test.R;
import com.app.test.util.DensityUtil;

/**
 * Created by jam on 2018/11/6.
 */

public class BorderImageView extends View {
    private Paint paintBorder;
    private TextPaint textPaint;
    private int borderWidth;
    private boolean showText;
    private int size;
    private int radiaus;
    private Bitmap source;

    public BorderImageView(Context context) {
        super(context);
        init();
    }

    public BorderImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintBorder = new Paint();
        paintBorder.setColor(Color.parseColor("#4cffffff"));
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setAntiAlias(true);
        borderWidth = DensityUtil.dp2px(2);
        paintBorder.setStrokeWidth(borderWidth);

        textPaint = new TextPaint();
        textPaint.setTextSize(DensityUtil.sp2px(9));
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
    }

    public void setShowText(boolean show) {
        showText = show;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSource(Bitmap source) {
        this.source = source;
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        paintBorder.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (source == null) {
            source = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
        }
        if (size > 0) {
            radiaus = size / 2 - borderWidth;
        } else {
            radiaus = Math.min(getWidth(), getHeight()) / 2 - borderWidth;
        }
        //画圆形头像
        canvas.drawBitmap(circleCrop(source), borderWidth, borderWidth, null);

        //写字
        if (showText) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor("#99000000"));
                canvas.drawArc(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth, 30, 120, false, paint);
            }

            String text = "切换";
            Rect rect = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), rect);
            int textX = (getWidth() - borderWidth - Math.abs(rect.width())) / 2;
            int textY = getHeight() - borderWidth * 3;
            canvas.drawText(text, textX, textY, textPaint);
        }

        //切边框
        canvas.drawCircle(radiaus + borderWidth, radiaus + borderWidth, radiaus + borderWidth / 2, paintBorder);
    }

    private Bitmap circleCrop(Bitmap source) {
        if (source == null) return null;
        int realRadiaus = radiaus;

        Bitmap squared;
        int minSize = Math.min(source.getWidth(), source.getHeight());
        float scale = (float) realRadiaus * 2 / (float) minSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        squared = Bitmap.createBitmap(source, 0, 0, minSize, minSize, matrix, true);

        Bitmap result = Bitmap.createBitmap(realRadiaus * 2, realRadiaus * 2, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        canvas.drawCircle(realRadiaus, realRadiaus, realRadiaus, paint);
        return result;
    }
}
