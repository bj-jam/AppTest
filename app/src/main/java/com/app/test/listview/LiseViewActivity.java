package com.app.test.listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class LiseViewActivity extends Activity {
    private List<String> list;
    private ListView mListView;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        initDate();
        initView();
    }

    private void initDate() {
        adapter = new ListViewAdapter();
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + "<----listView");
        }
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(adapter);
    }

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder h;
            if (convertView == null) {
                h = new ViewHolder();
                convertView = View.inflate(LiseViewActivity.this, R.layout.view_list_view_item, null);
                h.infoTv = (TextView) convertView.findViewById(R.id.infoTv);
                convertView.setTag(h);
            } else {
                h = (ViewHolder) convertView.getTag();
            }
            h.infoTv.setText(list.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView infoTv;
    }
}
