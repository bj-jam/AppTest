package com.app.test.paint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.app.test.R;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:线条闪烁
 */
public class LineTwinkleLayout extends FrameLayout {
    private static final int DURING = 400;
    //边框宽度
    private float strokeW;
    //边框圆角
    private float strokeRadius;
    //边框颜色
    private int strokeColor;
    private int during = DURING;

    private float lineSpace;

    //内边框
    private float insideStrokeW;
    private float insideRadius;
    private int insideColor;
    private int insideShadowColor;
    //外边框
    private float outsideStrokeW;
    private float outsideRadius;
    private int outsideColor;
    private int outsideShadowColor;

    private Paint paint;
    private Paint outsidePaint;
    private Paint insidePaint;
    private Paint shadowPaint;
    private Path path;
    private long next = 0;

    private static final int[] defaultColors = new int[]{Color.parseColor("#ff108c"),
            Color.parseColor("#ff108c")};

    private static final int[] defaultShadowColors = new int[]{Color.parseColor("#ffd2d2")
            , Color.parseColor("#ff108c")};
    private List<Integer> colorList = new CopyOnWriteArrayList<>();
    private List<Integer> shadowColorList = new CopyOnWriteArrayList<>();

    private int outsideAlpha = 0;
    private int insideAlpha = 0;

    private float outsideBlurMaskRatio = 0.33f;
    //外面的实线占宽度比率
    private float outsideShadowWidthRatio = 0.25f;
    private float insideBlurMaskRatio = 0.25f;
    //里面的实线占宽度比率
    private float insideShadowWidthRatio = 0.3f;

    private ValueAnimator outsideAnim;
    private ValueAnimator insideAnim;
    private ScheduledExecutorService executor;

    private int insideBgColor = Color.parseColor("#252B30");
    private float insideBgRadius = -1.0f;
    private float insideBgWidth = -1;

    private boolean isShow = true;

    public LineTwinkleLayout(Context context) {
        this(context, null);
    }

    public LineTwinkleLayout(Context context, AttributeSet paramAttributeSet) {
        this(context, paramAttributeSet, 0);
    }

