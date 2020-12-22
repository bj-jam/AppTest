/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.app.test.all.pdf.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.test.R;

import java.io.IOException;


public class PDFPagerAdapter extends PagerAdapter {

    protected String pdfPath;
    protected Context context;
    protected LayoutInflater inflater;
    PDFManager pdfManager;


    public PDFPagerAdapter(Context context, String pdfPath) {
        this.pdfPath = pdfPath;
        this.context = context;

        init();
    }


    /**
     * This constructor was added for those who want to customize ViewPager's offScreenSize attr
     */
    public PDFPagerAdapter(Context context, String pdfPath, int offScreenSize) {
        this.pdfPath = pdfPath;
        this.context = context;

        init();
    }


    protected void init() {
        try {
            pdfManager = new PDFManager(context, pdfPath);
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = inflater.inflate(R.layout.item_pdf_view, container, false);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);

        if (pdfManager == null || getCount() < position) {
            return v;
        }
        Bitmap bitmap = pdfManager.getBitmap(position);
        iv.setImageBitmap(bitmap);
        container.addView(v, 0);
        return v;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }


    public void close() {
        if (pdfManager != null) {
            pdfManager.close();
        }
    }

    @Override
    public int getCount() {
        return pdfManager != null ? pdfManager.getCount() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }
}
