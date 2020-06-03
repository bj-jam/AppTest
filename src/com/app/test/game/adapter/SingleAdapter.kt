package com.app.test.game.adapter

import android.support.v7.widget.RecyclerView
import android.widget.RelativeLayout
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.util.*

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
class SingleAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_single_option) {
    private var isSelected = false
    private var selectData: ArrayList<String>? = null
    private var answerData: ArrayList<String>? = null
    private var isTips = false
    private var finalHeight = 0

    //item的固定高度
    private val fixedHeight = dp2px(50)
    override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
        if (finalHeight > 0) {
            val view = helper.getView<RelativeLayout>(R.id.rl_option_layout)
            val params = view.layoutParams as RecyclerView.LayoutParams
            params.height = finalHeight - 10
            params.topMargin = 10
            view.layoutParams = params
        }
        val item = getItem(position)
        helper.setText(R.id.tv_option_content, StringUtils.trimToEmpty(item))
        helper.setGone(R.id.iv_status, false)
        var isSelect = false
        var isAnswer = false
        if (isTips || isSelected) if (answerData != null && answerData?.contains((position + 1).toString()) == true) isAnswer = true
        if (isSelected) {
            if (selectData != null && selectData?.contains((position + 1).toString()) == true) isSelect = true
            //选中而且是对的
            if (isSelect) {   //选中是错的
                helper.setGone(R.id.iv_status, true)
                if (isAnswer) {
                    helper.setImageResource(R.id.iv_status, R.drawable.ic_game_single_right)
                    helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_92ce3a_23)
                } else {
                    helper.setImageResource(R.id.iv_status, R.drawable.ic_game_single_error)
                    helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_ffb90e_23)
                }
            } else helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_e9eef3_23)
        } else {
            helper.setBackgroundRes(R.id.rl_option_layout, R.drawable.shape_rectangle_solid_e9eef3_23)
            helper.setGone(R.id.iv_status, false)
        }
        if (isTips && isAnswer) {
            helper.setGone(R.id.iv_correct, true)
        } else {
            helper.setGone(R.id.iv_correct, false)
        }
    }

    override fun convert(helper: BaseViewHolder, item: String?) {

    }

    fun setProblemData(data: List<String>?, selectData: ArrayList<String>?, answerData: ArrayList<String>?) {
        isSelected = false
        this.selectData = selectData
        this.answerData = answerData
        isTips = false
        setNewData(data)
    }

    fun dataChange() {
        isSelected = true
        notifyDataSetChanged()
    }

    fun setTips(tips: Boolean) {
        isTips = tips
        notifyDataSetChanged()
    }

    /**
     * 设置RecyclerView的高度，方便计算item的高度
     *
     * @param optionHeight
     */
    fun setOptionHeight(optionHeight: Int) {
        //显示选项的RecyclerView高度不够显示4个默认的高度 这是要重新计算高度
        if (optionHeight < fixedHeight * 4) {
            finalHeight = optionHeight / 4
            notifyDataSetChanged()
        }
    }
}