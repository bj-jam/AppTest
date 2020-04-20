package com.app.test.redenveloped;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;

import java.util.List;

public class RedEnvelopesLayout extends FrameLayout {

    private TimeCountDown timeCountDown;
    private TimerUtil timerUtil;

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
    private RedEnvelopesView redEnvelopesView;
    private List<RedEnvelopes> redEnvelopesList;
    //单位：秒
    private int redEnvelopesTime;

    private TextView tvMoney;
    private TextView tvTime;
    private ImageView ivTime;
    private ImageView ivReady;
    private FrameLayout flRedEnvelopesTop;
    private final int redEnvelopesEnd = 2000;
    private int moneyTotal;
    private ValueAnimator valueAnim;
    //用于动画参数
    private int showMoney;
    public static final int RED_PACKET_END = 111;

    private StringBuffer sb = new StringBuffer();


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RED_PACKET_END:
                    if (msg.arg1 == redEnvelopesEnd) {
                        autoReceiveMoney();
                    } else {
                        checkIsEnd();
                    }
                    break;
            }
        }
    };


    public RedEnvelopesLayout(Context context) {
        super(context);
        initView();
    }

    public RedEnvelopesLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RedEnvelopesLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        View.inflate(getContext(), R.layout.layout_redpacket, this);
        redEnvelopesView = findViewById(R.id.rpvRedPacket);
        tvMoney = findViewById(R.id.tvMoney);
        tvTime = findViewById(R.id.tvTime);
        ivTime = findViewById(R.id.ivTimeDownPrompt);
        ivReady = findViewById(R.id.ivReady);
        flRedEnvelopesTop = findViewById(R.id.flRedPacketTop);
    }

    public void setData(List<RedEnvelopes> list, int time) {
        redEnvelopesList = list;
        redEnvelopesTime = time;

        tvTime.setText(redEnvelopesTime + "s");
        ivReady.setVisibility(VISIBLE);
    }

    public void onDestroy() {
        if (!Utils.isEmpty(timeCountDown)) {
            timeCountDown.onDestroy();
            timeCountDown = null;
        }

        if (!Utils.isEmpty(valueAnim)) {
            valueAnim.cancel();
            valueAnim = null;
        }

        if (!Utils.isEmpty(handler)) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (!Utils.isEmpty(timerUtil)) {
            timerUtil.onDestroy();
            timerUtil = null;
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
        if (Utils.isEmpty(ivTime)) {
            return;
        }
        ivTime.setVisibility(VISIBLE);
        if (time >= 3 || time < 1) {
            ivTime.setImageResource(R.drawable.ic_three);
            time = 3;
        }
        ivTime.setScaleX(0.5f);
        ivTime.setScaleX(0.5f);

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.6f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.6f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ivTime, scaleXHolder, scaleYHolder);

        final int finalTime = time;
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!Utils.isEmpty(ivTime)) {
                    ivTime.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (Utils.isEmpty(ivTime)) {
                    return;
                }
                ivTime.setVisibility(View.INVISIBLE);
                switch (finalTime - 1) {
                    case 2:
                        ivTime.setImageResource(R.drawable.ic_two);
                        break;
                    case 1:
                        ivTime.setImageResource(R.drawable.ic_one);
                        break;
                    default:
                        ivTime.setImageResource(R.drawable.ic_three);
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
        if (!Utils.isEmpty(redEnvelopesView) && redEnvelopesView.isEnd()) {
            Message message = Message.obtain();
            message.what = RED_PACKET_END;
            message.arg1 = redEnvelopesEnd;
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
            tvTime.setText(redEnvelopesTime + "s");
        }
        timeCountDown = new TimeCountDown();
        timeCountDown.start(redEnvelopesTime, new TimeCountDown.TimeCallback() {
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

        if (Utils.isEmpty(redEnvelopesView) || Utils.isEmpty(handler)) {
            return;
        }


        if (Utils.isEmpty(redEnvelopesList)) {
            return;
        }


        //产生红包的间隔时间
        final long interval = redEnvelopesTime * 1000L / redEnvelopesList.size();


        final long tempIntervalTimeMillis = (getMaxMillis() - getMinMillis()) / redEnvelopesList.size();
        checkBitmap();
        //因为4个红包宽高一致，这里获取一个图片的宽度就行了
        final int redEnvelopesWidth = getBitmapWidth(bitmap1, false);
//        final int goldWidth = getGoldBitmapWidth(goldBitmap);
        final int clickRedPacketWidth = getBitmapWidth(clickBitmap, true);
        final int clickRedPacketHeight = getClickBitmapHeight(clickBitmap);

        final int dp10 = DensityUtil.dp2px(10);
        final int screenWidth = DensityUtil.getScreenWidth(getContext());
        final int screenHeight = DensityUtil.getScreenHeight(getContext());
        final int containerHeight = screenHeight + redEnvelopesWidth;


        final int goldDismissXOffset = 17;
        final int goldDismissYOffset = 17;

        timerUtil = new TimerUtil();
        timerUtil.startTime(interval, new TimerUtil.TimerCallback() {
            @Override
            public boolean onCheck(int checkCount) {
                if (Utils.isEmpty(getContext())) {
                    return true;
                }
                if (isExit()) {
                    return true;
                }
                if (Utils.isEmpty(redEnvelopesList)) {
                    return true;
                }

                int index = checkCount - 1;
                if (index < 0) {
                    index = 0;
                } else if (index >= redEnvelopesList.size()) {
                    index = redEnvelopesList.size() - 1;
                }
                RedEnvelopes bean = redEnvelopesList.get(index);

                final Bitmap randomRedPacket = RedEnvelopesLocation.get().getRandomRedPacket(bitmaps);

                long durationTimeMillis = getMaxMillis() - tempIntervalTimeMillis * index;
                //下一个红包的x方向位置
                int nextRedPacketX = RedEnvelopesLocation.get().getNextRedPacketX(dp10, screenWidth, redEnvelopesWidth);

                RedEnvelopesHelper redPacketHelper = RedEnvelopesHelper.produceRedPacket(index, getContext(), redEnvelopesWidth,
                        redEnvelopesWidth, clickRedPacketWidth, clickRedPacketHeight, nextRedPacketX, durationTimeMillis);

                if (!Utils.isEmpty(redPacketHelper)) {
                    redPacketHelper.setGoldDismissXOffset(goldDismissXOffset);
                    redPacketHelper.setGoldDismissYOffset(goldDismissYOffset);

                    redPacketHelper.setMoney(bean.getMoney());

                    redPacketHelper.setRedPacketLister(new RedEnvelopesHelper.RedPacketLister() {
                        @Override
                        public void clickRedPacket(RedEnvelopesHelper bean) {
                            if (Utils.isEmpty(sb)) {
                                sb = new StringBuffer();
                            }
                            sb.append(String.valueOf(bean.getIndex()));
                            sb.append(",");

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
                    if (Utils.isEmpty(redEnvelopesView)) {
                        return true;
                    }
                    redEnvelopesView.addRedPacketData(checkCount - 1, redPacketHelper);
                    redPacketHelper.startDown(containerHeight);

                    if (checkCount == 1) {
                        redEnvelopesView.startDraw();
                    }
                }

                if (checkCount >= redEnvelopesList.size() || isExit()) {
                    return true;
                }
                return false;
            }

            @Override
            public void onEnd() {

            }
        });
    }

    private void checkBitmap() {

        if (Utils.isEmptyAny(bitmap1, bitmap2, bitmap3, bitmap4)) {
            bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_14);
            bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_16);
            bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_19);
            bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_20);
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
        if (Utils.isEmpty(sb) || Utils.trimToEmpty(sb.toString())) {
            finish();
            return;
        }
        int lastIndexOf = sb.lastIndexOf(",");
        if (lastIndexOf != -1 && lastIndexOf == sb.length() - 1) {
            sb.deleteCharAt(lastIndexOf);
        }
    }

    public void finish() {
        if (inter != null) {
            inter.finish();
        }
    }

    //获取金币数量改变动画
    private void changeMoneyTextAnim() {
        if (Utils.isEmpty(valueAnim)) {
            valueAnim = ValueAnimator.ofInt(0, getMoney());
        }
        if (valueAnim.isRunning()) {
            valueAnim.cancel();
        }
        valueAnim = ValueAnimator.ofInt(showMoney, getMoney());
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                showMoney = animatedValue;
                if (!Utils.isEmpty(tvMoney)) {
                    tvMoney.setText(String.valueOf(showMoney));
                }
            }
        });
        valueAnim.setDuration(900);
        valueAnim.start();
    }


    //红包下落最长时间
    public int getMaxMillis() {
        return 3200;
    }

    //红包下落最短时间
    public int getMinMillis() {
        return 1400;
    }


    public void showOtherView() {
        if (Utils.isEmpty(flRedEnvelopesTop)) {
            return;
        }
        flRedEnvelopesTop.setVisibility(VISIBLE);
    }


    public boolean isExit() {
        Activity activity = null;
        if (!Utils.isEmpty(getContext()) && getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }
        return activity == null || activity.isDestroyed() || activity.isFinishing();
    }

}
