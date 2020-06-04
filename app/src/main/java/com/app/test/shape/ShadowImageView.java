package com.app.test.shape;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Desc: ShadowImageView  最好在API16以上使用
 */
public class ShadowImageView extends ImageView {

    private int mShadowRadius;

    // Animation
    private ObjectAnimator mRotateAnimator;
    private long mLastAnimationValue;

    public ShadowImageView(Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
//        final float density = getContext().getResources().getDisplayMetrics().density;
//        final int shadowXOffset = (int) (density * X_OFFSET);
//        final int shadowYOffset = (int) (density * Y_OFFSET);
//
//        mShadowRadius = (int) (density * SHADOW_RADIUS);
//
//        ShapeDrawable circle;
//        if (elevationSupported()) {
//            circle = new ShapeDrawable(new OvalShape());
//            ViewCompat.setElevation(this, SHADOW_ELEVATION * density);
//        } else {
//            OvalShape oval = new OvalShadow(mShadowRadius);
//            circle = new ShapeDrawable(oval);
//            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, circle.getPaint());
//            circle.getPaint().setShadowLayer(mShadowRadius, shadowXOffset, shadowYOffset, KEY_SHADOW_COLOR);
//            final int padding = mShadowRadius;
//            setPadding(padding + 10, padding + 10, padding + 10, padding + 10); // 设置图片与背景的边距
//        }
//        circle.getPaint().setAntiAlias(true); // 去锯齿
//        circle.getPaint().setColor(DEFAULT_BACKGROUND_COLOR);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            setBackground(circle); // 画背景圆
//        }

        mRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        mRotateAnimator.setDuration(44460); // 设置转速,越大越慢   // 设置持续时间
        mRotateAnimator.setInterpolator(new LinearInterpolator());
//        mRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);  // 设置循环播放

        // 设置动画监听
//        mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                // 监听动画执行的位置，以便下次开始时，从当前位置开始
//
//                // animation.getAnimatedValue()为flort类型
//                float currentValue = (float) animation.getAnimatedValue();
//            }
//        });
//        mRotateAnimator.start();
    }

    private boolean elevationSupported() {
        return Build.VERSION.SDK_INT >= 21;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!elevationSupported()) {
            setMeasuredDimension(getMeasuredWidth() + mShadowRadius * 2, getMeasuredHeight() + mShadowRadius * 2);
        }
    }

    /**
     * 停止动画 *
     */
    public void stopAnimation() {
        mLastAnimationValue = 0;
        clearAnimation();
        if (mRotateAnimator != null) {
            mRotateAnimator.end();
            mRotateAnimator = null;
        }
    }

    /**
     * 开始动画 *
     */
    public void startRotateAnimation() {
        if (mRotateAnimator != null) {
            mRotateAnimator.cancel();
            mRotateAnimator.start();
        }
    }

    public void cancelRotateAnimation() {
        if (mRotateAnimator != null) {
            mLastAnimationValue = 0;
            mRotateAnimator.cancel();
        }
    }

    public void pauseRotateAnimation() {
        if (mRotateAnimator != null) {
            mLastAnimationValue = mRotateAnimator.getCurrentPlayTime();
            mRotateAnimator.cancel();
            clearAnimation(); // 清除此ImageView身上的动画
        }
    }

    public void resumeRotateAnimation() {
        if (mRotateAnimator != null) {
            mRotateAnimator.start();
            mRotateAnimator.setCurrentPlayTime(mLastAnimationValue);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRotateAnimator != null) {
            // 控件被移除时，取消动画
            mRotateAnimator.cancel();
            // 清除此ImageView身上的动画
            clearAnimation();// 清除此ImageView身上的动画
            mRotateAnimator = null;
        }
    }

    /**
     * Draw oval shadow below ImageView under lollipop.
     */
//    private class OvalShadow extends OvalShape {
//        private RadialGradient mRadialGradient;
//        private Paint mShadowPaint;
//
//        OvalShadow(int shadowRadius) {
//            super();
//            mShadowPaint = new Paint();
//            mShadowRadius = shadowRadius;
//            updateRadialGradient((int) rect().width());
//        }
//
//        @Override
//        protected void onResize(float width, float height) {
//            super.onResize(width, height);
//            updateRadialGradient((int) width);
//        }
//
//        @Override
//        public void draw(Canvas canvas, Paint paint) {
//            final int viewWidth = ShadowImageView.this.getWidth();
//            final int viewHeight = ShadowImageView.this.getHeight();
//            canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, mShadowPaint);
//            canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2 - mShadowRadius, paint);
//        }
//
//        private void updateRadialGradient(int diameter) {
//            mRadialGradient = new RadialGradient(diameter / 2, diameter / 2,
//                    mShadowRadius, new int[]{FILL_SHADOW_COLOR, Color.TRANSPARENT},
//                    null, Shader.TileMode.CLAMP);
//            mShadowPaint.setShader(mRadialGradient);
//        }
//    }
}
