package com.app.test.spannable;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author lcx
 * Created at 2020.1.19
 * Describe:
 */
public class ShadowView extends View {
    public ShadowView(Context context) {
        super(context);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //红色的画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        //NORMAL: 内外都模糊绘制
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));
        canvas.drawRect(200, 100, 400, 300, paint);
        //SOLID: 内部正常绘制，外部模糊
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID));
        canvas.drawRect(600, 100, 800, 300, paint);
        //INNER: 内部模糊，外部不绘制
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.OUTER));
        canvas.drawRect(200, 500, 400, 700, paint);
        //OUTER: 内部不绘制，外部模糊
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));
        canvas.drawRect(600, 500, 800, 700, paint);


        RectF rectF = new RectF(200, 200, 500, 500);
        paint.setColor(Color.GREEN);
        /**
         * Create an emboss maskfilter
         *
         * @param direction  指定光源的位置，长度为xxx的数组标量[x,y,z]
         * @param ambient    环境光的因子 （0~1），越接近0，环境光越暗
         * @param specular   镜面反射系数 越接近0，镜面反射越强
         * @param blurRadius 模糊半径 值越大，模糊效果越明显
         */
        paint.setMaskFilter(new EmbossMaskFilter(new float[]{10, 10, 10}, 0.3f, 5, 150));
        canvas.drawRect(rectF, paint);
    }
}
