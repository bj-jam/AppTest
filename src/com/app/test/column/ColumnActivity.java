package com.app.test.column;

import java.util.Calendar;

import com.app.test.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

public class ColumnActivity extends Activity implements OnTouchListener {
	private HistogramView hv;

	private String[] weeks;// 设置星期数目
	private int[] steps;// 设置7天的步数
	private int[] text;// 设置是否显示对应柱状图的数值

	private TextView average_step;
	private TextView sum_step;

	private int average = 0;
	private int sum = 0;
	private int average1 = 0;
	private int sum1 = 0;

	private Calendar calendar;
	private String day;
	View cv;
	private AllAnimation ani;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_column);
		cv = getWindow().getDecorView();
		init();
		setWeek();
		setProgress();
		cv.startAnimation(ani);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void init() {
		weeks = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
		steps = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		text = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		average_step = (TextView) findViewById(R.id.average_step);
		sum_step = (TextView) findViewById(R.id.sum_step);
		ani = new AllAnimation();
		ani.setDuration(1000);

		calendar = Calendar.getInstance();

		hv = (HistogramView) findViewById(R.id.histograms);

		hv.setOnTouchListener(this);

	}

	@SuppressLint("SimpleDateFormat")
	private void setProgress() {
		int a[] = new int[] { 1000, 2314, 6431, 7732, 1234, 6432, 3426 };
		for (int j = 0; j < steps.length; j++) {
			steps[j] = a[j];
			sum += a[j];
		}

		hv.setWeekd(weeks);
		hv.setProgress(steps);

	}

	private void setWeek() {

		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day -= 1;
		for (int i = 0; i < weeks.length; i++) {
			weeks[i] = week(day - i);
		}
	}

	private String week(int day) {
		if (day < 1) {
			day += 7;
		}
		switch (day) {
		case 1:
			return "周一";
		case 2:
			return "周二";
		case 3:
			return "周三";
		case 4:
			return "周四";
		case 5:
			return "周五";
		case 6:
			return "周六";
		case 7:
			return "周日";
		default:
			return "";
		}
	}

	private class AllAnimation extends Animation {
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				sum1 = (int) (sum * interpolatedTime);
				average1 = (int) (average * interpolatedTime);
			} else {
				sum1 = sum;
				average1 = average;
			}
			cv.postInvalidate();
			sum_step.setText(sum1 + "");
			average = sum / 7;
			average_step.setText(average1 + "");

		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int step = (v.getWidth() - 30) / 8;
		int x = (int) event.getX();
		for (int i = 0; i < 7; i++) {
			if (x > (30 + step * (i + 1) - 30)
					&& x < (30 + step * (i + 1) + 30)) {
				text[i] = 1;
				for (int j = 0; j < 7; j++) {
					if (i != j) {
						text[j] = 0;
					}
				}
				hv.setText(text);
			}
		}

		return false;
	}

}
