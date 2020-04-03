package com.app.test.paint;

import android.content.Context;
import android.content.res.TypedArray;
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
 * Describe:线上圆点变颜色
 */
public class CircleLineLayout extends FrameLayout {
    private static final String TAG = "CircleLineLayout";
    private static final int DEFAULT_DURING = 300;
    private int during = DEFAULT_DURING;
    private float strokeWidth;
    private float strokeRadius;
    private int strokeColor;

    private int lineColor;
    private float lineWidth;
    private float ballSpace;
    private float ballRadius;
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private List<Point> pointList = new CopyOnWriteArrayList<>();
    private Point point;
    private long next = 0;
    private static final int[] defaultColors = new int[]{
            Color.parseColor("#f63a0c")
            , Color.parseColor("#46de4d")
            , Color.parseColor("#5d8dd4")
            , Color.parseColor("#f7b90c")};
    private List<Integer> colorList = new CopyOnWriteArrayList<>();


    public CircleLineLayout(Context context) {
        this(context, null);
    }

    public CircleLineLayout(Context context, AttributeSet paramAttributeSet) {
        this(context, paramAttributeSet, 0);
    }

    public CircleLineLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet);
    }

    private void init(Context paramContext, AttributeSet paramAttributeSet) {
        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CircleLineLayout);
        during = typedArray.getInt(R.styleable.CircleLineLayout_mcll_during, during);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_stroke_width, DensityUtil.dp2px(10));
        strokeColor = typedArray.getColor(R.styleable.CircleLineLayout_mcll_stroke_color, Color.TRANSPARENT);
        strokeRadius = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_stroke_radius, DensityUtil.dp2px(13));

        lineColor = typedArray.getColor(R.styleable.CircleLineLayout_mcll_line_color, Color.parseColor("#fd7813"));
        lineWidth = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_line_width, DensityUtil.dp2px(2));

        ballSpace = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_ball_space, DensityUtil.dp2px(15));
        ballRadius = typedArray.getDimensionPixelSize(R.styleable.CircleLineLayout_mcll_ball_radius, DensityUtil.dp2px(3));

        typedArray.recycle();

        if (ballRadius * 2 > strokeWidth) {
            ballRadius = strokeWidth / 2;
        }
        if (during > DEFAULT_DURING) {
            during = DEFAULT_DURING;
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
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

    private void drawBallStroke(Canvas canvas) {
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        canvas.drawPath(path, paint);
    }

    private void drawBall(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        if (Utils.isEmpty(colorList)) {
            return;
        }
        int n = colorList.size();
        if (next >= Long.MAX_VALUE / 2) {
            next = 0;
        }
        ++next;
        for (int i = 0; i < pointList.size(); i++) {
            point = pointList.get(i);
            int color = Math.abs((int) ((i + next) % n));
            paint.setColor(colorList.get(color));
            canvas.drawCircle(point.x, point.y, ballRadius, paint);
        }
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
        drawBallStroke(canvas);
        drawBall(canvas);
        cycle();
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
        return ballSpace;
    }

    private void cycle() {
        removeCallbacks(runnable);
        postDelayed(runnable, during);
    }


}