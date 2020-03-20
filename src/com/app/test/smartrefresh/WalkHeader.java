package com.app.test.smartrefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.util.DisplayUtil;
import com.app.test.util.FrameAnimationUtils;
import com.app.test.util.Utils;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WalkHeader extends FrameLayout implements RefreshHeader {

    private final int MSG_UPDATE_TIME_TXT = 10000000;
    //正的猫和红包
    private ImageView iv_move;
    //反的猫头
    private ImageView iv_top;
    //距离下次红包雨时间
    private TextView tv_time;
    //下拉红包整个view
    private LinearLayout ll_move;
    //冷却时间整个view
    private LinearLayout ll_end; //冷却时间整个view
    //下拉结束之后红包爆开的动画
    private ImageView iv_red_end;
    private RelativeLayout rl_top;
    private LayoutParams topParams;
    private LinearLayout.LayoutParams catParams;

    //下拉的时的高度
    int pullMargin;
    //下拉的时的高度
    int moveMargin;
    //是否在移动
    boolean isMoving;
    //初始顶部距离
    private int initMargin;
    //点击之后的顶部距离
    private int firstMargin;
    //移动区间内图片放大
    private int moveDistance;
    //原始的高度
    private int initHeight;
    //原始的宽度
    private int initWidth;
    //最大的高度
    private int maxHeight;
    //最大的宽度
    private int maxWidth;
    //最大的高度
    private int maxMoveHeight;
    //最大的宽度
    private int maxMoveWidth;
    //放大的比例
    private float scale = 1.5f;
    private Handler handler;
    private Handler handlerUpdateTxt;

    private RedEnvelopesStatus redEnvelopesStatus;
    private StringBuilder sb;
    private ScheduledExecutorService executorService;
    private FrameAnimationUtils frameAnimationUtils;
    private FrameAnimationUtils topAnim;

    private boolean isClicked;

    private long initTime;

    boolean isPull;

    boolean isRefreshing;


    public WalkHeader(Context context) {
        super(context);
        initView();
        initData();
    }

    public WalkHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    public WalkHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initData();
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        Log.e("jam", percent + "////" + offset + "////" + headerHeight + "////" + extendHeight);
        if (isRefreshing)
            return;
        if (!isPull) {
            setPullHeight();
            isPull = true;
        }
        if (percent == 0 && offset == 0) {
            isPull = false;
            restoreHeight();
        } else {
            isRestore = false;
            changeHeight((int) (offset * 1.5));
        }
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
//        Log.e("jam", "onReleasing");
    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        Log.e("jam", "onRefreshReleased");
        restoreHeight();
        isPull = false;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.FixedFront;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        Log.e("jam", "---" + newState);
        switch (newState) {
            case PullDownToRefresh:
                break;
            case Refreshing://刷新中
                isRefreshing = true;
                break;
            case ReleaseToRefresh://可以刷新了
                break;
            case RefreshFinish:
                isRefreshing = false;
                break;
            case PullDownCanceled://手动滑到消失既取消
            case RefreshReleased://

            case None:
                restoreHeight();
                isPull = false;
                break;
        }
    }


    @NonNull
    protected final <E extends View> E $(@IdRes int id) {
        return this.findViewById(id);
    }

    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_cat_header, this);
        iv_move = $(R.id.iv_move);
        iv_top = $(R.id.iv_top);
        ll_move = $(R.id.ll_move);
        tv_time = $(R.id.tv_time);
        ll_end = $(R.id.ll_end);
        rl_top = $(R.id.rl_top);
        iv_red_end = $(R.id.iv_red_end);
        if (Utils.isEmpty(topAnim))
            topAnim = getTopAnim();
        if (!Utils.isEmpty(topAnim))
            topAnim.start();
        rl_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoving) {
                    isMoving = true;
                    if (!Utils.isEmpty(handler) && !Utils.isEmpty(animRun))
                        handler.removeCallbacks(animRun);
                    startTopHide();
                }

            }
        });
        iv_move.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoving) {
                    isClicked = true;
                    isMoving = true;
                    if (!Utils.isEmpty(handler) && !Utils.isEmpty(animRun))
                        handler.removeCallbacks(animRun);
                    if (!Utils.isEmpty(topParams))
                        smallAnim(true, topParams.topMargin, initMargin);
                }

            }
        });
    }

    protected void initData() {
        initMargin = -DisplayUtil.getInstance().dip2px(170);
        firstMargin = -DisplayUtil.getInstance().dip2px(90);
        moveDistance = 0 - firstMargin;
        handler = new Handler(Looper.getMainLooper());
        handlerUpdateTxt = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_UPDATE_TIME_TXT) {
                    if (Utils.isEmpty(tv_time) || Utils.isEmpty(redEnvelopesStatus) || !redEnvelopesStatus.isEnd()) {
                        releaseExecutorService();
                        return;
                    }
                    redEnvelopesStatus.setCoolingTime(redEnvelopesStatus.getCoolingTime() - 1);
                    if (!Utils.isEmpty(tv_time))
                        if (redEnvelopesStatus.getCoolingTime() > 0) {
                            tv_time.setText(coolingTime(redEnvelopesStatus.getCoolingTime()));
                        } else {
                            getRerStatus();
                            releaseExecutorService();
                        }
                }
                super.handleMessage(msg);
            }
        };
        getRerStatus();
    }

    public void getRerStatus() {
        RedEnvelopesStatus redEnvelopesStatus = new RedEnvelopesStatus();
        onLoadDataSuccess(redEnvelopesStatus);
    }


    private Runnable animRun = new Runnable() {
        @Override
        public void run() {
            isMoving = true;
            smallAnim(false, firstMargin, initMargin);
        }
    };

    public void onDestroy() {
        if (!Utils.isEmpty(handler)) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (!Utils.isEmpty(handlerUpdateTxt)) {
            handlerUpdateTxt.removeCallbacksAndMessages(null);
        }
        if (!Utils.isEmpty(frameAnimationUtils)) {
            frameAnimationUtils.release();
            frameAnimationUtils = null;
        }
        if (!Utils.isEmpty(topAnim)) {
            topAnim.release();
            topAnim = null;
        }
        if (!Utils.isEmpty(animRun))
            animRun = null;
        releaseExecutorService();
    }

    private void releaseExecutorService() {
        try {
            if (!Utils.isEmpty(executorService)) {
                if (!executorService.isShutdown()) {
                    executorService.shutdownNow();
                }
                executorService = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!Utils.isEmpty(ll_move))
            topParams = (LayoutParams) ll_move.getLayoutParams();
        if (!Utils.isEmpty(iv_move))
            catParams = (LinearLayout.LayoutParams) iv_move.getLayoutParams();
        if (!Utils.isEmpty(catParams)) {
            initHeight = catParams.height;
            initWidth = catParams.width;
        }
        maxHeight = (int) (initHeight * scale);
        maxWidth = (int) (initWidth * scale);
        maxMoveHeight = maxHeight - initHeight;
        maxMoveWidth = maxWidth - initWidth;
    }

    public void onLoadDataSuccess(RedEnvelopesStatus dto) {
        if (Utils.isEmpty(dto)) {
            releaseExecutorService();
            return;
        }
        redEnvelopesStatus = dto;
        //已结束
        if (redEnvelopesStatus.isEnd() || redEnvelopesStatus.todayIsEnd()) {
            if (!Utils.isEmpty(ll_end)) {
                ll_end.setVisibility(VISIBLE);
            }
            if (!Utils.isEmpty(iv_top)) {
                iv_top.setVisibility(GONE);
            }

            //今天已结束
            if (redEnvelopesStatus.todayIsEnd()) {
                if (!Utils.isEmpty(tv_time)) {
                    tv_time.setVisibility(GONE);
                }
            } else {
                if (!Utils.isEmpty(tv_time)) {
                    tv_time.setVisibility(VISIBLE);
                }
                releaseExecutorService();
                initTime = SystemClock.elapsedRealtime();
                executorService = Executors.newScheduledThreadPool(1);
                executorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        long time = SystemClock.elapsedRealtime() - initTime;
                        //有几率出现1001 1002的差值，在给放大15ms的差值
                        if (time > 1015) {
                            redEnvelopesStatus.setCoolingTime(redEnvelopesStatus.getCoolingTime() - time / 1000);
                            if (redEnvelopesStatus.getCoolingTime() <= 0) {
//                                getPresenter().getStatus();
                                releaseExecutorService();
                                return;
                            }
                        }
                        initTime = SystemClock.elapsedRealtime();
                        try {
                            handlerUpdateTxt.removeMessages(MSG_UPDATE_TIME_TXT);
                            handlerUpdateTxt.sendEmptyMessage(MSG_UPDATE_TIME_TXT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }
        } else {
            if (!Utils.isEmpty(ll_end)) {
                ll_end.setVisibility(GONE);
            }
            if (!Utils.isEmpty(iv_top)) {
                iv_top.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 点击常驻的view的时候的开始动画
     */
    public void onlyTopHide() {
        if (Utils.isEmpty(rl_top))
            return;
        TranslateAnimation animation = yToHide(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Utils.isEmpty(rl_top))
                    return;
                rl_top.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //启动动画
        rl_top.startAnimation(animation);
    }

    /**
     * 点击常驻的view的时候的开始动画
     */
    private void startTopHide() {
        if (Utils.isEmpty(rl_top))
            return;
        TranslateAnimation animation = yToHide(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!Utils.isEmpty(rl_top))
                    rl_top.setVisibility(View.GONE);
                //执行需要显示的view的动画
                bigAnim(initMargin, firstMargin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //启动动画
        rl_top.startAnimation(animation);
    }

    /**
     * 反的猫显示动画
     */
    private void startTopShow() {
        if (Utils.isEmpty(rl_top))
            return;
        rl_top.startAnimation(yToShow());
    }

    /**
     * 正的的猫+红包显示动画
     */
    private void setMargins(int value) {
        if (Utils.isEmpty(topParams) || Utils.isEmpty(ll_move))
            return;
        topParams.setMargins(0, value, 0, 0);
        ll_move.setLayoutParams(topParams);
    }

    /**
     * 正的的猫+红包显示放大或者缩小
     */
    private void setScale(int value) {
        if (Utils.isEmpty(catParams) || Utils.isEmpty(iv_move))
            return;
        catParams.height = initHeight + maxMoveHeight * value / moveDistance;
        catParams.width = initWidth + maxMoveWidth * value / moveDistance;
        iv_move.setLayoutParams(catParams);
    }


    private void bigAnim(int... value) {
        ValueAnimator valueAnimator = valueAnim();
        valueAnimator.setIntValues(value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int value = (int) animation.getAnimatedValue();
                setMargins(value);
                //扩大动画结束
                if (value >= firstMargin) {
                    isMoving = false;
                    //延时2秒自动收起
                    if (!Utils.isEmpty(handler) && !Utils.isEmpty(animRun))
                        handler.postDelayed(animRun, 2000);
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 顶部的view缩小计时动画
     *
     * @param value
     */
    private void smallAnim(final boolean isToActivity, int... value) {
        ValueAnimator valueAnimator = valueAnim();
        valueAnimator.setIntValues(value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int value = (int) animation.getAnimatedValue();
                setMargins(value);
                if (value >= firstMargin) {
                    setScale(value - firstMargin);
                }
                if (value <= initMargin) {
                    //执行需要显示的view的动画
                    if (!Utils.isEmpty(rl_top))
                        rl_top.setVisibility(VISIBLE);
                    startTopShow();
                    isMoving = false;
                    if (isToActivity)
                        startAnim();
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 执行半瓶撒红包动画，结束之后跳转金币页面
     */
    public void startAnim() {
        //状态空的或者时间冷却中
        if (Utils.isEmpty(redEnvelopesStatus))
            return;
        if (moveMargin >= 0 || isClicked) {
            if (!Utils.isEmpty(iv_red_end)) {
                iv_red_end.setVisibility(VISIBLE);
                if (Utils.isEmpty(frameAnimationUtils)) {
                    frameAnimationUtils = getFrameAnimationUtilsConfig();
                }
                if (Utils.isEmpty(frameAnimationUtils)) {
                    toNewActivity();
                    return;
                }
                isMoving = true;
                frameAnimationUtils.restartAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toNewActivity();
                    }
                }, 1044);
            }
        }
    }

    private void toNewActivity() {
        toRedEnvelopes();
        if (!Utils.isEmpty(iv_red_end))
            iv_red_end.setVisibility(GONE);
        isClicked = false;
        isMoving = false;
    }

    private void toRedEnvelopes() {

    }


    private FrameAnimationUtils getFrameAnimationUtilsConfig() {
        if (Utils.isEmpty(iv_red_end))
            return null;
        FrameAnimationUtils.Config config = new FrameAnimationUtils.Config();
        config.setImageView(iv_red_end);
        config.setFrameResArray(Utils.getRes(getContext(), R.array.end_red_anim_envelopes));
        config.setDuration(36);
        config.setRepeatCount(0);
        return config.build();
    }

    private FrameAnimationUtils getTopAnim() {
        if (Utils.isEmpty(iv_top))
            return null;
        FrameAnimationUtils.Config config = new FrameAnimationUtils.Config();
        config.setImageView(iv_top);
        int[] frameResArray = Utils.getRes(getContext(), R.array.red_envelopes_top_anim);
        config.setFrameResArray(frameResArray);
        if (!Utils.isEmpty(frameResArray) && frameResArray.length > 0) {
            int length = frameResArray.length;
            int[] durations = new int[length];
            for (int i = 0; i < length; i++) {
                if (i == length - 1)
                    durations[i] = 732;
                else
                    durations[i] = 42;
            }
            config.setDurations(durations);
        }
        config.setRepeatCount(-1);
        return config.build();
    }

    /**
     * 开始拉伸了
     */
    public void setPullHeight() {
        if (!Utils.isEmpty(topParams))
            pullMargin = topParams.topMargin;
        //有滚动事件了 一处两秒复原的动作
        if (!Utils.isEmpty(handler) && !Utils.isEmpty(animRun))
            handler.removeCallbacks(animRun);
        //没有点击过、先执行点击之后的操作，然后在拉伸
        if (!Utils.isEmpty(valueAnimator))
            valueAnimator.cancel();
        if (pullMargin <= initMargin)
            onlyTopHide();
    }

    public boolean getIsMoving() {
        return isMoving;
    }

    /**
     * 下拉的距离改变 ui要跟着变化
     *
     * @param distance
     */
    public void changeHeight(int distance) {
        if (Utils.isEmpty(topParams) || Utils.isEmpty(ll_move))
            return;
        //初始marginTop加上滑动的距离
        moveMargin = pullMargin + distance;
        if (moveMargin <= 0) {
            topParams.topMargin = moveMargin;
            ll_move.setLayoutParams(topParams);
        }
        if (topParams.topMargin >= firstMargin) {
            setScale(topParams.topMargin - firstMargin);
        }
    }

    boolean isRestore;

    public void restoreHeight() {
        if (isRestore)
            return;
        isRestore = true;
        if (Utils.isEmpty(topParams))
            return;
        smallAnim(true, topParams.topMargin, initMargin);
    }

    /**
     * 冷却时间显示问题
     *
     * @param time
     * @return
     */
    private String coolingTime(long time) {
        sb = new StringBuilder();
        if (time < 60) {
            sb.append(time);
            sb.append("秒");
        } else {
            if (time % 60 > 0)
                sb.append(time / 60 + 1);
            else
                sb.append(time / 60);
            sb.append("分钟");
        }
        sb.append("后开抢红包");
        return sb.toString();

    }


    /**
     * y轴隐藏动画
     *
     * @param time 动画执行的时长
     * @return
     */
    public TranslateAnimation yToHide(long time) {
        TranslateAnimation hideY = new TranslateAnimation(
                //X轴初始位置
                Animation.RELATIVE_TO_SELF, 0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, 0f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, 0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, -1f);
        //200秒完成动画
        hideY.setDuration(time);
        hideY.setFillAfter(true);
        return hideY;
    }

    /**
     * Y轴显示动画
     *
     * @return
     */
    public TranslateAnimation yToShow() {
        TranslateAnimation showY = new TranslateAnimation(
                //X轴初始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, 0f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, -1f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, 0f);
        //200秒完成动画
        showY.setDuration(200);
        showY.setFillAfter(true);
        return showY;
    }

    ValueAnimator valueAnimator;

    public ValueAnimator valueAnim(int... value) {
        if (!Utils.isEmpty(valueAnimator))
            valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofInt(value);
        valueAnimator.setDuration(500);
        return valueAnimator;
    }

    /**
     * 红包雨的activity是否在顶部
     *
     * @param context
     * @return
     */
    private boolean isActivity(Context context) {
//        if (Utils.isEmpty(context)) {
//            return false;
//        }
//        try {
//            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            if (Utils.isEmpty(am))
//                return false;
//            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//            String className = cn.getClassName();
//            return TextUtils.equals(className, RedPacketActivity.class.getName());
//        } catch (Exception e) {
//            return false;
//        }
        return false;
    }

}
