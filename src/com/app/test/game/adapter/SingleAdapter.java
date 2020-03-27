package com.app.test.game.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.app.test.R;
import com.app.test.util.DensityUtil;
import com.app.test.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class SingleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean isSelected;
    private ArrayList<String> selectData;
    private ArrayList<String> answerData;
    private boolean isTips;
    private int finalHeight;
    //item的固定高度
    private int fixedHeight = DensityUtil.dp2px(50);

    public SingleAdapter() {
        super(R.layout.item_single_option);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        if (finalHeight > 0) {
            RelativeLayout view = helper.getView(R.id.rl_option_layout);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            params.height = finalHeight - 10;
            params.topMargin = 10;
            view.setLayoutParams(params);
        }
        String item = getItem(position);
        helper.setText(R.id.tv_option_content, StringUtils.trimToEmpty(item));
        helper.setGone(R.id.iv_status, false);
        boolean isSelect = false;
        boolean isAnswer = false;
        if (isTips || isSelected)
            if (answerData != null && answerData.contains((position + 1 + "")))
                isAnswer = true;
        if (isSelected) {
            if (selectData != null && selectData.contains(position + 1 + ""))
                isSelect = true;
            //选中而且是对的
            if (isSelect) {   //选中是错的
                helper.setGone(R.id.iv_status, true);
                if (isAnswer) {
                    helper.setImageResource(R.id.iv_status, R.drawable.ic_game_single_right);
                    helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_92ce3a_23);
                } else {
                    helper.setImageResource(R.id.iv_status, R.drawable.ic_game_single_error);
                    helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_ffb90e_23);
                }
            } else
                helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_e9eef3_23);
        } else {
            helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_e9eef3_23);
            helper.setGone(R.id.iv_status, false);
        }
        if (isTips && isAnswer) {
            helper.setGone(R.id.iv_correct, true);
        } else {
            helper.setGone(R.id.iv_correct, false);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }

    public void setProblemData(List<String> data, ArrayList<String> selectData, ArrayList<String> answerData) {
        this.isSelected = false;
        this.selectData = selectData;
        this.answerData = answerData;
        isTips = false;
        setNewData(data);
    }

    public void dataChange() {
        this.isSelected = true;
        notifyDataSetChanged();
    }

    public void setTips(boolean tips) {
        isTips = tips;
        notifyDataSetChanged();
    }

    /**
     * 设置RecyclerView的高度，方便计算item的高度
     *
     * @param optionHeight
     */
    public void setOptionHeight(int optionHeight) {
        //显示选项的RecyclerView高度不够显示4个默认的高度 这是要重新计算高度
        if (optionHeight < fixedHeight * 4) {
            finalHeight = optionHeight / 4;
            notifyDataSetChanged();
        }
    }
}
