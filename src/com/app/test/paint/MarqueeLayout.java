package com.app.test.paint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.app.test.R;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:变颜色的线条移动
 */
public class MarqueeLayout extends FrameLayout {
    private static final String TAG = MarqueeLayout.class.getSimpleName();
    //圆角
    private float radius;
    //边框宽度
    private float strokeWidth;
    private LinearGradient linearGradient;
    private RectF rectF;
    private Matrix matrix;
    private Bitmap bitmap;
    private Paint paint;
    private PorterDuffXfermode porterDuffXfermode;

    private PathMeasure pathMeasure;
    private float pathLength;
    private float pathLength_4;
    private float step;
    private float tDx;
    private float tDy;

    private float startA;
    private float stopA;
    private Path dstA;

    private float startB;
    private float stopB;
    private Path dstB;

    private float startC;
    private float stopC;
    private Path dstC;

    private float startD;
    private float stopD;
    private Path dstD;

    private int width;
    private int height;
    private float percentStep;
    boolean isFill = false;
    private int[] colors = new int[]{Color.parseColor("#FF64A1")
            , Color.parseColor("#A643FF")
            , Color.parseColor("#64EBFF")
            , Color.parseColor("#FFFE39")
            , Color.parseColor("#FF9964")
    };

    public MarqueeLayout(Context context) {
        this(context, null);
    }

    public MarqueeLayout(Context paramContext, AttributeSet attrs) {
        this(paramContext, attrs, 0);
    }

    public MarqueeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeLayout);
        int tempStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.MarqueeLayout_ml_stroke_width, 20);
        int tempRadius = typedArray.getDimensionPixelSize(R.styleable.MarqueeLayout_ml_round_radius, 10);
        float tempPercentCircumference = typedArray.getFloat(R.styleable.MarqueeLayout_ml_percent_step, 0.01f);
        typedArray.recycle();
        percentStep = Math.max(0, tempPercentCircumference);
        matrix = new Matrix();
        radius = tempRadius;
        strokeWidth = tempStrokeWidth;

        initPaint();
    }

    private void initPaint() {
        if (Utils.isEmpty(paint)) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }
        paint.setStrokeWidth(strokeWidth);
        if (Utils.isEmpty(porterDuffXfermode)) {
            porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        }
    }

    private void updatePathMeasure(PathMeasure pathMeasure) {
        /**
         * startD:开始截取位置距离path起点的位置,这不是一个坐标值,是没有负数的
         * stopD:结束点距离path起点的位置,同理上,这个是小于等于path的总长度(pathmeasure.getLength())
         * dst:截取的图形成一个path对象,
         * startWidthMoveTo:表示起点位置是否使用moveTo()
         */
        pathMeasure.getSegment(startA, stopA, dstA, true);
        pathMeasure.getSegment(startB, stopB, dstB, true);
        pathMeasure.getSegment(startC, stopC, dstC, true);
        pathMeasure.getSegment(startD, stopD, dstD, true);
    }

    private void updateDst() {
        if (stopA >= pathLength) {
            stopB = pathLength;
            startB = startA;

            startA = 0;
            stopA = 0;
        }
        if (startB >= pathLength)
            startA += step;

        stopA += step;
        startB += step;


        startC += step;
        stopC = (startC + pathLength_4);
        if (stopC >= pathLength)
            stopD += step;

        if (startC >= pathLength) {
            stopD = 0;
            startD = 0;
            startC = 0;
            stopC = (startC + pathLength_4);
        }
    }

    private void updateTranslate() {
        tDx += step;
        tDy += step;
        matrix.setTranslate(tDx, tDy);
        linearGradient.setLocalMatrix(matrix);
    }

    private void updateCanvas(Canvas canvas) {
        if (Utils.isEmpty(bitmap)) {
            return;
        }
        int saveCount = canvas.saveLayer(0.0f, 0.0f, width, height, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(dstA, paint);
        canvas.drawPath(dstB, paint);
        canvas.drawPath(dstC, paint);
        canvas.drawPath(dstD, paint);
        paint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saveCount);
    }

    private void reset() {
        dstA.reset();
        dstB.reset();
        dstC.reset();
        dstD.reset();
        dstA.lineTo(0.0f, 0.0f);
        dstB.lineTo(0.0f, 0.0F);
        dstC.lineTo(0.0f, 0.0f);
        dstD.lineTo(0.0f, 0.0f);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (Utils.isEmptyAny(pathMeasure, rectF)) {
            return;
        }
        updatePathMeasure(pathMeasure);
        updateDst();
        updateTranslate();
        updateCanvas(canvas);
        reset();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPath();
    }


    private void initPath() {
        initPaint();
        int tempWidth = getWidth();
        int tempHeight = getHeight();
        if (tempWidth <= 0 || tempHeight <= 0) {
            return;
        }
        this.width = tempWidth;
        this.height = tempHeight;

        Path path = new Path();

        dstA = new Path();
        dstB = new Path();
        dstC = new Path();
        dstD = new Path();

        pathMeasure = new PathMeasure();

        rectF = new RectF(0.0f, 0.0f, width, height);
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        pathMeasure.setPath(path, true);
        pathLength = pathMeasure.getLength();
        pathLength_4 = (pathLength / 4);
        Log.e(TAG, "pathLength>>" + pathLength + "\nwidth>>" + this.width + "\nheight>>" + this.height);
        step = (percentStep * pathLength);

        if (!isFill) {
            startA = 0;
            stopA = (startA + pathLength / 8);

            startB = (pathLength - pathLength / 8);
            stopB = pathLength;

            startC = (pathLength / 2 - pathLength / 8);
            stopC = (startC + pathLength_4);

            startD = 0;
            stopD = 0;
            isFill = true;
        }

        linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, width, colors, new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1.0f}, Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
        bitmap = createBitmap(width, height);
    }

    private Bitmap createBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Path path = new Path();
        RectF rectF = new RectF(0.0f, 0.0f, width, height);
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#0000ff"));
        canvas.drawPath(path, paint);
        return bitmap;
    }

}