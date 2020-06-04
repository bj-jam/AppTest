package com.app.test.test;

import java.util.HashMap;

import com.app.test.R;
import com.app.test.test.Zhexian1.Mstyle;

import android.app.Activity;
import android.os.Bundle;

public class ZhexianActivity extends Activity {
	HashMap<Double, Double> map;
	private Zhexian1 tu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhexian);

		tu = (Zhexian1) findViewById(R.id.zhexian);
		tu.SetTuView(map, 50, 10, "x", "y", false);
		map = new HashMap<Double, Double>();
		map.put(1.0, (double) 0);
		map.put(3.0, 25.0);
		map.put(4.0, 32.0);
		map.put(5.0, 41.0);
		map.put(6.0, 16.0);
		map.put(7.0, 36.0);
		map.put(8.0, 26.0);
		tu.setTotalvalue(50);
		tu.setPjvalue(10);
		tu.setMap(map);
		// tu.setXstr("");
		// tu.setYstr("");
		tu.setMargint(20);
		tu.setMarginb(50);
		tu.setMstyle(Mstyle.Curve);
		tu.setIsylineshow(false);
	}

	// private void randmap(HashMap<Double, Double> mp, Double d) {
	// ArrayList<Double> dz = tool.getintfrommap(mp);
	// Double[] dvz = new Double[mp.size()];
	// int t = 0;
	// @SuppressWarnings("rawtypes")
	// Set set = mp.entrySet();
	// @SuppressWarnings("rawtypes")
	// Iterator iterator = set.iterator();
	// while (iterator.hasNext()) {
	// @SuppressWarnings("rawtypes")
	// Map.Entry mapentry = (Map.Entry) iterator.next();
	// dvz[t] = (Double) mapentry.getValue();
	// t += 1;
	// }
	// for (int j = 0; j < dz.size() - 1; j++) {
	// mp.put(dz.get(j), mp.get(dz.get(j + 1)));
	// }
	// mp.put((Double) dz.get(mp.size() - 1), d);
	// tu.postInvalidate();
	// }
}
