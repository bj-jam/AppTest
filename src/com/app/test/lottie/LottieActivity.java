package com.app.test.lottie;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.app.test.R;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
public class LottieActivity extends Activity {
    private LottieAnimationView ivLottie;
    private LottieAnimationView ivLottie1;
    private LottieAnimationView ivLottie3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        ivLottie = (LottieAnimationView) findViewById(R.id.iv_lottie);
        ivLottie1 = (LottieAnimationView) findViewById(R.id.iv_lottie1);
        ivLottie3 = (LottieAnimationView) findViewById(R.id.iv_lottie2);
        findViewById(R.id.b_start_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });

        ivLottie.useHardwareAcceleration(true);
        ivLottie.setImageAssetsFolder("drink/images");
        ivLottie.setAnimation("drink/drink_water.json");

        ivLottie1.useHardwareAcceleration(true);
        ivLottie1.setImageAssetsFolder("gld/images");
        ivLottie1.setAnimation("gld/loading.json");

        ivLottie3.useHardwareAcceleration(true);
        ivLottie3.setImageAssetsFolder("back/images");
        ivLottie3.setAnimation("back/maopu.json");


    }

    private void startAnim() {
        ivLottie.playAnimation();
        ivLottie.removeAllAnimatorListeners();
        ivLottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                drinkWaterEnd();
            }
        });

        ivLottie1.playAnimation();
        ivLottie1.removeAllAnimatorListeners();
        ivLottie1.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });

        ivLottie3.playAnimation();
        ivLottie3.setRepeatCount(-1);
        ivLottie3.removeAllAnimatorListeners();
        ivLottie3.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    public void drinkWaterEnd() {
        if (!Utils.isEmpty(ivLottie)) {
            ivLottie.setProgress(0);
        }
    }
}
