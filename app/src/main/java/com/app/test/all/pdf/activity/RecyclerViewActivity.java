package com.app.test.all.pdf.activity;

import android.app.Activity;
import android.os.Environment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.test.R;
import com.app.test.all.pdf.adapter.PDFListAdapter;

public class RecyclerViewActivity extends Activity {
    RecyclerView pdfRecyclerView;
    PDFListAdapter adapter;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfRecyclerView = findViewById(R.id.listView);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PDFListAdapter(this, Environment.getExternalStorageDirectory() + "/test.pdf");
        pdfRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.close();
    }


}
