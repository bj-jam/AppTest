package com.app.test.showimage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.test.R;
import com.app.test.base.App;
import com.app.test.move.TanImageView;
import com.app.test.view.image.PhotoView;

public class ShowIamgeActivity extends Activity {
    private PhotoView mPhotoView;
    private GameOpenDoorView mGameOpenDoorView;
    private RelativeLayout mgv_move;
    private RelativeLayout rl_all;
    private GameMoveGoldView mGameMoveGoldView;
    ImageView iv_setting;

    private ImageView moveView;
    private ImageView button;
    private ImageView button1;
    private ImageView button2;
    private ImageView button3;
    private ImageView button4;
    private ImageView button5;
    private ImageView iv_shake;
    private TanImageView iv_correct;

    private GameToTipView gameToTipView;

    /**
     * 屏幕宽
     */
    private int w_screen;
    /**
     * 屏幕高
     */
    private int h_screen;

    private Anim anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        gameToTipView = new GameToTipView(this);
        anim = new Anim();
        anim.setRepeatCount(-1);
        mPhotoView = (PhotoView) findViewById(R.id.showImage);
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        mGameMoveGoldView = findViewById(R.id.GameMoveGoldView);
        mGameOpenDoorView = findViewById(R.id.odv_anim);
        mGameOpenDoorView.setGameOpenDoorListen(new GameOpenDoorView.IGameOpenDoorListen() {
            @Override
            public void openAnimStart() {
                Log.e("jam1", "openAnimStart: ");
            }

            @Override
            public void openAnimEnd() {
                Log.e("jam1", "openAnimEnd: ");
            }

            @Override
            public void closeAnimStart() {
                Log.e("jam1", "closeAnimStart: ");
            }

            @Override
            public void closeAnimEnd() {
                Log.e("jam1", "closeAnimEnd: ");
            }
        });
        iv_shake = findViewById(R.id.iv_shake);
        iv_shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameToTipView.getParent() == null) {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rl_all.addView(gameToTipView);
                }
                gameToTipView.showView(iv_shake);
            }
        });
        iv_setting = findViewById(R.id.iv_setting);
        mGameMoveGoldView.setTargetView(iv_setting);
        mGameMoveGoldView.setOnAnimListen(new GameMoveGoldView.OnAnimListen() {
            @Override
            public void onAnimStart() {

            }

            @Override
            public void onAnimEnd(int count) {
                Log.e("jam", "onAnimEnd: " + count);
            }

            @Override
            public void onAllAnimEnd() {
                Log.e("jam", "onAllAnimEnd ");
            }

            @Override
            public void onAnimRunning() {

            }
        });
        mgv_move = findViewById(R.id.mgv_move);
        moveView = findViewById(R.id.move_view);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        w_screen = dm.widthPixels;
        h_screen = dm.heightPixels;
        iv_correct = findViewById(R.id.iv_correct);
        button = findViewById(R.id.b_start);
        button1 = findViewById(R.id.b_start1);
        button2 = findViewById(R.id.b_start2);
        button3 = findViewById(R.id.b_start3);
        button4 = findViewById(R.id.b_start4);
        button5 = findViewById(R.id.b_start5);
        button5.startAnimation(anim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int[] clickLocation = new int[2];
                //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
//                iv_setting.getLocationInWindow(clickLocation);
//                Log.e("jam", clickLocation[1] + "onClick: ");
//                mGameMoveGoldView.startGoldAnim(10, v);
                mGameOpenDoorView.startCloseAnim();
//                iv_correct.startAim();
//                if (iv_shake.getAnimation() != null && iv_shake.getAnimation().hasStarted()) {
//                    iv_shake.getAnimation().cancel();
//                } else dfad(iv_shake);

//                propertyValuesHolderDown(iv_shake);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameMoveGoldView.startGoldAnim(5, v, button);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameMoveGoldView.start3Anim(5, v);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameMoveGoldView.start3Anim(5, v);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameMoveGoldView.startGoldAnim(5, v, button2, button3);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameMoveGoldView.startGoldAnim(10, v, button, button2, button4, button4);
            }
        });
        App.iLoader
                .displayImage(
                        "http://image.test.com/test/createcourse/COURSE/201512/f92b2f46cf0c48b7a8997a0d124fc46e.png",
                        mPhotoView);
    }


    /**
     * 三次贝塞尔曲线起始点，2控制点，终点
     */
    private Point[] mPoints = new Point[4];
    /**
     * 动画
     */
    private ValueAnimator mAnimator;

    protected void startAnimator() {
        mAnimator = ValueAnimator.ofObject(new BezierEvaluator(), new PointF(mPoints[0].x, mPoints[0].y), new PointF(mPoints[3].x, mPoints[3].y));
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                moveView.setX(pointF.x);
                moveView.setY(pointF.y);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                moveView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.setTarget(moveView);
        mAnimator.start();
    }

    class BezierEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            final float t = fraction;
            float oneMinusT = 1.0f - t;
            PointF point = new PointF();

            PointF point0 = (PointF) startValue;

            PointF point1 = new PointF();
            point1.set(mPoints[1].x, mPoints[1].y);

            PointF point2 = new PointF();
            point2.set(mPoints[2].x, mPoints[2].y);

            PointF point3 = (PointF) endValue;
//CycleInterpolator：动画从开始到结束，变化率是循环给定次数的正弦曲线。
            point.x = oneMinusT * oneMinusT * oneMinusT * (point0.x) + 3 * oneMinusT * oneMinusT * t * (point1.x) + 3 * oneMinusT * t * t * (point2.x) + t * t * t * (point3.x);
            point.y = oneMinusT * oneMinusT * oneMinusT * (point0.y) + 3 * oneMinusT * oneMinusT * t * (point1.y) + 3 * oneMinusT * t * t * (point2.y) + t * t * t * (point3.y);
            return point;
        }
    }


    private void dfad(View view) {

        TranslateAnimation animation = new TranslateAnimation(0, 0, 5, -5);
        animation.setInterpolator(new CycleInterpolator(2f));  //循环次数
        animation.setDuration(700);
        animation.setFillAfter(false);
        animation.setRepeatCount(-1);

        view.setAnimation(animation);
    }

    public void propertyValuesHolderDown(final View view) {
//        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0.9f, 0.9f, 0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f, 1f);
//        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.9f, 0.9f, 0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f, 1f);
//        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.9f, 0.9f, 0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f, 1f);
//        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_shake, "scaleX", 1f, 0.8f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_shake, "scaleY", 1f, 0.8f, 1f);
        animator.setRepeatCount(-1);
        animator1.setRepeatCount(-1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animator1);
        animatorSet.setDuration(200);
        animatorSet.start();

    }

}
