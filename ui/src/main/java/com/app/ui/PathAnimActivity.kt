package com.app.ui

import android.app.Activity
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class PathAnimActivity : Activity() {
    private var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_anim)
        imageView = findViewById(R.id.imageView)
        imageView?.setOnClickListener(View.OnClickListener {
            val animatedVectorDrawable = imageView?.drawable as AnimatedVectorDrawable
            animatedVectorDrawable.start()
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
        })
    }
}