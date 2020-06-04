package com.app.test.game.adapter

import android.view.View
import com.app.test.R
import com.app.test.game.bean.SuperType
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.DensityUtil.getScreenWidth
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
class IdiomFreeAdapter(data: List<SuperType>?) : BaseQuickAdapter<SuperType, BaseViewHolder>(R.layout.item_idiom_free, data) {
    private var itemWidth = 0
    override fun convert(helper: BaseViewHolder, data: SuperType) {
        helper.setText(R.id.tv_select_idiom_free_word, data.title)
        val flFreeWord = helper.getView<View>(R.id.fl_free_word)
        setItemWidth(flFreeWord)
        flFreeWord.visibility = if (data.isSelected) View.INVISIBLE else View.VISIBLE
    }

    /*用SpacesItemDecoration和GridLayoutItemDecoration调不出ui效果，xml中item之间的间距暂时用margin控制*/
    private fun setItemWidth(view: View) {
        if (itemWidth <= 0) {
            val screenWidth = getScreenWidth(view.context)
            val dp2px = dp2px(30 + 15 * 6)
            itemWidth = (screenWidth - dp2px) / 6
        }
        val layoutParams = view.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemWidth + dp2px(3)
        view.layoutParams = layoutParams
    }
}