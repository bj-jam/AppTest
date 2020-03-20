package com.app.test.redenveloped;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.util.Utils;
import com.app.test.utils.DisplayUtil;

import java.util.List;
import java.util.Random;

public class RedEnvelopesLayout extends FrameLayout {

    private TimeCountDown timeCountDown;
    private PollingCheck pollingCheck;

    private Bitmap bitmap1;
    private Bitmap bitmap3;
    private Bitmap bitmap2;
    private Bitmap bitmap4;
    private Bitmap clickBitmap;
    private Bitmap goldBitmap;
    private Bitmap[] bitmaps;

    public interface ActivityFinishInter {
        void finish();
    }

    private ActivityFinishInter inter;
    private RedEnvelopesView rpvRedPacket;
    private List<RedEnvelopes> redPacketList;
    //单位：秒
    private int redPacketTime;

    //    private ImageView ivRedPacketBack;
    private TextView tvMoney;
    private TextView tvTime;
    private ImageView ivTimeDownPrompt;
    private ImageView ivReady;
    private FrameLayout flRedPacketTop;
    private final int redPacketEnd = 2000;
    private int moneyTotal;
    private ValueAnimator valueAnimator;
    //用于动画参数
    private int showMoney;
    //是否可以返回
    private boolean canBack;
    private Random random = new Random();
    //如果没抢到金币，不再玩，关闭窗口或者退出页面 需要统计
    private boolean noMoneyFinish;

