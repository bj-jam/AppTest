package com.app.test.path;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.app.test.base.App;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;

import java.util.ArrayList;
import java.util.List;

/***
 *
 */
public class GameOverHelper {
    public interface GameOverAnimListener {
        void onSuccess();

        void onError();
    }

    private static final String LAV_CONFIG_IMAGES = "box/images";
    private static final String LAV_CONFIG_JSON = "box/baoxiang.json";

    private static List<View> boxStartAnim(Activity activity, RecyclerView recyclerView, @IdRes int boxViewId, int... position) {
        List<View> viewList = new ArrayList<>();
        if (Utils.isEmpty(recyclerView) || Utils.isEmpty(position)) {
            return viewList;
        }
        int length = position.length;
        for (int i = 0; i < length; i++) {
            View childView = recyclerView.getChildAt(position[i]);
            if (Utils.isEmpty(childView)) {
                continue;
            }
            if (!(childView instanceof ViewGroup)) {
                continue;
            }
            View boxView = childView.findViewById(boxViewId);
            if (Utils.isEmpty(boxView)) {
                continue;
            }

            Rect rect = new Rect();
            boxView.getGlobalVisibleRect(rect);

            viewList.add(boxView);
            ViewGroup viewItem = (ViewGroup) childView;

            viewItem.removeView(boxView);


            addViewToRoot(activity, boxView);
        }

        return viewList;
    }

    private static void animError(GameOverAnimListener listener) {
        if (!Utils.isEmpty(listener)) {
            listener.onError();
        }
    }

    private static void animSuccess(GameOverAnimListener listener) {
        if (!Utils.isEmpty(listener)) {
            listener.onSuccess();
        }
    }

    public static void boxStartAnim(Activity activity, List<View> viewList, GameOverAnimListener listener) {
        if (Utils.isEmpty(viewList) || Utils.isEmpty(activity) || Utils.isEmpty(listener)) {
            animError(listener);
            return;
        }
        /*因为这个动画开发时间比较短，为防止意外情况，这里try catch处理*/
        try {
            int screenWidth = App.sWidth;
            int screenHeight = App.sHeight;

            int centerX = screenWidth / 2;
            int centerY = screenHeight / 2;
            int size = viewList.size();

            for (int i = 0; i < size; i++) {
                View view = viewList.get(i);
                addViewToRoot(activity, view);
                startMove(activity, view, view.getTranslationX(), view.getTranslationY(), centerX, centerY, i == (size - 1), listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
            animError(listener);
        }
    }

    private static void addViewToRoot(Activity activity, View view) {
        if (Utils.isEmpty(activity) || Utils.isEmpty(view)) {
            return;
        }
        FrameLayout rootView = (FrameLayout) activity.getWindow().getDecorView();
        rootView.addView(view);
    }

    private static void startMove(final Activity activity, final View view, final float startX, final float startY, final int endX, final int endY, final boolean isLastView, final GameOverAnimListener listener) {
        if (Utils.isEmpty(view)) {
            return;
        }
        int viewLeftTopX = endX - view.getMinimumWidth() / 2;
        if (startX < 0 || startY < 0 || viewLeftTopX < 0 || endY < 0) {
            return;
        }
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(viewLeftTopX, startY, viewLeftTopX, endY);

        final PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, length);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                float[] position = new float[2];
                pathMeasure.getPosTan(animatedValue, position, null);
                view.setTranslationX(position[0]);
                view.setTranslationY(position[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startHidden(view);
                if (isLastView) {
                    showBigBox(activity, endX, endY, listener);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private static void startHidden(View view) {
        if (Utils.isEmpty(view)) {
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        view.setAnimation(alphaAnimation);
        view.setVisibility(View.INVISIBLE);
    }

    public static void showBigBox(Activity activity, int endX, int endY, final GameOverAnimListener listener) {
        if (Utils.isEmpty(activity)) {
            return;
        }
        final FrameLayout rootView = (FrameLayout) activity.getWindow().getDecorView();
        final LottieAnimationView lav = new LottieAnimationView(activity);

        int dp100 = DensityUtil.dp2px(150);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp100);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        lav.setLayoutParams(layoutParams);
//        lav.setTranslationX(endX-dp100/2);
        lav.setTranslationY(endY - dp100 / 2);

        lav.useHardwareAcceleration(true);
        lav.setImageAssetsFolder(LAV_CONFIG_IMAGES);
        lav.setAnimation(LAV_CONFIG_JSON);
        lav.setRepeatCount(0);
        lav.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!Utils.isEmpty(lav)) {
                    lav.removeAllUpdateListeners();
                    lav.removeAllLottieOnCompositionLoadedListener();
                    lav.removeAllAnimatorListeners();
                    rootView.removeView(lav);
                }
                animSuccess(listener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        lav.playAnimation();

        rootView.addView(lav);
    }
}
