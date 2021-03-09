package com.app.test.lottie;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.app.test.R;

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



    }

    private void startAnim() {

    }


}
