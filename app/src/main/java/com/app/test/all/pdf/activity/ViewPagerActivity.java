package com.app.test.all.pdf.activity;

import android.app.Activity;
import android.os.Environment;

import androidx.viewpager.widget.ViewPager;

import com.app.test.R;
import com.app.test.all.pdf.adapter.PDFPagerAdapter;

public class ViewPagerActivity extends Activity {
    ViewPager pdfViewPager;
    PDFPagerAdapter adapter;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        pdfViewPager = findViewById(R.id.viewPager);

        adapter = new PDFPagerAdapter(this, Environment.getExternalStorageDirectory() + "/test.pdf");
        pdfViewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.close();
    }


}
