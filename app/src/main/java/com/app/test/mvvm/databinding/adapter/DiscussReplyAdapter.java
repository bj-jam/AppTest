package com.app.test.mvvm.databinding.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.app.test.R;
import com.app.test.mvvm.databinding.bean.ReplyDto;

/**
 * Created by jam on 2019/2/18.
 */

public class DiscussReplyAdapter extends BaseQuickAdapter<ReplyDto, BaseViewHolder> {

    private Bitmap bitmap;
    private Context context;
    private final String icon = "icon";

    public DiscussReplyAdapter(Context context) {
        super(R.layout.item_talk_discuss_reply);
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lib_document_icon);
    }


    @Override
    protected void convert(BaseViewHolder helper, ReplyDto item) {
        //tv_discuss_content
//        helper.setText(R.id.tv_reply_content, item.content);
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(item.userName)) {
            if (item.userName.length() > 4) {
                sb.append(item.userName.substring(0, 4));
                sb.append("...");
            } else
                sb.append(item.userName);
        }
        try {
            setTextContent((TextView) helper.getView(R.id.tv_reply_content), item.content, sb.toString(), item.isFamousTeacher);
        } catch (Exception e) {
            helper.setText(R.id.tv_reply_content, Html.fromHtml("<font color='#2C6785'>" + sb.toString() + " : </font>" + item.content));
        }
    }

    private void setTextContent(TextView textView, String content, String name, boolean isFamousTeacher) {
        int end = 0;
        int imageEnd = 0;
        StringBuilder buffer = new StringBuilder();
        buffer.append(name);
        if (isFamousTeacher) {
            buffer.append(" ");
            buffer.append(icon);
            end = name.length() + icon.length() + 4;
            imageEnd = name.length() + icon.length() + 1;
        } else {
            end = name.length() + 3;
        }
        buffer.append(" : ");
        buffer.append(content);
        SpannableString spanString = new SpannableString(buffer.toString());
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2C6785"));
        spanString.setSpan(colorSpan, 0, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (isFamousTeacher) {
            ImageSpan imgSpan = new ImageSpan(textView.getContext(), bitmap);
            spanString.setSpan(imgSpan, name.length() + 1, imageEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spanString);
    }

}
