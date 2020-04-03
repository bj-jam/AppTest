package com.app.test.paint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.app.test.R;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:双线中间圆点移动
 */

public class SpotMoveLayout extends FrameLayout {
    private static final String TAG = "SpotMoveLayout";
    public static final int DEFAULT_DURING = 300;
    private float strokeWidth;

    private float strokeRadius;
    private float innerStrokeRadius;
    private int strokeColor;

    private int during = DEFAULT_DURING;
    private int lineColor;
    private float lineWidth;
    private float ballSpace;
    private float ballRadius;

    private float ballInnerRadius;
    private int ballInnerColor;
    private boolean isBallInnerColorFellowOut = false;

    private Paint paint;
    private Paint ballPaint;
    private Paint ballInnerPaint;
    private Path path;
    private PathMeasure pathMeasure;
    private List<Point> pointList = new CopyOnWriteArrayList<>();
    private Point point;
    private long next = 0;
    private static final int[] defaultColors = new int[]{
            Color.parseColor("#ff0012")
            , Color.parseColor("#0060ff")};
    private List<Integer> colorList = new CopyOnWriteArrayList<>();


    public SpotMoveLayout(Context context) {
        this(context, null);
    }

    public SpotMoveLayout(Context context, AttributeSet paramAttributeSet) {
        this(context, paramAttributeSet, 0);
    }

    public SpotMoveLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet);
    }

    private void init(Context paramContext, AttributeSet paramAttributeSet) {
        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SpotMoveLayout);
        during = typedArray.getInt(R.styleable.SpotMoveLayout_mdll_during, DEFAULT_DURING);

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_stroke_width, DensityUtil.dp2px(10));
        strokeColor = typedArray.getColor(R.styleable.SpotMoveLayout_mdll_stroke_color, Color.TRANSPARENT);
        strokeRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_stroke_radius, DensityUtil.dp2px(16));
        innerStrokeRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_stroke_radius, DensityUtil.dp2px(8));

        lineColor = typedArray.getColor(R.styleable.SpotMoveLayout_mdll_line_color, Color.parseColor("#fb7812"));
        lineWidth = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_line_width, DensityUtil.dp2px(1));

        ballSpace = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_ball_space, DensityUtil.dp2px(17));
        ballRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_ball_radius, DensityUtil.dp2px(4));

        ballInnerRadius = typedArray.getDimensionPixelSize(R.styleable.SpotMoveLayout_mdll_ball_inner_radius, DensityUtil.dp2px(2));
        ballInnerColor = typedArray.getColor(R.styleable.SpotMoveLayout_mdll_ball_inner_color, Color.WHITE);
        isBallInnerColorFellowOut = typedArray.getBoolean(R.styleable.SpotMoveLayout_mdll_ball_inner_fellow_out, isBallInnerColorFellowOut);

        typedArray.recycle();
        ballSpace /= 2;
        if (innerStrokeRadius > strokeRadius) {
            innerStrokeRadius = strokeRadius / 2;
        }
        if (ballRadius * 2 > strokeWidth) {
            ballRadius = strokeWidth / 2;
        }
        if (ballInnerRadius > ballRadius) {
            ballInnerRadius = ballRadius / 2;
        }
        if (during > DEFAULT_DURING) {
            during = DEFAULT_DURING;
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        ballInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        path = new Path();
        colorList = getColorList(defaultColors);
    }

    private List<Integer> getColorList(int[] colors) {
        if (Utils.isEmpty(colors)) {
            colors = defaultColors;
        }
        List<Integer> colorList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            colorList.add(colors[i]);
        }
        return colorList;
    }


    private void updateDraw(Canvas canvas) {
        if (next >= Long.MAX_VALUE / 2) {
            next = 0;
        }
        ++next;
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        if (Utils.isEmpty(pointList)) {
            return;
        }
        int n = colorList.size();
        int colorIndex = Math.abs((int) ((next) % n));
        colorIndex = Math.max(0, colorIndex);
        int color = colorList.get(colorIndex);
        drawBallStroke(canvas, color);
        drawBall(canvas, color);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        updateDraw(canvas);
        cycle();
    }

    private void cycle() {
        removeCallbacks(runnable);
        postDelayed(runnable, during);
    }

    private void drawBallStroke(Canvas canvas, int color) {
        lineColor = color;
        int width = getWidth();
        int height = getHeight();

        float outStrokeWidthSpace = lineWidth / 2;
        RectF out = new RectF(outStrokeWidthSpace, outStrokeWidthSpace, width - outStrokeWidthSpace, height - outStrokeWidthSpace);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        canvas.drawRoundRect(out, strokeRadius, strokeRadius, paint);


        float innerStrokeWidthSpace = strokeWidth - lineWidth / 2 + 1;
        RectF inner = new RectF(innerStrokeWidthSpace, innerStrokeWidthSpace, width - innerStrokeWidthSpace, height - innerStrokeWidthSpace);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        canvas.drawRoundRect(inner, innerStrokeRadius, innerStrokeRadius, paint);
    }

    private void drawBall(Canvas canvas, int color) {
        if (Utils.isEmpty(colorList)) {
            return;
        }
        for (int i = (next % 2 == 0 ? 0 : 1); i < pointList.size(); i += 2) {
            point = pointList.get(i);
            ballPaint.setColor(color);
            ballPaint.setStyle(Paint.Style.FILL);
            ballPaint.setMaskFilter(new BlurMaskFilter(ballRadius / 4, BlurMaskFilter.Blur.NORMAL));
            canvas.drawCircle(point.x, point.y, ballRadius, ballPaint);

            if (!isBallInnerColorFellowOut) {
                ballInnerPaint.setColor(ballInnerColor);
                ballInnerPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(point.x, point.y, ballInnerRadius, ballInnerPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((w != oldw) || (h != oldh)) {
            initPath();
        }
    }

    private void initPath() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        path.reset();
        pointList.clear();
        float ballStrokeWidthSpace = strokeWidth / 2;
        RectF localRectf = new RectF(ballStrokeWidthSpace, ballStrokeWidthSpace, width - ballStrokeWidthSpace, height - ballStrokeWidthSpace);
        path.addRoundRect(localRectf, strokeRadius, strokeRadius, Path.Direction.CW);
        pathMeasure = new PathMeasure(path, false);
        float pathLength = pathMeasure.getLength();

        if (ballSpace <= 0) {
            ballSpace = pathLength * 1.0f / (pathLength / (ballRadius * 2));
        }
        float tempBallSpace = getAvaBallSpace();
        float item = tempBallSpace + 2 * ballRadius;
        int count = (int) (pathLength / item + 0.5f);
        tempBallSpace = pathLength * 1.0f / count + 2 * ballRadius;

        float[] pos = new float[2];
        float distance = 0;
        while (distance <= pathLength) {
            if (pathMeasure.getPosTan(distance, pos, null)) {
                if (pathLength - distance >= tempBallSpace) {
                    pointList.add(new Point((int) pos[0], (int) pos[1]));
                }
            }
            distance += tempBallSpace;
        }
        Log.e(TAG, "pointList>>" + pointList);
    }

    private float getAvaBallSpace() {
        return (ballSpace - 2 * ballRadius) / 2;
    }


}