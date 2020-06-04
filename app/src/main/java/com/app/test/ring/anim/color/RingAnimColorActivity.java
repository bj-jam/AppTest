package com.app.test.ring.anim.color;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.test.R;

public class RingAnimColorActivity extends Activity implements OnClickListener {
	private ColorRingView mColorRingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ring_anim_color);
		mColorRingView = (ColorRingView) findViewById(R.id.cp);
		mColorRingView.setOnClickListener(this);
		Random random = new Random();
		int count = random.nextInt(10);
		int[] textColors = new int[count];
		int[] color = new int[count];
		for (int i = 0; i < count; i++) {
			textColors[i] = random.nextInt(1000);
			color[i] = Color.parseColor(getRandColorCode());
		}
		mColorRingView.setNumbers(textColors);
		mColorRingView.setTextColors(color);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if (arg0.getId() == R.id.cp) {
			Random random = new Random();
			int count = random.nextInt(10);
			int[] textColors = new int[count];
			int[] color = new int[count];
			for (int i = 0; i < count; i++) {
				textColors[i] = random.nextInt(1000);
				color[i] = Color.parseColor(getRandColorCode());
			}
			mColorRingView.setNumbers(textColors);
			mColorRingView.setTextColors(color);
		}
	}

	/** 随机生成颜色 */
	public static String getRandColorCode() {
		String r, g, b;
		Random random = new Random();
		r = Integer.toHexString(random.nextInt(256)).toUpperCase();
		g = Integer.toHexString(random.nextInt(256)).toUpperCase();
		b = Integer.toHexString(random.nextInt(256)).toUpperCase();

		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" + g : g;
		b = b.length() == 1 ? "0" + b : b;

		return "#FF" + r + g + b;
	}
}
