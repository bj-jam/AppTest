package com.app.test.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class AnimUtils {
    private IAnimListener mAnimListener;
    private ImageView imageView;
    private int[] frameResArray;
    // 每帧动画的播放间隔数组
    private int[] durations;
    // 下一遍动画播放的延迟时间
    private int delay;
    //最后一帧
    private int lastFrame;
    //是否一下次
    private boolean isNext;
    //是否一暂停
    private boolean isPause;
    private int currentFrame;

    private int repeatCount;
    private int currentRepeatCount;
    private boolean isHasStarted = false;

    private AnimUtils() {
        super();
    }

    public AnimUtils(Config config) {
        if (Utils.isEmpty(config)) {
            throw new IllegalArgumentException("config maybe null!!!");
        }
        this.imageView = config.iv;
        this.durations = config.durations;
        this.frameResArray = config.frameResArray;
        if (Utils.isEmpty(imageView) || Utils.isEmpty(durations) || Utils.isEmpty(frameResArray)) {
            throw new IllegalArgumentException("imageView,durations,frameResArray maybe null!!!");
        }
        this.lastFrame = frameResArray.length - 1;

        this.delay = config.delay;
        this.repeatCount = config.repeatCount;
        if (config.isAutoStart) {
            playByDurations(0);
        }
    }


    private void playByDurations(final int i) {
        if (Utils.isEmpty(imageView) || Utils.isEmpty(frameResArray) || Utils.isEmpty(durations)) {
            return;
        }
        imageView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isPause) {
                    currentFrame = i;
                    return;
                }
                isHasStarted = true;
                if (0 == i) {
                    if (mAnimListener != null) {
                        mAnimListener.onAnimStart();
                    }
                }
                setImageDrawable(imageView, frameResArray[i]);
                if (i == lastFrame) {
                    if (repeatCount < 0 || currentRepeatCount < repeatCount) {
                        if (mAnimListener != null) {
                            mAnimListener.onAnimRepeat();
                        }
                        isNext = true;
                        playByDurations(0);
                    } else {
                        if (mAnimListener != null) {
                            mAnimListener.onAnimEnd();
                        }
                    }
                    ++currentRepeatCount;
                } else {
                    playByDurations(i + 1);
                }
            }
        }, isNext && delay > 0 ? delay : durations[i]);

    }

    public static void setImageDrawable(ImageView imageView, int resId) {
        if (Utils.isEmpty(imageView)) {
            return;
        }
        Drawable drawable = null;
        try {
            drawable = imageView.getResources().getDrawable(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
    }


    public void release() {
        pause();
        currentRepeatCount = 0;
        currentFrame = 0;
        isNext = false;
    }

    public void pause() {
        this.isPause = true;
    }

    public void start() {
        if (!isPause && isHasStarted) {
            return;
        }
        isPause = false;
        playByDurations(0);
    }


    public void restartAnimation() {
        isPause = false;
        currentFrame = 0;
        playByDurations(currentFrame);
    }

    public void continueAnimation() {
        if (!isPause) {
            return;
        }
        isPause = false;
        playByDurations(currentFrame);
    }

    public boolean isPause() {
        return this.isPause;
    }

    public void setAnimationListener(IAnimListener listener) {
        this.mAnimListener = listener;
    }

    public static class AnimListener implements IAnimListener {

        @Override
        public void onAnimStart() {

        }

        @Override
        public void onAnimEnd() {

        }

        @Override
        public void onAnimRepeat() {

        }
    }

    public interface IAnimListener {
        void onAnimStart();

        void onAnimEnd();

        void onAnimRepeat();
    }

    public static class Config {
        /**
         * 播放动画的控件
         */
        private ImageView iv;
        /**
         * 播放的图片数组
         */
        private int[] frameResArray;
        /**
         * 每帧动画的播放间隔(毫秒)
         */
        private int duration;
        /**
         * 每帧动画的播放间隔(毫秒)
         */
        private int[] durations;
        /**
         * 首次播放延时
         */
        private int delay;
        /**
         * 循环播放次数 (-1:无限循环)
         */
        private int repeatCount;
        /**
         * 是否自动播放
         */
        private boolean isAutoStart;

        public Config setImageView(ImageView iv) {
            this.iv = iv;
            return this;
        }

        public Config setFrameResArray(int[] frameResArray) {
            this.frameResArray = frameResArray;
            return this;
        }

        public Config setDuration(int duration) {
            this.duration = duration;
            if (Utils.isEmpty(frameResArray)) {
                throw new IllegalArgumentException("frameResArray must config before duration!!!");
            }
            int length = frameResArray.length;
            int[] durations = new int[length];
            for (int i = 0; i < length; i++) {
                durations[i] = duration;
            }
            this.durations = durations;
            return this;
        }

        public Config setDurations(int[] durations) {
            this.durations = durations;
            return this;
        }

        public Config setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Config setRepeatCount(int repeatCount) {
            this.repeatCount = repeatCount;
            return this;
        }

        public Config setAutoStart(boolean autoStart) {
            isAutoStart = autoStart;
            return this;
        }

        public AnimUtils build() {
            return new AnimUtils(this);
        }
    }

}
