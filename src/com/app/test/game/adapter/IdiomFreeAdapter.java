package com.app.test.game.adapter;


import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.R;
import com.app.test.game.bean.SuperType;
import com.app.test.util.DensityUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class IdiomFreeAdapter extends BaseQuickAdapter<SuperType, BaseViewHolder> {
    private int itemWidth = 0;

    public IdiomFreeAdapter(@Nullable List<SuperType> data) {
        super(R.layout.item_idiom_free, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SuperType data) {
        helper.setText(R.id.tv_select_idiom_free_word, data.getTitle());
        View flFreeWord = helper.getView(R.id.fl_free_word);
        setItemWidth(flFreeWord);
        flFreeWord.setVisibility(data.isSelected() ? View.INVISIBLE : View.VISIBLE);
    }

    /*用SpacesItemDecoration和GridLayoutItemDecoration调不出ui效果，xml中item之间的间距暂时用margin控制*/
    private void setItemWidth(View view) {
        if (itemWidth <= 0) {
            int screenWidth = DensityUtil.getScreenWidth(view.getContext());
            int dp2px = DensityUtil.dp2px(30 + 15 * 6);
            itemWidth = (screenWidth - dp2px) / 6;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemWidth + DensityUtil.dp2px(3);
        view.setLayoutParams(layoutParams);
    }
}
