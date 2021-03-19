package com.app.ui.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.app.ui.R;

public class HeaderAnimView extends RelativeLayout {

    private HeaderScrollView headerScrollView;
    private ImageView iv;

    public HeaderAnimView(Context context) {
        super(context);
        initView();
    }

    public HeaderAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HeaderAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        View.inflate(getContext(), R.layout.view_header_anim, this);
        headerScrollView = findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                new String[]{"星期一 	和马云洽谈", "星期二	约见李彦宏", "星期三 	约见乔布斯", "星期四 	和Lance钓鱼", "星期五 	和Jett洽谈",
                        "星期六 	和Jason洽谈", "星期日 	和MZ洽谈", "星期一 	和马云洽谈", "星期二	约见李彦宏", "星期三 	约见乔布斯",
                        "星期四 	和Ricky钓鱼", "星期五 	和David洽谈", "星期六 	和Jason洽谈", "星期日 	和MZ洽谈", "……"
                }
        );
        //获取到头部的View
        View header = View.inflate(getContext(), R.layout.listview_header, null);
        //获取到头部的View的图片控件
        iv = header.findViewById(R.id.layout_header_image);
        //将imageView传入到ListView中
        headerScrollView.setZoomImageView(iv);
        //将头部的View设置给ListView的HeaderView
        headerScrollView.addHeaderView(header);
        //设置适配器
        headerScrollView.setAdapter(adapter);

    }
}
