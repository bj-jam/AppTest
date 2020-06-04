package com.app.test.ring.anim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.ring.anim.view.CircleMenuLayout;

/**
 *
 */
public class CircleBankActivity extends Activity {

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡"};
    private int[] mItemImgs = new int[]{R.drawable.icon_pic1,
            R.drawable.icon_pic2, R.drawable.icon_pic3,
            R.drawable.icon_pic4, R.drawable.icon_pic5,
            R.drawable.icon_pic6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circle_menu);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);


        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {

            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(CircleBankActivity.this, mItemTexts[pos], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(CircleBankActivity.this, "点击了中心圆盘  ", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
