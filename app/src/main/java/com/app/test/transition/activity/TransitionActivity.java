package com.app.test.transition.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.test.R;
import com.app.test.transition.model.Item;
import com.app.test.transition.view.adapter.MainGridAdapter;


public class TransitionActivity extends Activity {

    private GridView gv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        initView();
    }

    private void initView() {

        gv = (GridView) findViewById(R.id.id_gv);
        gv.setAdapter(new MainGridAdapter(this));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Item item = (Item) parent.getItemAtPosition(position);

                // Construct an Intent as normal
                Intent intent = new Intent(TransitionActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_PARAM_ID, item.getId());

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        TransitionActivity.this,
                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        new Pair<View, String>(view.findViewById(R.id.id_iv),
                                DetailActivity.VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(view.findViewById(R.id.id_tv),
                                DetailActivity.VIEW_NAME_HEADER_TITLE));

                // Now we can start the Activity, providing the activity options as a bundle
                ActivityCompat.startActivity(TransitionActivity.this, intent, activityOptions.toBundle());
            }
        });
    }

    private void beginTransition() {

    }
}
