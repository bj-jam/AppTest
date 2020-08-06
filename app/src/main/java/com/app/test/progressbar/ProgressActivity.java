package com.app.test.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.test.R;

import java.util.Timer;
import java.util.TimerTask;


public class ProgressActivity extends Activity implements OnProgressBarListener {
    private Timer timer;

    private NumberProgressBar bnp;


    private ProgressWheel pwOne, pwTwo;
    private PieProgress mPieProgress1, mPieProgress2;
    boolean wheelRunning, pieRunning;
    int wheelProgress = 0, pieProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_progress);

        bnp = (NumberProgressBar) findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);


        pwOne = (ProgressWheel) findViewById(R.id.progress_bar_one);
        pwOne.spin();
        pwTwo = (ProgressWheel) findViewById(R.id.progress_bar_two);
        new Thread(r).start();

        mPieProgress1 = (PieProgress) findViewById(R.id.pie_progress1);
        mPieProgress2 = (PieProgress) findViewById(R.id.pie_progress2);
        new Thread(indicatorRunnable).start();

        Button startBtn = (Button) findViewById(R.id.btn_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!wheelRunning) {
                    wheelProgress = 0;
                    pwTwo.resetCount();
                    new Thread(r).start();
                }
            }
        });

        Button pieStartBtn = (Button) findViewById(R.id.btn_start2);
        pieStartBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!pieRunning) {
                    pieProgress = 0;
                    new Thread(indicatorRunnable).start();
                }
            }
        });
    }


    final Runnable r = new Runnable() {
        public void run() {
            wheelRunning = true;
            while (wheelProgress < 361) {
                pwTwo.incrementProgress();
                wheelProgress++;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            wheelRunning = false;
        }
    };

    final Runnable indicatorRunnable = new Runnable() {
        public void run() {
            pieRunning = true;
            while (pieProgress < 361) {
                mPieProgress1.setProgress(pieProgress);
                mPieProgress2.setProgress(pieProgress);
                pieProgress += 2;
                ;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            pieRunning = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            Toast.makeText(getApplicationContext(), "结束", Toast.LENGTH_SHORT).show();
            bnp.setProgress(0);
        }
    }
}
