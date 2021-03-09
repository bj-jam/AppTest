package com.app.test.mvvm.databinding.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.app.test.R
import com.app.test.mvvm.databinding.bean.CommentBean
import java.util.*

/**
 * Created by jam on 2019/2/18.
 */

class DiscussAdapter(data: List<CommentBean>?, private val context: Context) : BaseMultiItemQuickAdapter<CommentBean, DiscussAdapter.ViewHolder>(data) {
    private var statusMap: HashMap<String, String>? = null
    private var countMap: HashMap<String, Int>? = null
    private val allCount: Int = 0
    private var uuid: String? = null

    init {
        addItemType(CommentBean.COMMENT_TYPE, R.layout.item_talk_discuss)
        addItemType(CommentBean.INFO_TYPE, R.layout.item_info_talk_discuss)
    }


    override fun convert(helper: DiscussAdapter.ViewHolder, item: CommentBean) {
        if (item.itemType == CommentBean.INFO_TYPE) {
            //tv_count
            helper.setText(R.id.tv_count, "全部讨论（" + item.allCount + "）")
        } else {
            //iv_person
            helper.addOnClickListener(R.id.iv_person)
            Glide.with(helper.itemView.context).load(item.userAvatar)
                    .asBitmap().placeholder(R.drawable.default_head).error(R.drawable.default_head)
                    .into(helper.getView<View>(R.id.iv_person) as ImageView)
            //tv_person_name
            helper.addOnClickListener(R.id.tv_person_name)
            helper.setText(R.id.tv_person_name, item.userName)

            helper.setGone(R.id.ll_title, false)
            //tv_is_teacher
            if (item.isFamousTeacher)
                helper.setGone(R.id.tv_is_teacher, true)
            else
                helper.setGone(R.id.tv_is_teacher, false)
            helper.addOnClickListener(R.id.tv_is_teacher)
            //tv_send_time
            helper.setText(R.id.tv_send_time, item.time.toString() + "")
            helper.addOnClickListener(R.id.tv_send_time)
            val tv_discuss_content = helper.getView<TextView>(R.id.tv_discuss_content)
            val tv_show_all = helper.getView<TextView>(R.id.tv_show_all)
            val iv_show_all = helper.getView<ImageView>(R.id.iv_show_all)
            //ll_show_all
            val ll_show_all = helper.getView<LinearLayout>(R.id.ll_show_all)
            //tv_discuss_content
            tv_discuss_content.text = item.content
            helper.addOnClickListener(R.id.tv_discuss_content)
            //            if (item.hasEllipsis == null) {
            //                //如果textView.getLayout()为空，待TextView渲染结束后重新获取Layout对象。
            //                tv_discuss_content.post(new Runnable() {
            //                    @Override
            //                    public void run() {
            //                        try {
            //                            if (tv_discuss_content == null || ll_show_all == null || tv_discuss_content.getLayout() == null)
            //                                return;
            //                            int ellipsisCount = tv_discuss_content.getLayout().getEllipsisCount(tv_discuss_content.getLineCount() - 1);
            //                            //是否超出范围:如果行数大于3或者而且ellipsisCount>0超出范围，会显示省略号。
            //                            if (item.hasEllipsis == null) {
            //                                item.hasEllipsis = !(tv_discuss_content.getLineCount() <= LINES && ellipsisCount == 0);
            //                            }
            //                            //如果文字没有超出范围，则隐藏按钮。
            //                            ll_show_all.setVisibility(item.hasEllipsis ? View.VISIBLE : View.GONE);
            //                            //文字是否全部展示。
            //                            item.isShowAll = ellipsisCount > 0;
            //                            setTextViewLines(tv_discuss_content, tv_show_all, iv_show_all,
            //                                    !item.hasEllipsis || item.isShowAll);
            //                        } catch (Exception e) {
            //                            e.printStackTrace();
            //                        }
            //                    }
            //                });
            //            } else {
            //                ll_show_all.setVisibility(item.hasEllipsis ? View.VISIBLE : View.GONE);
            //                setTextViewLines(tv_discuss_content, tv_show_all, iv_show_all, !item.hasEllipsis || item.isShowAll);
            //            }
            //            ll_show_all.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    item.isShowAll = !item.isShowAll;
            //                    setTextViewLines(tv_discuss_content, tv_show_all, iv_show_all, !item.hasEllipsis || item.isShowAll);
            //                }
            //            });
            helper.addOnClickListener(R.id.ll_reply)
            //iv_show_all
            if (item.replyList == null || item.replyList.size == 0) {
                helper.setGone(R.id.ll_reply, false)
                helper.setGone(R.id.ll_reply_arrow, false)
            } else {
                helper.setGone(R.id.ll_reply, true)
                helper.setGone(R.id.ll_reply_arrow, true)
                //recyclerView
                if (helper.childAdapter == null) {
                    helper.childAdapter = DiscussReplyAdapter(helper.itemView.context)
                    val recyclerView = helper.getView<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
                    recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(recyclerView.context,
                            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                            false)
                    recyclerView.setOnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            helper.getView<View>(R.id.ll_reply).performClick()  //模拟父控件的点击
                        }
                        false
                    }
                    recyclerView.isNestedScrollingEnabled = false
                    recyclerView.adapter = helper.childAdapter
                    //                holder.endAdapter.setVideoClickListener(videoClickListener);
                }
                if (item.replyList.size > 3) {
                    helper.childAdapter!!.setNewData(item.replyList.subList(0, 3))
                    helper.setGone(R.id.tv_look_all_reply, true)
                } else {
                    helper.childAdapter!!.setNewData(item.replyList)
                    helper.setGone(R.id.tv_look_all_reply, false)
                }
            }
            //tv_look_all_reply
            helper.setText(R.id.tv_look_all_reply, "查看全部" + item.replyCount + "条评论 >")
            if (TextUtils.equals(uuid, item.uuid))
                helper.setGone(R.id.tv_cancel, true)
            else
                helper.setGone(R.id.tv_cancel, false)
            //tv_cancel
            helper.addOnClickListener(R.id.tv_cancel)
            //ll_to_reply
            helper.addOnClickListener(R.id.ll_to_reply)
            helper.addOnClickListener(R.id.ll_reply)
            if (statusMap != null && statusMap!![item.commentId] != null && TextUtils.equals("1", statusMap!![item.commentId])) {
                helper.setImageResource(R.id.iv_praise, R.drawable.lib_excel_icon)
                helper.setTextColor(R.id.tv_praise_count, -0x7fc0)
            } else {
                helper.setImageResource(R.id.iv_praise, R.drawable.lib_document_icon)
                helper.setTextColor(R.id.tv_praise_count, -0x777778)
            }
            if (countMap != null && countMap!![item.commentId] != null) {
                helper.setText(R.id.tv_praise_count, countMap!![item.commentId].toString() + "")
                item.praiseCount = countMap?.get(item.commentId) ?: 0
            } else {
                helper.setText(R.id.tv_praise_count, item.praiseCount)
            }
            //iv_praise
            //tv_praise_count

            helper.addOnClickListener(R.id.ll_praise)
        }
    }


    private fun setTextViewLines(textView: TextView, button: TextView, iv_show_all: ImageView, isShowAll: Boolean) {
        if (!isShowAll) {
            //展示全部，按钮设置为点击收起。
            textView.maxHeight = context.resources.displayMetrics.heightPixels
            button.text = "收起"
            iv_show_all.setImageResource(R.drawable.lib_document_icon)
        } else {
            //显示3行，按钮设置为点击显示全部。
            textView.maxLines = LINES
            button.text = "展开"
            iv_show_all.setImageResource(R.drawable.lib_document_icon)
        }
    }

    inner class ViewHolder(view: View) : BaseViewHolder(view) {
        var childAdapter: DiscussReplyAdapter? = null
    }


    fun setPraiseStatus(statusMap: HashMap<String, String>, countMap: HashMap<String, Int>) {
        this.statusMap = statusMap
        this.countMap = countMap
        notifyDataSetChanged()
    }


    fun setUuid(uuid: String) {
        this.uuid = uuid
    }

    companion object {
        /**
         * 最多展示3行。
         */
        private val LINES = 5
    }
}
