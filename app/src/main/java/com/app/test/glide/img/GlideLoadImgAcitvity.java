package com.app.test.glide.img;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.app.test.R;
import com.app.test.view.AnimView;

public class GlideLoadImgAcitvity extends Activity {
    private ImageView imageView1;
    private AnimView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_load_img);
        initView();
        initDate();

    }

    private void initDate() {
        // TODO Auto-generated method stub

    }

    private void initView() {
        // TODO Auto-generated method stub
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (AnimView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.startAnim();
            }
        });
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.stopAnim();
            }
        });
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        Glide.with(this)
                .load("http://image.test.com/download/upload/course/file/24/981a42a8ddae48a2866506a46a90f65a.jpg")
                .placeholder(R.drawable.code6)
                .transform(new GlideCircleTransform(this)).into(imageView1);
        Glide.with(this)
                .load("http://image.test.com/download/upload/course/file/24/981a42a8ddae48a2866506a46a90f65a.jpg")
                .transform(new GlideRoundTransform(this, 20)).into(imageView2);
        Glide.with(this)
                .load("http://image.test.com/download/upload/createcourse/image/a73a2f90-a33c-42b4-80a1-19afcd2ee292.jpg")
                .transform(new GlideRoundTransform(this)).into(imageView3);
        Glide.with(this)
                .load("http://image.test.com/download/upload/createcourse/image/a73a2f90-a33c-42b4-80a1-19afcd2ee292.jpg")
                .transform(new GlideRoundTransform(this)).into(imageView5);
        Glide.with(this).load(R.drawable.load).into(imageView4);

    }

}
