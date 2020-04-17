package com.app.test.flexbox;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.test.R;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lcx
 * Created at 2020.4.17
 * Describe:
 */
public class BoxView extends FrameLayout {

    private FitRectPack fitRectPack;
    private int maxHeight = 0;
    private int dp50;
    private int dp60;
    private int screenWidth;

    public BoxView(Context context) {
        super(context);
        init();
    }

    public BoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp50 = DensityUtil.dp2px(50);
        dp60 = DensityUtil.dp2px(60);
        screenWidth = DensityUtil.getScreenWidth(getContext());
    }

    private int[] image = {
            R.drawable.ic_01,
            R.drawable.ic_02,
            R.drawable.ic_03,
            R.drawable.ic_04,
            R.drawable.ic_05,
            R.drawable.ic_06,
            R.drawable.ic_07,
            R.drawable.ic_08,
            R.drawable.ic_09,
            R.drawable.ic_10,
            R.drawable.ic_11,
            R.drawable.ic_12,
            R.drawable.ic_13,
            R.drawable.ic_14,
            R.drawable.ic_15,
            R.drawable.ic_16,
            R.drawable.ic_17,
            R.drawable.ic_18,
            R.drawable.ic_19,
            R.drawable.ic_20,
            R.drawable.ic_21,
            R.drawable.ic_22,
            R.drawable.ic_23,
            R.drawable.ic_24,
            R.drawable.ic_25,
            R.drawable.ic_26,
            R.drawable.ic_27,
            R.drawable.ic_28,
            R.drawable.ic_29,
            R.drawable.ic_30,
            R.drawable.ic_31,
            R.drawable.ic_32,
            R.drawable.ic_33,
            R.drawable.ic_34,
            R.drawable.ic_35,
            R.drawable.ic_36,
            R.drawable.ic_37,
            R.drawable.ic_38,
            R.drawable.ic_39,
            R.drawable.ic_40,
            R.drawable.ic_41,
            R.drawable.ic_42,
            R.drawable.ic_43,
            R.drawable.ic_44,
            R.drawable.ic_45,
            R.drawable.ic_46
    };

    private List<BoxBean> getList() {
        Random random = new Random();
        List<BoxBean> list = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            BoxBean boxBean = new BoxBean();
            int imageIndex = random.nextInt(image.length);
            boxBean.id = image[imageIndex];
            list.add(boxBean);
        }
        return list;
    }

    public void setData() {
        initRect(getList());
    }


    private void initRect(List<BoxBean> dataList) {
        if (Utils.isEmpty(dataList)) {
            return;
        }
        int size = dataList.size();
        Random random = new Random();
        int allArea = (dp50 + DensityUtil.dp2px(25)) * (dp50 + DensityUtil.dp2px(25)) * size;
        fitRectPack = new FitRectPack(screenWidth - 20, allArea / screenWidth, false);

        for (int i = 0; i < size; i++) {
            int imageWidth = dp50 + DensityUtil.dp2px(random.nextInt(30));
            fitRectPack.insert(imageWidth, imageWidth, FitRectPack.FreeRectangleChoiceHeuristic.BestAreaFit);
        }

        for (int i = 0; i < size; i++) {
            int imageId = dataList.get(i).id;

            final View view = View.inflate(getContext(), R.layout.item_box_view, null);
            //view偏移的角度
            int angle = random.nextInt(41) - 20;
            ImageView showItem = view.findViewById(R.id.iv_box_item);
            ImageView itemStatus = view.findViewById(R.id.iv_box_item_status);

            view.setRotation(angle);
            view.setRotationX(0.5f);
            view.setRotationY(0.5f);

            itemStatus.setRotation(-angle);
            itemStatus.setRotationX(0.5f);
            itemStatus.setRotationY(0.5f);
            //这样取的话数组会出现越界的情况
            Rectangle rect = fitRectPack.usedRectangles.get(i);
            //设置单个view的大小
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(rect.width, rect.height);
            showItem.setLayoutParams(params);
            //设置状态view的大小和位置
            params = new FrameLayout.LayoutParams(rect.width > dp60 ? dp60 : rect.width, rect.height > dp60 ? dp60 : rect.height);
            params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            itemStatus.setLayoutParams(params);
            //加载资源
            Glide.with(getContext()).load(imageId).asBitmap().into(showItem);
            //设置整个view在父容器中的位置
            FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setX(rect.x + 10);
            view.setY(rect.y);
            view.setLayoutParams(itemParams);
            addView(view);
            maxHeight = Math.max(maxHeight, rect.y);
        }
        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight + dp50 + dp50);
        Log.e("jam", "initRect: " + (maxHeight + dp50 + dp50));
        setLayoutParams(layoutParams);
    }
}
