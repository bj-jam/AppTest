package com.app.test.flexbox;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.app.test.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.Arrays;

/**
 * @author lcx
 * Created at 2020.4.1
 * Describe:
 */
public class FlexboxManagerView extends FrameLayout {
    private RecyclerView recyclerView;

    private Integer[] imgs = {
            R.drawable.share_fb,
            R.drawable.ic_game_gold_10,
            R.drawable.icon_pic3,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.icon_pic3,
            R.drawable.share_kongjian,
            R.drawable.ic_game_gold_10,
            R.drawable.icon_pic3,
            R.drawable.share_pyq,

            R.drawable.share_tw,
            R.drawable.ic_game_gold_00,
            R.drawable.icon_pic7,
            R.drawable.ic_game_gold_02,
            R.drawable.icon_pic3,
            R.drawable.ic_game_gold_04,
            R.drawable.icon_pic5,
            R.drawable.ic_game_gold_06,
            R.drawable.ic_game_gold_07,
            R.drawable.icon_pic8,
            R.drawable.ic_game_gold_09,
            R.drawable.share_wechat,
            R.drawable.share_weibo,
            R.drawable.icon_pic5,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.icon_pic5,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo, R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,

            R.drawable.icon_pic5,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.share_weibo,
            R.drawable.icon_pic5,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.icon_pic5,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.ic_game_gold_11,
            R.drawable.share_qq,
            R.drawable.ic_game_gold_12,
            R.drawable.ic_game_gold_13,
            R.drawable.share_qq,
            R.drawable.ic_game_gold_14,
            R.drawable.share_qq,

            R.drawable.ic_game_gold_15,
            R.drawable.icon_pic5,
            R.drawable.ic_game_gold_17,
            R.drawable.share_wechat,
            R.drawable.share_weibo
    };

    public FlexboxManagerView(Context context) {
        super(context);
        initView();
    }

    public FlexboxManagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlexboxManagerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView() {
        View.inflate(getContext(), R.layout.view_flexbox_manager, this);
        recyclerView = findViewById(R.id.rv_list);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP); //设置是否换行
        layoutManager.setFlexDirection(FlexDirection.ROW); // 设置主轴排列方式
        layoutManager.setAlignItems(AlignItems.STRETCH);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ViewAdapter(Arrays.asList(imgs)));
    }
}
