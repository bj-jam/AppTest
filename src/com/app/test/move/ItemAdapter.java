package com.app.test.move;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
public class ItemAdapter extends BaseAdapter implements View.OnClickListener {
    List<String> data = new ArrayList<>();
    Context mContext;

    public ItemAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 30; i++) {
            data.add("item+" + i);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_move, parent, false);
            convertView.setTag(new ViewH(convertView));
        }
        ViewH holder = (ViewH) convertView.getTag();
        holder.tv.setText(data.get(position));
        holder.img.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.add(v);
        }
    }

    private AddClickListener mListener;

    public void setListener(AddClickListener listener) {
        mListener = listener;
    }

    public interface AddClickListener {
        void add(View v);
    }

    public static class ViewH {
        private ImageView img;
        private TextView tv;

        public ViewH(View view) {
            img = ((ImageView) view.findViewById(R.id.item_img));
            tv = ((TextView) view.findViewById(R.id.item_text));
        }
    }
}
