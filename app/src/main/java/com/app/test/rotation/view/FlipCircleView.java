package com.app.test.rotation.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.app.test.R;

/**
 * Created by jam on 2018/11/6.
 */

public class FlipCircleView extends FrameLayout {
    private BorderImageView ivFront;
    private BorderImageView ivBack;

    private AnimatorSet showAnimator;
    private AnimatorSet hideAnimator;
    private OnSwitchListener onSwitchListener;
    private boolean isFront = true;

    public FlipCircleView(Context context) {
        super(context);
        init();
    }

    public FlipCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        ivFront = new BorderImageView(getContext());
        ivBack = new BorderImageView(getContext());

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(ivBack, params);
        addView(ivFront, params);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAnimator.setTarget(isFront ? ivFront : ivBack);
                showAnimator.setTarget(isFront ? ivBack : ivFront);
                hideAnimator.start();
                showAnimator.start();
                isFront = !isFront;
            }
        });

        initAnimator();
        setCameraDistance();
    }

    private void initAnimator() {
        showAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.show_flip);
        hideAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.hide_flip);
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setEnabled(true);
                if (onSwitchListener != null) {
                    onSwitchListener.onSwitch(isFront);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        showAnimator.addListener(listener);
        hideAnimator.addListener(listener);
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        ivFront.setCameraDistance(scale);
        ivBack.setCameraDistance(scale);
    }

    public void reset() {
        if (ivFront != null) {
            ivFront.setAlpha(1f);
            ivFront.setRotationY(0f);
        }
        if (ivBack != null) {
            ivBack.setAlpha(0f);
            ivBack.setRotationY(180f);
        }
        isFront = true;
    }

    public boolean isFront() {
        return isFront;
    }

    public void enableSwitch(boolean enable) {
        setEnabled(enable);
        if (ivFront != null) {
            ivFront.setShowText(enable);
        }
        if (ivBack != null) {
            ivBack.setShowText(enable);
        }
    }

    public void setFrontImage(String url) {
        if (ivFront != null) {
            ivFront.setSize(getMeasuredWidth());
            Glide.with(getContext()).load(url).asBitmap().placeholder(R.drawable.default_head).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ivFront.setSource(resource);
                }
            });
        }
    }

    public void setFrontImage(int resId) {
        if (ivFront != null) {
            ivFront.setSize(getMeasuredWidth());
            Glide.with(getContext()).load(resId).asBitmap().placeholder(R.drawable.default_head).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ivFront.setSource(resource);
                }
            });
        }
    }

    public void setBackImage(String url) {
        if (ivBack != null) {
            ivBack.setSize(getMeasuredWidth());
            Glide.with(getContext()).load(url).asBitmap().placeholder(R.drawable.default_head).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ivBack.setSource(resource);
                }
            });
        }
    }

    public void setBackImage(int resId) {
        if (ivBack != null) {
            ivBack.setSize(getMeasuredWidth());
            Glide.with(getContext()).load(resId).asBitmap().placeholder(R.drawable.default_head).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ivBack.setSource(resource);
                }
            });
        }
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public interface OnSwitchListener {
        void onSwitch(boolean isFront);
    }
}
