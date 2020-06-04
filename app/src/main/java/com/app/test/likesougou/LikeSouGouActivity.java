package com.app.test.likesougou;

import android.app.Activity;
import android.os.Bundle;

import com.app.test.R;

public class LikeSouGouActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_sougou);
        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().add(R.id.root_content, mainFragment).commit();
    }
}