    public LineTwinkleLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet);
    }

    private void init(Context paramContext, AttributeSet paramAttributeSet) {
        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LineTwinkleLayout);
        during = typedArray.getInt(R.styleable.LineTwinkleLayout_dll_during, during);

        strokeW = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_stroke_width, DensityUtil.dp2px(21));
        strokeColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_stroke_color, Color.TRANSPARENT);
        strokeRadius = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_stroke_radius, DensityUtil.dp2px(16));

        lineSpace = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_line_space, DensityUtil.dp2px(2));

        outsideStrokeW = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_out_stroke_width, DensityUtil.dp2px(6));
        outsideRadius = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_out_radius, DensityUtil.dp2px(22));
        outsideColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_out_color, Color.parseColor("#ff108c"));
        outsideShadowColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_out_shadow_color, Color.parseColor("#ffd2d2"));
        outsideShadowWidthRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_out_shadow_width_ratio, 0.5f);
        outsideBlurMaskRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_out_blur_mask_ratio, 1.5f);

        insideStrokeW = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_inner_stroke_width, DensityUtil.dp2px(6));
        insideRadius = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_inner_radius, DensityUtil.dp2px(18));
        insideColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_inner_color, Color.parseColor("#ff108c"));
        insideShadowColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_inner_shadow_color, Color.parseColor("#ff108c"));
        insideShadowWidthRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_inner_shadow_width_ratio, 0.5f);
        insideBlurMaskRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_inner_blur_mask_ratio, 2f);

        if (outsideStrokeW > strokeW) {
            outsideStrokeW = strokeW / 5;
        }
        if (insideStrokeW > strokeW) {
            insideStrokeW = strokeW / 5;
        }
        if (lineSpace > strokeW) {
            lineSpace = strokeW / 5;
        }
        if (during > DURING) {
            during = DURING;
        }
        if (outsideBlurMaskRatio <= 0) {
            outsideBlurMaskRatio = 0.8f;
        }
        if (insideBlurMaskRatio <= 0) {
            insideBlurMaskRatio = 0.8f;
        }

        typedArray.recycle();

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        outsidePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        outsidePaint.setStyle(Paint.Style.STROKE);

        insidePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        insidePaint.setStyle(Paint.Style.STROKE);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        shadowPaint.setStyle(Paint.Style.STROKE);

        path = new Path();
        colorList = getColorList(defaultColors, defaultColors);
        shadowColorList = getColorList(defaultShadowColors, defaultShadowColors);
    }

    private void startAnim() {
        startInsideAnim();
        startOutsideAnim();
        stopTimer();
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateNext();
            }

            private void updateNext() {
                if (next >= Long.MAX_VALUE / 2) {
                    next = 0;
                }
                next += 2;
            }

        }, during * 2, during * 2, TimeUnit.MILLISECONDS);
    }

    private void startOutsideAnim() {
        if (Utils.isEmpty(outsideAnim)) {
            outsideAnim = ValueAnimator.ofInt(0, 255);
        }
        outsideAnim.cancel();
        outsideAnim.setRepeatMode(ValueAnimator.REVERSE);
        outsideAnim.setRepeatCount(-1);
        outsideAnim.setInterpolator(new DecelerateInterpolator());
        outsideAnim.setDuration((int) (during * 1f));
        outsideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isShow) {
                    outsideAlpha = (int) animation.getAnimatedValue();
                } else {
                    outsideAlpha = 0;
                }
                postInvalidate();
            }
        });
        outsideAnim.start();
    }

    private void startInsideAnim() {
        if (Utils.isEmpty(insideAnim)) {
            insideAnim = ValueAnimator.ofInt(0, 255);
        }
        insideAnim.cancel();
        insideAnim.setRepeatMode(ValueAnimator.REVERSE);
        insideAnim.setInterpolator(myInterpolator);
        insideAnim.setRepeatCount(-1);
        insideAnim.setDuration((int) (during * 1f));
        insideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isShow) {
                    insideAlpha = (int) animation.getAnimatedValue();
                } else {
                    insideAlpha = 0;
                }
                postInvalidate();
            }
        });
        insideAnim.start();
    }

    private List<Integer> getColorList(int[] colors, int[] defaultColors) {
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
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeW);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        drawDoubleLine(canvas);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        updateDraw(canvas);
    }

    private void drawDoubleLine(Canvas canvas) {
        drawInnerBg(canvas);
        drawOutLine(canvas);
        drawInnerLine(canvas);
    }

    private void drawInnerBg(Canvas canvas) {
        if (insideBgRadius < 0) {
            insideBgRadius = insideRadius;
        }
        int width = getWidth();
        int height = getHeight();
        if (insideBgWidth < 0) {
            insideBgWidth = strokeW - outsideStrokeW - lineSpace;
        }
        float gbCenter = outsideStrokeW + lineSpace + insideBgWidth / 2;
        RectF bgRect = new RectF(gbCenter, gbCenter, width - gbCenter, height - gbCenter);
        shadowPaint.setColor(insideBgColor);
        shadowPaint.setStrokeWidth(insideBgWidth);
        canvas.drawRoundRect(bgRect, insideBgRadius, insideBgRadius, shadowPaint);
    }

    private void drawOutLine(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float widthSpace = outsideStrokeW / 2;
        RectF out = new RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace);
        outsidePaint.setColor(getFitColor(colorList, next, outsideColor));
        if (outsideStrokeW * outsideBlurMaskRatio > 0) {
            outsidePaint.setMaskFilter(new BlurMaskFilter(outsideStrokeW * outsideBlurMaskRatio, BlurMaskFilter.Blur.INNER));
        }
        outsidePaint.setStrokeWidth(outsideStrokeW);
        outsidePaint.setAlpha(outsideAlpha);
        canvas.drawRoundRect(out, outsideRadius, outsideRadius, outsidePaint);

        shadowPaint.setColor(getFitColor(shadowColorList, next, outsideShadowColor));
        shadowPaint.setAlpha(outsideAlpha);
        shadowPaint.setStrokeWidth(outsideStrokeW * outsideShadowWidthRatio);
        canvas.drawRoundRect(new RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace)
                , outsideRadius, outsideRadius, shadowPaint);
    }

    private void drawInnerLine(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float widthSpace = lineSpace + outsideStrokeW + insideStrokeW / 2;
        long index = next + 1;
        RectF inner = new RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace);
        insidePaint.setColor(getFitColor(colorList, index, insideColor));
        if (insideStrokeW * insideBlurMaskRatio > 0) {
            insidePaint.setMaskFilter(new BlurMaskFilter(insideStrokeW * insideBlurMaskRatio, BlurMaskFilter.Blur.INNER));
        }
        insidePaint.setStrokeWidth(insideStrokeW);
        insidePaint.setAlpha(insideAlpha);
        canvas.drawRoundRect(inner, insideRadius, insideRadius, insidePaint);

        int fitColor = getFitColor(shadowColorList, index, insideShadowColor);
        shadowPaint.setColor(fitColor);
        shadowPaint.setStrokeWidth(insideStrokeW * insideShadowWidthRatio);
        shadowPaint.setAlpha(insideAlpha);
        canvas.drawRoundRect(new RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace)
                , insideRadius, insideRadius, shadowPaint);

    }

    /**
     * colorList,next,outColor
     *
     * @param next
     * @return
     */
    private int getFitColor(List<Integer> colorList, long next, int defaultColor) {
        if (Utils.isEmpty(colorList) || colorList.size() == 1) {
            return defaultColor;
        }
        int n = colorList.size();
        int innerColorIndex = Math.abs((int) ((next) % n));
        innerColorIndex = Math.max(0, innerColorIndex);
        return colorList.get(innerColorIndex);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((w != oldw) || (h != oldh)) {
            initPath();
            startAnim();
        }
    }

    private void initPath() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        path.reset();
        float v = strokeW / 2;
        RectF rectF = new RectF(v, v, width - v, height - v);
        path.addRoundRect(rectF, strokeRadius, strokeRadius, Path.Direction.CW);
    }


    Interpolator myInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float input) {
            if (input < 0.4) {
                return 0;
            }
            return (float) Math.pow(input, 2);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    public void stopAnim() {
        if (!Utils.isEmpty(outsideAnim)) {
            outsideAnim.cancel();
            outsideAnim = null;
        }
        if (!Utils.isEmpty(insideAnim)) {
            insideAnim.cancel();
            insideAnim = null;
        }
        stopTimer();
    }

    private void stopTimer() {
        try {
            if (Utils.isEmpty(executor)) {
                return;
            }
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
            executor = null;
        } catch (Exception e) {
        }
    }

}