    private boolean isDestroy;
    public static final int RED_PACKET_END = 111;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RED_PACKET_END:
                    if (msg.arg1 == redPacketEnd) {
                        canBack = true;
                        autoReceiveMoney();
                    } else {
                        checkIsEnd();
                    }
                    break;
            }
        }
    };

    private StringBuffer stringBuffer = new StringBuffer();

    public RedEnvelopesLayout(Context context) {
        super(context);
        initView();
        initData();
    }

    public RedEnvelopesLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    public RedEnvelopesLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initData();
    }


    private void initView() {
        View.inflate(getContext(), R.layout.layout_redpacket, this);
        rpvRedPacket = $(this, R.id.rpvRedPacket);
//        ivRedPacketBack = $(this, R.id.ivRedPacketBack);
        tvMoney = $(this, R.id.tvMoney);
        tvTime = $(this, R.id.tvTime);
        ivTimeDownPrompt = $(this, R.id.ivTimeDownPrompt);
        ivReady = $(this, R.id.ivReady);
        flRedPacketTop = $(this, R.id.flRedPacketTop);
    }


    protected void initData() {
        random = new Random();
    }

    public void setActivityFinish(ActivityFinishInter inter) {
        this.inter = inter;
    }


    public void setData(List<RedEnvelopes> list, int time) {
        redPacketList = list;
        redPacketTime = time;

        tvTime.setText(redPacketTime + "s");
        ivReady.setVisibility(VISIBLE);
//        ivTimeDownPrompt.setVisibility(VISIBLE);
    }

    public void onDestroy() {
        isDestroy = true;
        if (!Utils.isEmpty(timeCountDown)) {
            timeCountDown.onDestroy();
            timeCountDown = null;
        }

        if (!Utils.isEmpty(valueAnimator)) {
            valueAnimator.cancel();
            valueAnimator = null;
        }

        if (!Utils.isEmpty(handler)) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (!Utils.isEmpty(pollingCheck)) {
            pollingCheck.onDestroy();
            pollingCheck = null;
        }
        if (!Utils.isEmpty(inter)) {
            inter = null;
        }
    }

    private synchronized void addMoney(int moneyNum) {
        moneyTotal += moneyNum;
    }

    public synchronized int getMoney() {
        return moneyTotal;
    }

    public void readyGo() {
        if (Utils.isEmpty(handler)) {
            return;
        }
        //因为activity过度动画设置了时间，然后延迟执行也能让动画一开始没有卡顿效果
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeDownStart(3);
            }
        }, 1000);
    }

    private void timeDownStart(int time) {
        if (Utils.isEmpty(ivTimeDownPrompt)) {
            return;
        }
        ivTimeDownPrompt.setVisibility(VISIBLE);
        if (time >= 3 || time < 1) {
            ivTimeDownPrompt.setImageResource(R.drawable.ic_redpacket_timedown3);
            time = 3;
        }
        ivTimeDownPrompt.setScaleX(0.5f);
        ivTimeDownPrompt.setScaleX(0.5f);

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.6f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.6f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ivTimeDownPrompt, scaleXHolder, scaleYHolder);

        final int finalTime = time;
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!Utils.isEmpty(ivTimeDownPrompt)) {
                    ivTimeDownPrompt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (Utils.isEmpty(ivTimeDownPrompt)) {
                    return;
                }
                ivTimeDownPrompt.setVisibility(View.INVISIBLE);
                switch (finalTime - 1) {
                    case 3:
                        ivTimeDownPrompt.setImageResource(R.drawable.ic_redpacket_timedown3);
                        break;
                    case 2:
                        ivTimeDownPrompt.setImageResource(R.drawable.ic_redpacket_timedown2);
                        break;
                    case 1:
                        ivTimeDownPrompt.setImageResource(R.drawable.ic_redpacket_timedown1);
                        break;
                    default:
                        ivTimeDownPrompt.setImageResource(R.drawable.ic_redpacket_timedown3);
                }
                if (finalTime == 1) {
                    if (!Utils.isEmpty(ivReady)) {
                        ivReady.setVisibility(View.INVISIBLE);
                    }
                    //倒计时完成，准备掉红包
                    startShowRedPacket();
                    return;
                }
                timeDownStart(finalTime - 1);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    private void checkIsEnd() {
        if (isExit() || Utils.isEmpty(handler)) {
            return;
        }
        if (!Utils.isEmpty(rpvRedPacket) && rpvRedPacket.isEnd()) {
            Message message = Message.obtain();
            message.what = RED_PACKET_END;
            message.arg1 = redPacketEnd;
            handler.sendMessage(message);
        } else {
            handler.sendEmptyMessageDelayed(RED_PACKET_END, 300);
        }

    }

    private void startShowRedPacket() {
        //重新玩的时候清除之前的子view检测以及是否结束
        if (!Utils.isEmpty(handler)) {
            handler.removeMessages(RED_PACKET_END);
        }
        if (!Utils.isEmpty(tvTime)) {
            tvTime.setText(redPacketTime + "s");
        }
        timeCountDown = new TimeCountDown();
        timeCountDown.start(redPacketTime, new TimeCountDown.TimeCallback() {
            @Override
            public void onNext(int timeSecond) {
                tvTime.setText(timeSecond + "s");
            }

            @Override
            public void onComplete() {
                if (!Utils.isEmpty(tvTime)) {
                    tvTime.setText("0s");
                }
                if (!Utils.isEmpty(handler)) {
                    handler.sendEmptyMessageDelayed(RED_PACKET_END, 300);
                }
            }
        });

        if (Utils.isEmpty(rpvRedPacket) || Utils.isEmpty(handler)) {
            return;
        }


        if (Utils.isEmpty(redPacketList)) {
            return;
        }


        //产生红包的间隔时间
        final long interval = redPacketTime * 1000L / redPacketList.size();


        final long tempIntervalTimeMillis = (getMaxMillis() - getMinMillis()) / redPacketList.size();
        checkBitmap();
        //因为4个红包宽高一致，这里获取一个图片的宽度就行了
        final int redPacketWidth = getBitmapWidth(bitmap1, false);
//        final int goldWidth = getGoldBitmapWidth(goldBitmap);
        final int clickRedPacketWidth = getBitmapWidth(clickBitmap, true);
        final int clickRedPacketHeight = getClickBitmapHeight(clickBitmap);

        final int dp10 = DisplayUtil.dip2px(getContext(), 10);
        final int screenWidth = getScreenWidth(getContext());
        final int screenHeight = getScreenHeight(getContext());
        final int containerHeight = screenHeight + redPacketWidth;


        final int goldDismissXOffset = 17;
        final int goldDismissYOffset = 17;

        pollingCheck = new PollingCheck();
        pollingCheck.startForMillis(interval, new PollingCheck.CheckCallback() {
            @Override
            public boolean onCheck(int checkCount) {
                if (Utils.isEmpty(getContext())) {
                    return true;
                }
                if (isExit()) {
                    return true;
                }
                if (Utils.isEmpty(redPacketList)) {
                    return true;
                }

                int index = checkCount - 1;
                if (index < 0) {
                    index = 0;
                } else if (index >= redPacketList.size()) {
                    index = redPacketList.size() - 1;
                }
                RedEnvelopes bean = redPacketList.get(index);

                final Bitmap randomRedPacket = RedEnvelopesLocation.get().getRandomRedPacket(bitmaps);

                long durationTimeMillis = getMaxMillis() - tempIntervalTimeMillis * index;
                //下一个红包的x方向位置
                int nextRedPacketX = RedEnvelopesLocation.get().getNextRedPacketX(dp10, screenWidth, redPacketWidth);

                RedEnvelopesHelper redPacketHelper = RedEnvelopesHelper.produceRedPacket(index, getContext(), redPacketWidth, redPacketWidth, clickRedPacketWidth, clickRedPacketHeight, nextRedPacketX, durationTimeMillis);
//                redPacketHelper.setGoldBitmapWidth(goldWidth);
//                redPacketHelper.setGoldBitmapHeight(goldWidth);

                if (!Utils.isEmpty(redPacketHelper)) {
                    redPacketHelper.setGoldDismissXOffset(goldDismissXOffset);
                    redPacketHelper.setGoldDismissYOffset(goldDismissYOffset);

                    redPacketHelper.setMoney(bean.getMoney());

                    redPacketHelper.setRedPacketInter(new RedEnvelopesHelper.RedPacketInter() {
                        @Override
                        public void clickRedPacket(RedEnvelopesHelper bean) {
                            if (Utils.isEmpty(stringBuffer)) {
                                stringBuffer = new StringBuffer();
                            }
                            stringBuffer.append(String.valueOf(bean.getIndex()));
                            stringBuffer.append(",");

                            addMoney(bean.getMoney());
                            changeMoneyTextAnim();
                        }

                        @Override
                        public Bitmap getNormalBitmap() {
                            return randomRedPacket;
                        }

                        @Override
                        public Bitmap getClickBitmap() {
                            return clickBitmap;
                        }

                        @Override
                        public Bitmap getGoldBitmap() {
                            return goldBitmap;
                        }
                    });
                    if (Utils.isEmpty(rpvRedPacket)) {
                        return true;
                    }
                    rpvRedPacket.addRedPacketData(checkCount - 1, redPacketHelper);
                    redPacketHelper.startDown(containerHeight);

                    if (checkCount == 1) {
                        rpvRedPacket.startDraw();
                    }
                }

                if (checkCount >= redPacketList.size() || isExit()) {
                    return true;
                }
                return false;
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void checkBitmap() {

        if (Utils.isEmptyAny(bitmap1, bitmap2, bitmap3, bitmap4)) {
            bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redpacket1);
            bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redpacket2);
            bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redpacket3);
            bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redpacket4);
            bitmaps = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        }
        if (Utils.isEmpty(clickBitmap)) {
            clickBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redpacket_click);
        }
        if (Utils.isEmpty(goldBitmap)) {
            goldBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redpacket_gold_active);
        }
    }

    private int getBitmapWidth(Bitmap bitmap, boolean isClickBitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return isClickBitmap ? 258 : 204;
        }
        return bitmap.getWidth();
    }

    private int getGoldBitmapWidth(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 150;
        }
        return bitmap.getWidth();
    }

    private int getClickBitmapHeight(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 331;
        }
        return bitmap.getHeight();
    }

    public void autoReceiveMoney() {
        if (isExit()) {
            return;
        }
        if (Utils.isEmpty(stringBuffer) || Utils.trimToEmpty(stringBuffer.toString())) {
            /*PromptUtils.showToast(ctx,"无金币可领取");*/
            finish();
            return;
        }
        int lastIndexOf = stringBuffer.lastIndexOf(",");
        if (lastIndexOf != -1 && lastIndexOf == stringBuffer.length() - 1) {
            stringBuffer.deleteCharAt(lastIndexOf);
        }

//        getPresenter().receiveMoney(stringBuffer.toString());
    }

    public void autoReceiveOnError(int type) {
        hideOtherView();
    }


    public boolean canBack() {
        return canBack;
    }

    public void finish() {
        if (inter != null) {
            inter.finish();
        }
    }

    //获取金币数量改变动画
    private void changeMoneyTextAnim() {
        if (Utils.isEmpty(valueAnimator)) {
            valueAnimator = ValueAnimator.ofInt(0, getMoney());
        }
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(showMoney, getMoney());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                showMoney = animatedValue;
                if (!Utils.isEmpty(tvMoney)) {
                    tvMoney.setText(String.valueOf(showMoney));
                }
            }
        });
        valueAnimator.setDuration(900);
        valueAnimator.start();
    }


    //红包下落最长时间
    public int getMaxMillis() {
        return 3200;
    }

    //红包下落最短时间
    public int getMinMillis() {
        return 1400;
    }


    private void hideOtherView() {
        if (Utils.isEmpty(flRedPacketTop)) {
            return;
        }
        if (flRedPacketTop.getVisibility() == INVISIBLE) {
            return;
        }
        AlphaAnimation flRedPacketTopAnimation = new AlphaAnimation(1, 0);
        flRedPacketTopAnimation.setRepeatCount(0);
        flRedPacketTopAnimation.setDuration(400);
        flRedPacketTop.setVisibility(INVISIBLE);
        flRedPacketTop.startAnimation(flRedPacketTopAnimation);

        AlphaAnimation ivRedPacketBottomAnimation = new AlphaAnimation(1, 0);
        ivRedPacketBottomAnimation.setRepeatCount(0);
        ivRedPacketBottomAnimation.setDuration(400);
    }

    public void showOtherView() {
        if (Utils.isEmpty(flRedPacketTop)) {
            return;
        }
//        AlphaAnimation flRedPacketTopAnimation=new AlphaAnimation(1,0);
//        flRedPacketTopAnimation.setRepeatCount(0);
//        flRedPacketTopAnimation.setDuration(400);
        flRedPacketTop.setVisibility(VISIBLE);
//        flRedPacketTop.startAnimation(flRedPacketTopAnimation);

//        AlphaAnimation ivRedPacketBottomAnimation=new AlphaAnimation(1,0);
//        ivRedPacketBottomAnimation.setRepeatCount(0);
//        ivRedPacketBottomAnimation.setDuration(400);
//        ivRedPacketBottom.setVisibility(VISIBLE);
//        ivRedPacketBottom.startAnimation(ivRedPacketBottomAnimation);
    }

    protected final <E extends View> E $(@NonNull View view, @IdRes int id) {
        return view.findViewById(id);
    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        return 0;
    }


    public boolean isExit() {
        return isDestroy;
    }

    public boolean isNoMoneyFinish() {
        return noMoneyFinish;
    }

    public void setNoMoneyFinish(boolean noMoneyFinish) {
        this.noMoneyFinish = noMoneyFinish;
    }
}
