package com.app.test.redenveloped;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.IntDef;
import android.view.animation.LinearInterpolator;

import com.app.test.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RedEnvelopesHelper {

    public interface RedPacketInter {
        void clickRedPacket(RedEnvelopesHelper bean);

        Bitmap getNormalBitmap();

        Bitmap getClickBitmap();

        Bitmap getGoldBitmap();
    }

    private RedPacketInter redPacketInter;

    public RedPacketInter getRedPacketInter() {
        return redPacketInter;
    }

    public void setRedPacketInter(RedPacketInter redPacketInter) {
        this.redPacketInter = redPacketInter;
    }

    private RedEnvelopes redPacketBean;
    private Context context;
    private Integer index = -1;
    private int money;
    //x,y代表当前红包图片绘制位置
    private int x;
    private int y;
    //初始化时的Y坐标
    private int initY;
    //点击时的x,y坐标，防止动画执行刷新x，y坐标值，在做逻辑判断时提前记录
    private int currentX;
    private int currentY;
    private boolean canClick = true;
    private int normalWidth;
    private int normalHeight;
    private int clickBitmapWidth;
    private int clickBitmapHeight;

    private int goldBitmapWidth;
    private int goldBitmapHeight;

    private long durationTime = 3000;
    private long showDismissTime = 500;
    //金币移动动画执行时间
    private int moneyMoveTime = 800;

    private ValueAnimator valueAnimator;
    private boolean isEnd;
    private Matrix matrix;
    //3：红包下落,2:显示被点击红包，1显示金币，0：消失
    private int redPacketStatus = 3;


    public static final int status_3 = 3;
    public static final int status_2 = 2;
    public static final int status_1 = 1;
    public static final int status_0 = 0;

    @IntDef({status_3, status_2, status_1, status_0})
    @Retention(RetentionPolicy.SOURCE)
    public @interface status {
    }

    //金币消失x方向偏移量
    private int goldDismissXOffset;
    //金币消失y方向偏移量
    private int goldDismissYOffset;

    public void setGoldDismissXOffset(int goldDismissXOffset) {
        this.goldDismissXOffset = goldDismissXOffset;
    }

    public void setGoldDismissYOffset(int goldDismissYOffset) {
        this.goldDismissYOffset = goldDismissYOffset;
    }

    public int getGoldDismissXOffset() {
        return goldDismissXOffset;
    }

    public int getGoldDismissYOffset() {
        return goldDismissYOffset;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRedPacketStatus() {
        return redPacketStatus;
    }

    public void setRedPacketStatus(@status int redPacketStatus) {
        this.redPacketStatus = redPacketStatus;
    }

    public RedEnvelopesHelper(Context context) {
        this.context = context;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setInitY(int initY) {
        this.initY = initY;
        setY(initY);
    }

    public void saveCurrentXY() {
        currentX = getX();
        currentY = getY();
    }

    public Bitmap getBitmap() {
        return getRedPacketInter() == null ? null : getRedPacketInter().getNormalBitmap();
    }

    public Bitmap getClickBitmap() {
        return getRedPacketInter() == null ? null : getRedPacketInter().getClickBitmap();
    }

    public Bitmap getGoldBitmap() {
        return getRedPacketInter() == null ? null : getRedPacketInter().getGoldBitmap();
    }

    public int getNormalWidth() {
        return normalWidth;
    }

    public void setNormalWidth(int width) {
        this.normalWidth = width;
    }

    public int getNormalHeight() {
        return normalHeight;
    }

    public void setNormalHeight(int height) {
        this.normalHeight = height;
    }

    public int getClickBitmapWidth() {
        return clickBitmapWidth;
    }

    public void setClickBitmapWidth(int clickBitmapWidth) {
        this.clickBitmapWidth = clickBitmapWidth;
    }

    public int getClickBitmapHeight() {
        return clickBitmapHeight;
    }

    public void setClickBitmapHeight(int clickBitmapHeight) {
        this.clickBitmapHeight = clickBitmapHeight;
    }

    public int getGoldBitmapWidth() {
        return goldBitmapWidth;
    }

    public void setGoldBitmapWidth(int goldBitmapWidth) {
        this.goldBitmapWidth = goldBitmapWidth;
    }

    public int getGoldBitmapHeight() {
        return goldBitmapHeight;
    }

    public void setGoldBitmapHeight(int goldBitmapHeight) {
        this.goldBitmapHeight = goldBitmapHeight;
    }

    public Matrix getMatrix() {
        return matrix == null ? new Matrix() : matrix;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public boolean constant(int x, int y) {
        if (x >= currentX && x <= currentX + getNormalWidth() && y >= currentY && y <= getNormalHeight() + currentY) {
            return true;
        }
        return false;
    }

    public void setDurationTime(long time) {
        if (time <= 0) {
            time = 3000;
        }
        durationTime = time;
    }


    public void startDown(int containerHeight) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initY, containerHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getRedPacketStatus() != status_3) {
                    return;
                }
                float animatedValue = (Float) animation.getAnimatedValue();
                setY((int) animatedValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setRedPacketStatus(status_3);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (canClick) {
                    setEnd(true);
                }
            }
        });
        valueAnimator.setDuration(durationTime);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void startChangeToRedPacket() {
        //防止重复点击
        canClick = false;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        final int scaleX;
        final int scaleY;
        final int translateX;

        scaleX = getClickBitmapWidth() / 2;
        scaleY = getClickBitmapHeight() / 2;
        translateX = scaleX - getNormalWidth() / 2;

        valueAnimator = ValueAnimator.ofFloat(0.8f, 1.2f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getRedPacketStatus() != status_2) {
                    return;
                }
                if (matrix == null) {
                    matrix = new Matrix();
                } else {
                    matrix.reset();
                }
                float animatedValue = (float) animation.getAnimatedValue();
                matrix.setTranslate(getX() - translateX, getY() - 20);
                matrix.preScale(animatedValue, animatedValue, scaleX, scaleY);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setRedPacketStatus(status_2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setRedPacketStatus(status_1);
                startChangeToGold();
            }
        });
        valueAnimator.setDuration(showDismissTime);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void startChangeToGold() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_redpacket_gold_active);
        final int scaleX;
        final int scaleY;
        if (bitmap == null) {
            scaleX = 150 / 2;
            scaleY = 150 / 2;
        } else {
            scaleX = bitmap.getWidth() / 2;
            scaleY = bitmap.getHeight() / 2;
        }
