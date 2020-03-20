package com.app.test.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.app.test.R;
import com.app.test.utils.DisplayUtil;
import com.app.test.view.TeachTalkTogetherView;

/**
 *
 */

public class AnimActivity extends Activity {
    protected ImageView ivIsVip;
    protected ImageView toOpenVipImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_anim);

        initView();

        initLineHeadAnima();

        ((TeachTalkTogetherView) findViewById(R.id.teach)).startAnima();
    }

    private void initLineHeadAnima() {
        final float offset = DisplayUtil.dip2px(this, 15);
//        final int offset=DisplayUtil.dip2px(this,15);


        final ViewGroup container = findViewById(R.id.container);

        ImageView pic0 = findViewById(R.id.pic0);
        ImageView pic1 = findViewById(R.id.pic1);
        ImageView pic2 = findViewById(R.id.pic2);
        ImageView pic3 = findViewById(R.id.pic3);
        ImageView pic4 = findViewById(R.id.pic4);


        pic0.setZ(4);
        pic1.setZ(3);
        pic2.setZ(2);
        pic3.setZ(1);
        pic4.setZ(0);


        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, offset);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = 0; i < container.getChildCount(); i++) {

                    View chilidView = container.getChildAt(i);
                    float order = container.getChildCount() - 1 - chilidView.getZ();//0-4

                    if (order >= 1 && order <= 3) {
                        float transX = (float) animation.getAnimatedValue() + (order - 1) * offset;
                        chilidView.setTranslationX(transX);
                    }

                    if (order == 0) {
                        //缩放动画
                        chilidView.setScaleX(1.0f * ((float) animation.getAnimatedValue() / offset));
                        chilidView.setScaleY(1.0f * ((float) animation.getAnimatedValue() / offset));
                    } else if (order == 3) {
                        //缩放动画
//                        chilidView.setScaleX(1.0f * (1.0f - (float)animation.getAnimatedValue() / offset));
//                        chilidView.setScaleY(1.0f * (1.0f - (float)animation.getAnimatedValue() / offset));
                    }

                    if (animation.getCurrentPlayTime() == 3000) {
                        //动画结束
                        if (order == 3) {
                            chilidView.setScaleX(0);
                            chilidView.setScaleY(0);
                            chilidView.setZ(4);
                            chilidView.setTranslationX(0);
                        }

                        if (order == 2 || order == 1) {
                            chilidView.setZ(chilidView.getZ() - 1);
                        }
                    }
                }
            }
        });


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                valueAnimator.start();
            }
        });

    }

    private void initView() {
        ivIsVip = (ImageView) findViewById(R.id.iv_is_vip);
        toOpenVipImg = (ImageView) findViewById(R.id.to_open_vip_img);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(toOpenVipImg, "translationY", 0F, -50F, 0F);
        animator1.setDuration(1500);
        animator1.setRepeatCount(-1);
        animator1.start();

        AnimatorSet set = new AnimatorSet();
        set.play(animator1);
        set.start();

    }


}

