package com.app.test.viewpager.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.test.R;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by dengzhaoxuan on 2017/3/22.
 */

public class HorizonAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mdata;
    private VerticalViewPager mViewPager;

    public interface OnImgClickListener {
        void onImgClick(View view);
    }

    private OnImgClickListener mOnImageClikListener;

    public void setmOnImageClikListener(OnImgClickListener mOnImageClikListener) {
        this.mOnImageClikListener = mOnImageClikListener;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnImageClikListener.onImgClick(v);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    };

    public HorizonAdapter(Context context, List<String> data, VerticalViewPager viewPager) {
        mContext = context;
        mdata = data;
        mViewPager = viewPager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager1, null);
        ImageView img = (ImageView) view.findViewById(R.id.iv_viewpager);
        Glide.with(mContext)
                .load(mdata.get(position))
                .into(img);

        img.setOnClickListener(mOnClickListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {

        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }
}
