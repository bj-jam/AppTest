package com.app.test.rotate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.app.test.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/6/29.
 */

public class MyView extends View {

    private float first = 0;// 扳手图像的当前角度

    BitmapDrawable myImage;
    /**
     * 圆弧的画笔
     */
    private Paint circlePaint;
    /**
     * 默认圆弧的画笔
     */
    private Paint defaultPaint;
    /**
     * 圆弧画笔的宽度
     */
    private int circleWidth;
    /**
     * 当前弧度
     */
    private float mSweepAnglePer;

    /**
     * 圆弧的范围框
     */
    private RectF mColorWheelRectangle;
    /**
     * 圆弧的半径
     */
    private int mRadius;


    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        mColorWheelRectangle = new RectF();

        circleWidth = DisplayUtil.dip2px(getContext(), 2);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(0xFF00c896);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);

        defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(0xFFFF00FF);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setStrokeWidth(circleWidth);


        anim = new BarAnimation();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int min = Math.min(width, height);
        mRadius = min / 2;


        mRadius = ((min - circleWidth * 2) / 2);

        float radiusX = (width - mRadius * 2) / 2;// 左边画图起点
        float radiusY = (height - mRadius * 2) / 2;// 顶部画图起点
        if (myImage != null) {
            canvas.save();
            // 设置扳手图像的旋转角度和旋转轴心坐标（后两个参数，注意这个坐标是相对于屏幕的），该轴心也是图像的正中心
            canvas.rotate(first + mSweepAnglePer, mRadius + circleWidth, mRadius + circleWidth);
            myImage.setBounds(circleWidth, circleWidth, min - circleWidth, min - circleWidth);

            myImage.draw(canvas);
            canvas.restore();
        }

        // 画图矩形区域——左边起点，底部起点，左边起点+直径，顶部起点+直径
        mColorWheelRectangle.set(radiusX, radiusY, 2 * mRadius + radiusX, 2
                * mRadius + radiusY);
        canvas.drawArc(mColorWheelRectangle, -90, 360, false,
                defaultPaint);
        canvas.drawArc(mColorWheelRectangle, -90, first + mSweepAnglePer, false,
                circlePaint);

    }

    public void setInfo(BitmapDrawable bitmapDrawable, int playTime, int allTime) {
        this.myImage = bitmapDrawable;
        if (playTime > 0 && allTime > 0) {
            this.first = ((float) playTime / (float) allTime) * 360;
        } else {
            this.first = 0;
        }
        if (allTime > playTime) {//总时间大于已播放时间
            anim.setDuration((allTime - playTime) * 1000);
        } else {
            anim.setDuration(60000);
        }
        startAnimation(anim);
    }

    public void stopAni() {
        clearAnimation();
    }

    public void startAni() {
        startAnimation(anim);
    }


    private BarAnimation anim;


    public class BarAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = interpolatedTime * (360 - first);
            }
            postInvalidate();
        }
    }
}
