package com.app.test.lamp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.app.test.util.DensityUtil;


/**
 * @author lcx
 * Created at 2020.3.30
 * Describe:
 */
public class LampView extends View {

    Path path = new Path();
    Paint paint = new Paint();
    LinearGradient linearGradient;
    private int h;
    private int w;
    private int[] doughnutColors;
    private float currentValue;

    private int defaultLength = DensityUtil.dp2px(200);


    public LampView(Context context) {
        super(context);
        init();
    }

    public LampView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LampView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(20));
        doughnutColors = new int[]{Color.RED, Color.GREEN, Color.LTGRAY, Color.BLUE};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.h = h;
        this.w = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linearGradient = new LinearGradient(0, 0, w, h, doughnutColors, null,
                Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setColor(Color.RED);
        path.reset();
        if (currentValue <= w) {
            canvas.drawPoint(currentValue, 0, paint);
            canvas.drawPoint(w - currentValue, h, paint);
        } else if (currentValue > w && currentValue <= h + w) {
            canvas.drawPoint(w, currentValue - w, paint);
            canvas.drawPoint(0, h - (currentValue - w), paint);
        } else if (currentValue > h + w && currentValue <= 2 * w + h) {
            canvas.drawPoint(w - (currentValue - (h + w)), h, paint);
            canvas.drawPoint(w - (w - (currentValue - (h + w))), 0, paint);
        } else if (currentValue > 2 * w + h) {
            canvas.drawPoint(0, w - (currentValue - (2 * w + h)), paint);
            canvas.drawPoint(w, h - ((2 * w + h * 2) - currentValue), paint);
        }

//        path.moveTo(currentValue, 0);
//        path.lineTo(w, 0);
//        path.lineTo(w, h);
//        path.lineTo(0, h);
//        path.close();
//        canvas.drawPath(path, paint);
    }

    public void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(h * 2 + w * 2);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
