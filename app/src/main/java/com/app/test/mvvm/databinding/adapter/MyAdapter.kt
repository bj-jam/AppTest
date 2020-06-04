package com.app.test.mvvm.databinding.adapter

import android.view.View
import com.app.test.R
import com.app.test.databinding.ItemCommentDetialBinding
import com.app.test.mvvm.databinding.bean.CommentBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by jam on 2019-09-29.
 *
 * @describe date binding 配合BaseQuickAdapter使用
 */
class MyAdapter(data: List<CommentBean>) : BaseQuickAdapter<CommentBean, MyAdapter.CustomHolder>(R.layout.item_comment_detial, data) {


    override fun convert(helper: CustomHolder, item: CommentBean) {
        helper.bindTo(item)
    }


    class CustomHolder private constructor(view: View) : BaseViewHolder(view) {

        private val mBinding: ItemCommentDetialBinding = ItemCommentDetialBinding.bind(view)


        fun bindTo(dto: CommentBean) {
            mBinding.commentDto = dto
            mBinding.executePendingBindings()
        }
    }
}