package com.app.test.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.app.test.R;


/**
 * @author lcx
 * Created at 2019.12.18
 * Describe:菱形背景图
 */
public class DiamondBackView extends View {
    //
    private static final int CUT_BOTTOM = 0;
    private static final int FIT_Y = CUT_BOTTOM + 1;
    private static final int GAP_TOP = FIT_Y + 1;
    private static final int GAP_BOTTOM = GAP_TOP + 1;
    private static final int GAP_BOTTOM_TOP = GAP_BOTTOM + 1;
    /**
     * 菱形画笔
     */
    private Paint diamondPaint;
    /**
     * 边框画笔
     */
    private Paint framePaint;
    //菱形填充的颜色
    private int diamondColor;
    //边框的颜色
    private int frameColor;
    //一行的菱形数
    private int horizontalCount = 5;
    //一列的菱形数
    private int verticalCount = 6;
    //菱形的宽度
    private int maxWidth;
    //菱形的宽度
    private int maxHeight;
    //不铺面屏幕时 距离屏幕顶部的距离
    private int topMargin;
    //是否需要边框
    private boolean isDrawFrame;
    //边框的宽度
    private float frameWidth;
    //菱形铺满的方式
    private int showMode = CUT_BOTTOM;
    private Path path = new Path();
    //view的宽高
    private int viewWidth;
    private int viewHeight;

    public DiamondBackView(Context context) {
        super(context);
        init(context, null);
    }

    public DiamondBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiamondBackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化参数
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiamondBackView);
        }
        horizontalCount = opInteger(typedArray, R.styleable.DiamondBackView_dbv_horizontal_count, 5);
        verticalCount = opInteger(typedArray, R.styleable.DiamondBackView_dbv_vertical_count, 6);
        showMode = opInteger(typedArray, R.styleable.DiamondBackView_dbv_show_mode, CUT_BOTTOM);
        diamondColor = optColor(typedArray, R.styleable.DiamondBackView_dbv_color, 0xffF5F8FC);
        frameColor = optColor(typedArray, R.styleable.DiamondBackView_dbv_frame_color, 0xffF5F8FC);
        isDrawFrame = optBoolean(typedArray, R.styleable.DiamondBackView_dbv_is_have_frame, false);
        frameWidth = optDimension(typedArray, R.styleable.DiamondBackView_dbv_frame_width, 2f);
        if (typedArray != null) {
            typedArray.recycle();
        }

        diamondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        diamondPaint.setColor(diamondColor);

        framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        framePaint.setColor(frameColor);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(frameWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (showMode) {
            case CUT_BOTTOM:
                drawCutBottom(canvas);
                break;
            case FIT_Y:
                drawFitY(canvas);
                break;
            case GAP_TOP:
            case GAP_BOTTOM:
            case GAP_BOTTOM_TOP:
                drawMargin(canvas);
                break;
        }
    }

    /**
     * 计算需要的数值
     *
     * @param viewWidth  view的宽
     * @param viewHeight view的高
     */
    private void calculateValue(int viewWidth, int viewHeight) {
        maxWidth = viewWidth / horizontalCount;
        if (showMode == CUT_BOTTOM) {
            verticalCount = viewHeight / maxWidth + 1;
        } else if (showMode == FIT_Y) {
            maxHeight = viewHeight / verticalCount;
        } else if (showMode == GAP_TOP) {
            verticalCount = viewHeight / maxWidth;
            topMargin = viewHeight - maxWidth * verticalCount;
        } else if (showMode == GAP_BOTTOM) {
            verticalCount = viewHeight / maxWidth;
            topMargin = 0;
        } else if (showMode == GAP_BOTTOM_TOP) {
            verticalCount = viewHeight / maxWidth;
            topMargin = (viewHeight - maxWidth * verticalCount) / 2;
        }
    }

    /**
     * 斜的正方形  底部会被截掉
     *
     * @param canvas
     */
    private void drawCutBottom(Canvas canvas) {
        for (int j = 0; j < verticalCount; j++) {
            for (int i = 0; i < horizontalCount; i++) {
                path.reset();
                path.moveTo(maxWidth / 2 + i * maxWidth, j * maxWidth);
                path.lineTo(i * maxWidth, maxWidth / 2 + j * maxWidth);
                path.lineTo(maxWidth / 2 + i * maxWidth, (j + 1) * maxWidth);
                path.lineTo((i + 1) * maxWidth, maxWidth / 2 + j * maxWidth);
                path.close();
                canvas.drawPath(path, diamondPaint);
                if (isDrawFrame)
                    canvas.drawPath(path, framePaint);
            }
        }
    }

    /**
     * 根据设置的列的个数 画满，不截掉
     *
     * @param canvas
     */
    private void drawFitY(Canvas canvas) {
        for (int j = 0; j < verticalCount; j++) {
            for (int i = 0; i < horizontalCount; i++) {
                path.reset();
                path.moveTo(maxWidth / 2 + i * maxWidth, j * maxHeight);
                path.lineTo(i * maxWidth, maxHeight / 2 + j * maxHeight);
                path.lineTo(maxWidth / 2 + i * maxWidth, (j + 1) * maxHeight);
                path.lineTo((i + 1) * maxWidth, maxHeight / 2 + j * maxHeight);
                path.close();
                canvas.drawPath(path, diamondPaint);
                if (isDrawFrame)
                    canvas.drawPath(path, framePaint);
            }
        }
    }

    /**
     * 斜的正方形 上留白或者底留白或者上下都留白
     *
     * @param canvas
     */
    private void drawMargin(Canvas canvas) {
        for (int j = 0; j < verticalCount; j++) {
            for (int i = 0; i < horizontalCount; i++) {
                path.reset();
                path.moveTo(maxWidth / 2 + i * maxWidth, topMargin + j * maxWidth);
                path.lineTo(i * maxWidth, topMargin + maxWidth / 2f + j * maxWidth);
                path.lineTo(maxWidth / 2 + i * maxWidth, topMargin + (j + 1) * maxWidth);
                path.lineTo((i + 1) * maxWidth, topMargin + maxWidth / 2f + j * maxWidth);
                path.close();
                canvas.drawPath(path, diamondPaint);
                if (isDrawFrame)
                    canvas.drawPath(path, framePaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        viewWidth = width;
        viewHeight = height;
        calculateValue(viewWidth, viewHeight);
    }

    public DiamondBackView setDiamondColor(int diamondColor) {
        this.diamondColor = diamondColor;
        return this;
    }

    public DiamondBackView setFrameColor(int frameColor) {
        this.frameColor = frameColor;
        return this;
    }

    public DiamondBackView setHorizontalCount(int horizontalCount) {
        this.horizontalCount = horizontalCount;
        return this;
    }

    public DiamondBackView setVerticalCount(int verticalCount) {
        this.verticalCount = verticalCount;
        return this;
    }

    public DiamondBackView setDrawFrame(boolean drawFrame) {
        isDrawFrame = drawFrame;
        return this;
    }

    public DiamondBackView setFrameWidth(float frameWidth) {
        this.frameWidth = frameWidth;
        return this;
    }

    public DiamondBackView setShowMode(int showMode) {
        this.showMode = showMode;
        return this;
    }

    public void updateView() {
        calculateValue(viewWidth, viewHeight);
        postInvalidate();
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

    private static boolean optBoolean(TypedArray typedArray, int index, boolean def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getBoolean(index, def);
    }
}
