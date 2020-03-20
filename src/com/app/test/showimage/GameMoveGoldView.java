package com.app.test.showimage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.test.R;
import com.app.test.move.MoveImageView;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
public class GameMoveGoldView extends RelativeLayout {
    private int selfX;
    private int selfY;
    private int targetX;
    private int targetY;
    private View targetView;
    private int animCount;
    //时长
    private long duration = 1000;
    private OnAnimListen onAnimListen;
    private Point point0;
    private Point point1;
    private Point point2;
    private Point point3;
    /**
     * 动画
     */


    private boolean alreadyGet;

    private Handler handler = new Handler(Looper.getMainLooper());

    public GameMoveGoldView(Context context) {
        super(context);
        initView();
        initData();
    }

    public GameMoveGoldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    public GameMoveGoldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initData();
    }

    public void initView() {
//        View.inflate(getContext(), R.layout.view_game_move_gold, this);
    }

    public void initData() {

    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setOnAnimListen(OnAnimListen onAnimListen) {
        this.onAnimListen = onAnimListen;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        alreadyGet = false;
    }


    public synchronized void startGoldAnim(int count, final View... clickView) {

        if (clickView == null || targetView == null || handler == null)
            return;
        for (View view : clickView) {
            calculateLocation(count, view);
        }


    }

    public void start3Anim(int count, final View clickView) {

        if (!alreadyGet) {
            int[] location1 = new int[2];
            if (targetView != null) {
                targetView.getLocationInWindow(location1);
                targetX = location1[0];
                targetY = location1[1];
                //终点
                point3 = new Point(targetX, targetY - targetView.getHeight() / 2);
            }
            alreadyGet = true;
        }
        int[] location = new int[2];
        clickView.getLocationOnScreen(location);
        selfX = location[0];
        selfY = location[1];
        //初始点
        point0 = new Point(selfX, selfY);
        point1 = new Point(selfX + 250, selfY+500 );
        point2 = new Point(targetX - 100, targetY + 100);

        for (int i = 0; i < count; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setX(point0.x);
                    imageView.setY(point0.y);
                    imageView.setImageResource(R.drawable.ic_game_gold_00);
                    GameMoveGoldView.this.addView(imageView);
                    startAnim(imageView, point0, point1, point2, point3);
                }
            }, i * 200);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (isFlymeOs4x()) {
            return 2 * statusBarHeight;
        }

        return statusBarHeight;
    }

    public static boolean isFlymeOs4x() {
        String sysVersion = Build.VERSION.RELEASE;
        if ("4.4.4".equals(sysVersion)) {
            String sysIncrement = Build.VERSION.INCREMENTAL;
            String displayId = Build.DISPLAY;
            if (!TextUtils.isEmpty(sysIncrement)) {
                return sysIncrement.contains("Flyme_OS_4");
            } else {
                return displayId.contains("Flyme OS 4");
            }
        }
        return false;
    }

    private void calculateLocation(int count, View view) {
        int[] clickLocation = new int[2];
        int[] targetLocation = new int[2];
        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        view.getLocationOnScreen(clickLocation);
        targetView.getLocationOnScreen(targetLocation);
        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        final PointF startP = new PointF();
        final PointF endP = new PointF();
        final PointF controlP = new PointF();
        //開始的数据点坐标就是 用户给的坐标
        startP.x = clickLocation[0];
        startP.y = clickLocation[1] - getStatusBarHeight(getContext());
        //结束的数据点坐标就是 目标的坐标
        endP.x = targetLocation[0];
        endP.y = targetLocation[1] - getStatusBarHeight(getContext());
        //控制点坐标
        controlP.x = startP.x;
        controlP.y = endP.y;
        for (int i = 0; i < count; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnim(startP, endP, controlP);
                }
            }, i * 80);
        }
        if (onAnimListen != null)
            onAnimListen.onAnimStart();
    }


    private void startAnim(final View view, Point startPoint, Point point1, Point point2, Point endPoint) {
        ObjectAnimator mAnimator = ObjectAnimator.ofObject(view, "mPointF", new BezierEvaluator(point1, point2), new PointF(startPoint.x, startPoint.y), new PointF(endPoint.x, endPoint.y));
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Object target = ((ObjectAnimator) animation).getTarget();
                if (target != null && ((View) target).getParent() != null)
                    removeView((View) target);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.setTarget(view);
        mAnimator.start();
    }


    public void startAnim(PointF startP, PointF endP, PointF controlP) {

        //自己定义ImageView 继承ImageView
        MoveImageView img = new MoveImageView(getContext());
        img.startAnim();
        //设置img在父布局中的坐标位置
        img.setX(startP.x);
        img.setY(startP.y);
        //父布局加入该Img
        addView(img);
        //启动属性动画
        ObjectAnimator animator = ObjectAnimator.ofObject(img, "mPointF", new PointFTypeEvaluator(controlP), startP, endP);
        animator.setDuration(duration);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animCount--;
                if (onAnimListen != null)
                    onAnimListen.onAnimEnd(animCount);
                if (animCount <= 0 && onAnimListen != null)
                    onAnimListen.onAllAnimEnd();
                Object target = ((ObjectAnimator) animation).getTarget();
                if (target != null && target instanceof MoveImageView) {
                    MoveImageView imageView = (MoveImageView) target;
                    imageView.stopAnim();
                    removeView(imageView);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        animCount++;
    }

    /**
     * 自己定义估值器
     */
    public class PointFTypeEvaluator implements TypeEvaluator<PointF> {
        /**
         * 每一个估值器相应一个属性动画。每一个属性动画仅相应唯一一个控制点
         */
        PointF control;
        /**
         * 估值器返回值
         */
        PointF mPointF = new PointF();

        public PointFTypeEvaluator(PointF control) {
            this.control = control;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return getBezierPoint(startValue, endValue, control, fraction);
        }

        /**
         * 二次贝塞尔曲线公式
         *
         * @param start   開始的数据点
         * @param end     结束的数据点
         * @param control 控制点
         * @param t       float 0-1
         * @return 不同t相应的PointF
         */
        private PointF getBezierPoint(PointF start, PointF end, PointF control, float t) {
            mPointF.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x;
            mPointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y;
            return mPointF;
        }
    }


    class BezierEvaluator implements TypeEvaluator<PointF> {
        private Point p1;
        private Point p2;

        public BezierEvaluator(Point point1, Point point2) {
            this.p1 = point1;
            this.p2 = point2;
        }


        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            final float t = fraction;
            float oneMinusT = 1.0f - t;
            PointF point = new PointF();
            point.x = oneMinusT * oneMinusT * oneMinusT * (startValue.x) + 3 * oneMinusT * oneMinusT * t * (p1.x) + 3 * oneMinusT * t * t * (p2.x) + t * t * t * (endValue.x);
            point.y = oneMinusT * oneMinusT * oneMinusT * (startValue.y) + 3 * oneMinusT * oneMinusT * t * (p1.y) + 3 * oneMinusT * t * t * (p2.y) + t * t * t * (endValue.y);
            return point;
        }
    }


    public interface OnAnimListen {
        void onAnimStart();

        void onAnimEnd(int count);

        void onAllAnimEnd();

        void onAnimRunning();
    }
}
