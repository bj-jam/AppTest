package com.app.test.titanic;

import android.app.Activity;
import android.os.Bundle;

import com.app.test.R;


public class TitanicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titanic);

        TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "111111.ttf"));

        // start animation
        new Titanic().start(tv);
    }

}
