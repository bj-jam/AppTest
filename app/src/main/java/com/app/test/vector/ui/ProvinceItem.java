package com.app.test.vector.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

import java.io.Serializable;

/**
 * 省份JavaBean
 */
public class ProvinceItem implements Serializable {
    private Path path;
    private int color;
    private Region region;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;

        region = new Region();
        RectF rect = new RectF();
        path.computeBounds(rect, true);
        region.setPath(path, new Region((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom));
    }

    public int getColor() {
        if (color == 0)
            return Color.BLUE;
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    //画path
    public void drawPath(Canvas canvas, Paint mPaint, boolean isSelected) {
        if (isSelected) {
            //画边框
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setShadowLayer(8, 0, 0, Color.BLACK);
            mPaint.setColor(getColor());
            mPaint.setStrokeWidth(2);
            canvas.drawPath(path, mPaint);
            mPaint.clearShadowLayer();

            //画内容
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, mPaint);
        } else {
            //画内容
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(getColor());
            canvas.drawPath(path, mPaint);

            //画边框
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(1);
            canvas.drawPath(path, mPaint);
        }
    }

    public boolean isOnTouch(float x, float y) {
        if (region == null)
            throw new RuntimeException("path has not set");
        return region.contains((int) x, (int) y);
    }

    public Region getRegion() {
        if (region == null)
            throw new RuntimeException("path has not set");
        return region;
    }
}
