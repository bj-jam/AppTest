package com.app.ui;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
//    private ImageView imageView_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
//        imageView_1 = findViewById(R.id.imageView_1);
        imageView.setOnClickListener(new View.OnClickListener() {
            //            @TargetApi(Build.VERSION_CODES.M)
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AnimatedVectorDrawable animatedVectorDrawable
                        = (AnimatedVectorDrawable) imageView.getDrawable();
                animatedVectorDrawable.start();
//                animatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
//                    @Override
//                    public void onAnimationEnd(Drawable drawable) {
//                        super.onAnimationEnd(drawable);
//                        AnimatedVectorDrawable animatedVectorDrawable
//                                = (AnimatedVectorDrawable) imageView_1.getDrawable();
//                        animatedVectorDrawable.start();
//                    }
//
//                    @Override
//                    public void onAnimationStart(Drawable drawable) {
//                        super.onAnimationStart(drawable);
//                    }
//                });
            }
        });
    }
}
