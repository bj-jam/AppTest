package com.app.test.cardview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.app.test.R;

/**
 * Created by jam on 17/5/9.
 *
 * @describe
 */

public class CardViewActivity extends Activity {
    protected TextView text1;
    protected TextView text2;
    protected TextView text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_card_view);
        initView();
    }

    private void initView() {
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        setText1();
        setText2();
    }


    private void setText1() {
        String html = "图片哈哈哈<img src='" + R.drawable.icon_pic5 + "'/>";
        Html.ImageGetter imageGetter = new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                // TODO Auto-generated method stub
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };
        CharSequence mCharSequence = Html.fromHtml(html, imageGetter, null);
        text1.setText(mCharSequence);
        text1.append("图片 ");
    }

    private void setText2() {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pic5);
        ImageSpan imgSpan = new ImageSpan(this, b);
        SpannableString spanString = new SpannableString("设置文字的前景色为淡蓝色");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        spanString.setSpan(colorSpan, 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanString.setSpan(imgSpan, 6, 7, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        text2.setText(spanString);
    }
}
