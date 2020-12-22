package com.app.test.all.pdf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020/12/22
 * Describe:
 */
public class PDFMainActivity extends Activity implements android.view.View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_main);
    }

    @Override
    public void onClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.tv_list:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
            case R.id.tv_viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
        }
    }
}
