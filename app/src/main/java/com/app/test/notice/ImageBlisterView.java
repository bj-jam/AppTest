package com.app.test.notice;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.test.R;
import com.app.test.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by able on 2019/2/28.
 * description:
 */
public class ImageBlisterView extends RelativeLayout implements Handler.Callback {
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private Timer mTimer;
    private Handler handler;
    private Integer[] itemPic;
    private List<Integer> itemList;

    private int currentId;

    private List<AnimDataBean> dataList = new ArrayList<>();
    private List<ImageView> viewList = new ArrayList<>();


    public ImageBlisterView(Context context) {
        super(context);
        initView();
        initData();
    }

    public ImageBlisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    public ImageBlisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initData();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_blister_image, this);
        imageView = findViewById(R.id.imageView);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView1.setVisibility(GONE);
        imageView2.setVisibility(GONE);
        imageView3.setVisibility(GONE);
        imageView4.setVisibility(GONE);
        imageView5.setVisibility(GONE);

    }

    private void initData() {
        handler = new Handler(this);
        dataList = new ArrayList<>();
        viewList = new ArrayList<>();
        itemList = new ArrayList<>();
        itemPic = new Integer[]{R.drawable.icon_pic1, R.drawable.icon_pic2, R.drawable.icon_pic3,
                R.drawable.icon_pic4, R.drawable.icon_pic5, R.drawable.icon_pic6, R.drawable.icon_pic7,
                R.drawable.icon_pic8, R.drawable.icon_pic9, R.drawable.icon_pic10, R.drawable.icon_pic11,
                R.drawable.icon_pic12, R.drawable.icon_pic13, R.drawable.icon_pic14, R.drawable.icon_pic15,
                R.drawable.icon_pic16, R.drawable.icon_pic17, R.drawable.icon_pic18, R.drawable.icon_pic19,
                R.drawable.icon_pic20};
        viewList.add(imageView1);
        viewList.add(imageView2);
        viewList.add(imageView3);
        viewList.add(imageView4);
        viewList.add(imageView5);

        for (int i = 0; i < 6; i++) {
            AnimDataBean dataBean = null;
            if (i == 0) {
                dataBean = new AnimDataBean(x1, 3500);
            } else if (i == 1) {
                dataBean = new AnimDataBean(x2, 2500);
            } else if (i == 2) {
                dataBean = new AnimDataBean(x3, 2000);
            } else if (i == 3) {
                dataBean = new AnimDataBean(x4, 1500);
            } else if (i == 4) {
                dataBean = new AnimDataBean(x5, 2700);
            } else if (i == 5) {
                dataBean = new AnimDataBean(x6, 3000);
            }
            dataList.add(dataBean);
        }

        mTimer = new Timer();
        mTimer.schedule(task, 1000, 1500);

    }


    float[] x1 = new float[]{0, 20f, 0, -20f, 0};
    float[] y1 = new float[]{0, - DensityUtil.dp2px( 50)};

    float[] x2 = new float[]{0, -20f, 0, 20f, 0};

    float[] x3 = new float[]{0, 16f, 0, -16f, 0};
    float[] x4 = new float[]{0, -16f, 0, 16f, 0};

    float[] x5 = new float[]{0, 10f, 0, -10f, 0};

    float[] x6 = new float[]{0, -4f, 0, 4f, 0};


    private void doAnim(final ImageView view, AnimDataBean animBean) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", animBean.x1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", y1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 0f);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.1f);

        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX, translationY, scaleX, scaleY, anim); //设置动画
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(animBean.time);  //设置动画时间
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (viewList != null) {
                    viewList.add(view);
                    view.setVisibility(GONE);
                }
                if (itemList != null && itemList.size() > 0) {
                    itemList.remove(0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start(); //启动
    }


    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }
    };

    private void doAnim() {
        if (dataList != null && viewList != null && viewList.size() > 0) {
            AnimDataBean dataBean = null;
            ImageView animView = null;
            int viewPosition = new Random().nextInt(viewList.size());
            int datePosition = new Random().nextInt(dataList.size());
            if (viewPosition >= 0 && viewPosition < viewList.size()) {
                animView = viewList.get(viewPosition);
                viewList.remove(animView);
            }
            if (datePosition >= 0 && datePosition < dataList.size()) {//防止数组越界
                dataBean = dataList.get(datePosition);
            }
            if (animView != null && dataBean != null) {
                animView.setVisibility(VISIBLE);
                if (currentId < itemPic.length) {
                    animView.setImageResource(itemPic[currentId]);
                } else {
                    animView.setImageResource(itemPic[0]);
                }
                doAnim(animView, dataBean);
                currentId = getPosition();
                itemList.add(currentId);
                if (currentId < itemPic.length) {
                    imageView.setImageResource(itemPic[currentId]);
                } else {
                    imageView.setImageResource(itemPic[0]);
                }
            }
        }
    }


    private int getPosition() {
        Integer i = new Random().nextInt(20);
        if (itemList.contains(i)) {
            for (int b = 1; b < 8; b++) {
                i += b;
                if (i > 19) {//防止出现大于图片的个数自动选择第一个引起重复
                    i = 0;
                }
                if (!itemList.contains(i)) {
                    return i;
                }
            }
        }
        return i;
    }


    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg != null && msg.what == 1) {
            doAnim();
        }
        return false;
    }


    public static class AnimDataBean {
        public float[] x1;
        public int time;

        public AnimDataBean(float[] x, int time) {
            this.x1 = x;
            this.time = time;
        }
    }
}
