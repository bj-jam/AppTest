package com.app.test.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.app.test.util.DensityUtil;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by able on 2019/9/22.
 * description:
 */
public class CustomRel extends RelativeLayout {
    private int dp20 = DensityUtil.dp2px(20);
    private int dp15 = DensityUtil.dp2px(15);
    private int dp10 = DensityUtil.dp2px(10);

    public CustomRel(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
        init();
    }

    public CustomRel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        init();

    }

    public CustomRel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
        init();

    }

    private void init() {
        RoundedImageView pic1 = new RoundedImageView(getContext());

        addView(pic1, new RecyclerView.LayoutParams(dp20, dp20));
        RoundedImageView pic2 = new RoundedImageView(getContext());
        RoundedImageView pic3 = new RoundedImageView(getContext());
        RoundedImageView pic4 = new RoundedImageView(getContext());
        RoundedImageView pic5 = new RoundedImageView(getContext());
    }


}