//        setBitmap(bitmap);
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("x", getX(), getGoldDismissXOffset() - scaleX / 2);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("y", getY(), getGoldDismissYOffset());
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("scale", 0.8f, 0.5f);
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(holder1, holder2, holder3);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getRedPacketStatus() != status_1) {
                    return;
                }
                float x = (float) animation.getAnimatedValue("x");
                float y = (float) animation.getAnimatedValue("y");
                float scale = (float) animation.getAnimatedValue("scale");
                if (matrix == null) {
                    matrix = new Matrix();
                } else {
                    matrix.reset();
                }
                matrix.setTranslate(getX() + scaleX / 3, getY());
                matrix.preScale(scale, scale, scaleX, scaleY);
                setX((int) x);
                setY((int) y);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setRedPacketStatus(status_1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setRedPacketStatus(status_0);
                setEnd(true);
            }
        });
        valueAnimator.setDuration(moneyMoveTime);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    public void clickRedPacket() {
        if (getRedPacketInter() != null) {
            getRedPacketInter().clickRedPacket(this);
        }
        startChangeToRedPacket();
    }


    public static RedEnvelopesHelper produceRedPacket(int index, Context context, int normalBitmapWidth, int normalBitmapHeight, int clickBitmapWidth, int clickBitmapHeight, int redPacketX, long durationTimeMillis) {
        if (context == null) {
            return null;
        }
        RedEnvelopesHelper redPacket = new RedEnvelopesHelper(context);
//        int i = getMaxMillis()- temp* (checkCount - 1);
        redPacket.setDurationTime(durationTimeMillis);
        redPacket.setIndex(index);

        redPacket.setNormalWidth(normalBitmapWidth);
        redPacket.setNormalHeight(normalBitmapHeight);

        redPacket.setClickBitmapWidth(clickBitmapWidth);
        redPacket.setClickBitmapHeight(clickBitmapHeight);


//            redPacket.setBitmap(redPacketBitmap);


        redPacket.setX(redPacketX);
        redPacket.setInitY(-redPacket.getNormalHeight());

//        int nextRedPacketX = getNextRedPacketX(activity,screenWidth, redPacket.getNormalWidth());
//        rpv.addRedPacketData(checkCount-1,redPacket);
//        redPacket.startDown(RedEnvelopesHelper.getScreenHeight(activity));
        return redPacket;
    }


}
