package com.app.test.line;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.app.test.R;

public class LineActivity extends Activity {
    private LineView1 sl;
    private int[] s = new int[30];
    /**
     * SD卡路径
     */
    public static final String SDCardPath = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    /**
     * 图片缓存目录
     */
    public static final String downFile = SDCardPath
            + "/Test/image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_view);
        sl = (LineView1) findViewById(R.id.sl);
        s[3] = 5;
        s[20] = 8;
        s[8] = 100;
        s[9] = 14;
        s[10] = 13;
        sl.setNumbers(s, true, "6", true);
    }
}
