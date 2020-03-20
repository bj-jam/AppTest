package com.app.test.showimage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
public class GameOpenDoorView extends RelativeLayout {
    private ImageView mLeft;
    private ImageView mRight;
    private IGameOpenDoorListen gameOpenDoorListen;
    private ValueAnimator closeAnimator;
    private ValueAnimator openAnimator;

    public GameOpenDoorView(Context context) {
        super(context);
        initView();
    }

    public GameOpenDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameOpenDoorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        View.inflate(getContext(), R.layout.view_game_open_door_anim, this);
        mLeft = findViewById(R.id.iv_left);
        mRight = findViewById(R.id.iv_right);
        setVisibility(INVISIBLE);
        setClickable(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setGameOpenDoorListen(IGameOpenDoorListen gameOpenDoorListen) {
        this.gameOpenDoorListen = gameOpenDoorListen;
    }

    private int width;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
    }

    public void startCloseAnim() {
        stopAnim();
        setVisibility(VISIBLE);
        setClickable(true);
        if (closeAnimator == null) {
            closeAnimator = new ValueAnimator();
            closeAnimator.setIntValues(-width, 0);
            closeAnimator.setDuration(1000);
            closeAnimator.setInterpolator(new LinearInterpolator());
            closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int pointX = (int) animation.getAnimatedValue();
                    mLeft.setX(pointX);
                    mRight.setX(-pointX);
                    if (pointX == 0) {
                        startOpenAnim();
                    }
                }
            });
        }

        closeAnimator.start();
        if (gameOpenDoorListen != null)
            gameOpenDoorListen.closeAnimStart();
    }

    private void startOpenAnim() {
        setVisibility(VISIBLE);
        if (openAnimator == null) {
            openAnimator = new ValueAnimator();
            openAnimator.setIntValues(0, width);
            openAnimator.setDuration(1000);
            openAnimator.setInterpolator(new LinearInterpolator());
            openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int pointX = (int) animation.getAnimatedValue();
                    mLeft.setX(-pointX);
                    mRight.setX(pointX);
                    if (pointX == width) {
                        setVisibility(GONE);
                        if (gameOpenDoorListen != null) {
                            gameOpenDoorListen.openAnimEnd();
                        }
                    }
                }
            });
        }

        openAnimator.start();
        if (gameOpenDoorListen != null) {
            gameOpenDoorListen.closeAnimEnd();
            gameOpenDoorListen.openAnimStart();
        }
    }


    private void stopAnim() {
        if (openAnimator != null && openAnimator.isRunning()) {
            openAnimator.cancel();
        }
        if (closeAnimator != null && closeAnimator.isRunning()) {
            closeAnimator.cancel();
        }
    }


//    public void startOpenAnim() {
//        mRight.animate().translationX(width).setDuration(600).start();
//        mLeft.animate().translationX(-width).setDuration(600).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.openAnimStart();
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                setVisibility(GONE);
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.openAnimEnd();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        }).start();
//        TranslateAnimation animation = leftHide(600);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.openAnimStart();
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.openAnimEnd();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mLeft.startAnimation(animation);
//        TranslateAnimation animation1 = rightHide(600);
//        mRight.startAnimation(animation1);
//        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(mLeft, "translationX", 0, -width);
//        ObjectAnimator rightAnim = ObjectAnimator.ofFloat(mRight, "translationX", 0, width);
//        AnimatorSet set = new AnimatorSet();
//        set.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.openAnimStart();
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.openAnimEnd();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        set.setDuration(3 * 1000);
//        set.playTogether(leftAnim, rightAnim);
//        set.start();
//    }

//    public TranslateAnimation leftHide(long time) {
//        TranslateAnimation hideX = new TranslateAnimation(
//                //X轴初始位置
//                Animation.RELATIVE_TO_SELF, -0.2f,
//                //X轴移动的结束位置
//                Animation.RELATIVE_TO_SELF, -1f,
//                //y轴开始位置
//                Animation.RELATIVE_TO_SELF, 0f,
//                //y轴移动后的结束位置
//                Animation.RELATIVE_TO_SELF, 0f);
//        //200秒完成动画
//        hideX.setDuration(time);
//        hideX.setFillAfter(true);
//        return hideX;
//    }

//    public TranslateAnimation rightHide(long time) {
//        TranslateAnimation hideX = new TranslateAnimation(
//                //X轴初始位置
//                Animation.RELATIVE_TO_SELF, 0.2f,
//                //X轴移动的结束位置
//                Animation.RELATIVE_TO_SELF, 1f,
//                //y轴开始位置
//                Animation.RELATIVE_TO_SELF, 0f,
//                //y轴移动后的结束位置
//                Animation.RELATIVE_TO_SELF, 0f);
//        //200秒完成动画
//        hideX.setDuration(time);
//        hideX.setFillAfter(true);
//        return hideX;
//    }

//    public TranslateAnimation leftShow(long time) {
//        TranslateAnimation showX = new TranslateAnimation(
//                //X轴初始位置
//                Animation.RELATIVE_TO_SELF, -1f,
//                //X轴移动的结束位置
//                Animation.RELATIVE_TO_SELF, -0.2f,
//                //y轴开始位置
//                Animation.RELATIVE_TO_SELF, 0f,
//                //y轴移动后的结束位置
//                Animation.RELATIVE_TO_SELF, 0f);
//        //200秒完成动画
//        showX.setDuration(time);
//        showX.setFillAfter(true);
//        return showX;
//    }

//    public TranslateAnimation rightShow(long time) {
//        TranslateAnimation showX = new TranslateAnimation(
//                //X轴初始位置
//                Animation.RELATIVE_TO_SELF, 1f,
//                //X轴移动的结束位置
//                Animation.RELATIVE_TO_SELF, 0.2f,
//                //y轴开始位置
//                Animation.RELATIVE_TO_SELF, 0f,
//                //y轴移动后的结束位置
//                Animation.RELATIVE_TO_SELF, 0f);
//        //200秒完成动画
//        showX.setDuration(time);
//        showX.setFillAfter(true);
//        return showX;
//    }


//    public void startCloseAnim() {
//        setVisibility(View.VISIBLE);
////        setClickable(true);
//        mRight.setX(width);
//        mLeft.setX(-width);
//        mRight.animate().translationX(0).setDuration(600).start();
//        mLeft.animate().translationX(0).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.closeAnimStart();
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.closeAnimEnd();
//                startOpenAnim();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        }).setDuration(600).start();
//        TranslateAnimation animation = leftShow(600);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.closeAnimStart();
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.closeAnimEnd();
//                startOpenAnim();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mLeft.startAnimation(animation);
//        TranslateAnimation animation1 = rightShow(600);
//        mRight.startAnimation(animation1);

//        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(mLeft, "translationX", -width, 0);
//        ObjectAnimator rightAnim = ObjectAnimator.ofFloat(mRight, "translationX", width, 0);
//        AnimatorSet set = new AnimatorSet();
//        set.setDuration(3 * 1000);
//        set.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.closeAnimStart();
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (gameOpenDoorListen != null)
//                    gameOpenDoorListen.closeAnimEnd();
//                startOpenAnim();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        set.playTogether(leftAnim, rightAnim);
//        set.start();
//    }


    public interface IGameOpenDoorListen {
        void openAnimStart();

        void openAnimEnd();

        void closeAnimStart();

        void closeAnimEnd();


    }
}
