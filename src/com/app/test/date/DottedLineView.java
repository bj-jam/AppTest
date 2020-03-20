package com.app.test.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.app.test.R;


/**
 * @author lcx
 * Created at 2019.12.18
 * Describe:菱形背景图
 */
public class DottedLineView extends View {
    //
    private static final int CUT_BOTTOM = 0;
    private static final int FIT_Y = CUT_BOTTOM + 1;
    private static final int GAP_TOP = FIT_Y + 1;
    private static final int GAP_BOTTOM = GAP_TOP + 1;
    private static final int GAP_BOTTOM_TOP = GAP_BOTTOM + 1;
    /**
     * 菱形画笔
     */
    private Paint paint;

    private int lineColor;
    private int lineWidth;
    private int orientation;
    private int dashGap;
    private int dashWidth;

    private float startX;
    private float startY;
    private float stopX;
    private float stopY;


    public DottedLineView(Context context) {
        super(context);
        init(context, null);
    }

    public DottedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DottedLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化参数
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DottedLineView);
        }
        lineColor = optColor(typedArray, R.styleable.DottedLineView_dlv_line_color, R.color._94c8f9);
        orientation = opInteger(typedArray, R.styleable.DottedLineView_dlv_orientation, 0);
        lineWidth = opInteger(typedArray, R.styleable.DottedLineView_dlv_orientation, 2);
        dashGap = opInteger(typedArray, R.styleable.DottedLineView_dlv_dash_gap, 0);
        dashWidth = opInteger(typedArray, R.styleable.DottedLineView_dlv_dash_width, 0);

        if (typedArray != null) {
            typedArray.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        paint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashGap}, 0));


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * 计算需要的数值
     *
     * @param viewWidth  view的宽
     * @param viewHeight view的高
     */
    private void calculateValue(int viewWidth, int viewHeight) {
        if (orientation == 0) {
            startX = 0;
            stopX = viewWidth;
            startY = viewHeight / 2 - lineWidth / 2;
            stopY = viewHeight / 2 - lineWidth / 2;
        } else {
            startX = viewWidth / 2 - lineWidth / 2;
            stopX = viewWidth / 2 - lineWidth / 2;
            startY = 0;
            stopY = viewHeight;
        }
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        calculateValue(width, height);
    }


    private static float optDimension(TypedArray typedArray, int index, float def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getDimension(index, def);
    }

    private static int opInteger(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getInteger(index, def);
    }

    private static int optColor(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getColor(index, def);
    }

}
