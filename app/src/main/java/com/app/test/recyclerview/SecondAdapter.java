package com.app.test.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.test.R;

import java.util.List;

/**
 * Created by dell on 2017/1/3.
 */

public class SecondAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    List<RecyclerViewActivity.CourseInfo> list;
    Context context;
    RecycleItemClickListener mItemClickListener;
    private int type;

    public SecondAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<RecyclerViewActivity.CourseInfo> list, int type) {
        this.type = type;
        this.list = list;
    }

    public void setItemClickListener(RecycleItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (type == 0) {
            itemView = LayoutInflater.from(context).inflate(R.layout.tree_down_item, parent, false);
        } else if (type == 1) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        } else if (type == 2) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_recyclerview1, parent, false);
        }
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        if (mItemClickListener != null) {
            itemView.setOnClickListener(this);
        }
        return viewHolder;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((RecyclerViewActivity.CourseInfo) v.getTag());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewActivity.CourseInfo courseInfo = list.get(position);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.itemView.setTag(courseInfo);
        viewHolder.title.setText(courseInfo.name);
        viewHolder.content.setText(courseInfo.price);
        Glide.with(context).load("http://image.test.com/download/upload/course/file/24/981a42a8ddae48a2866506a46a90f65a.jpg").
                placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(viewHolder.picture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface RecycleItemClickListener {
        void onItemClick(RecyclerViewActivity.CourseInfo courseInfo);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView title;
        public TextView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.image_view);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

}
