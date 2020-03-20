package com.app.test.util;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.app.test.R;

public class MainActivity extends Activity implements TextWatcher {

	private List<String> list = new ArrayList<String>();
	private SimpleDateFormat mFormat;
	private long time;
	private SoftReference<Bitmap> s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle);
		// getImgSrcs("");
		// String[] strings = getImgSrcs(null);
		// getTextFromHtml("");
		//
		// // mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// // try {
		// // time = mFormat.parse("::").getTime();
		// // } catch (ParseException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// for (String string : list) {
		//
		// }
		s = new SoftReference<Bitmap>(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.ic_launcher));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String[] getImgSrcs(String htmlStr) {
		if (htmlStr == null) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		String[] strs = null;

		String imgRegex = "<img[^(>)(src)]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
		Pattern p = Pattern.compile(imgRegex);
		Matcher m = p.matcher(htmlStr);

		while (m.find()) {
			String str = m.group(1);
			list.add(str);
		}

		if (list.size() != 0) {
			strs = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				strs[i] = list.get(i);
			}
		}
		return strs;
	}

	private String getTextFromHtml(String htmlStr) {
		return htmlStr.replaceAll("<[^\\P{Graph}>]+(?: [^>]*)?>", "");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		Log.e("jam", s + "-----CharSequence-----");
		Log.e("jam", start + "-----start-----");
		Log.e("jam", count + "-----count-----");
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		Log.e("jam", s + "<-----onTextChanged CharSequence-----");
		Log.e("jam", start + "<-----onTextChanged start-----");
		Log.e("jam", count + "<-----onTextChanged count-----");
		Log.e("jam", before + "<-----onTextChanged before-----");
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Log.e("jam", s.toString() + "<-----afterTextChanged-----");
		if (TextUtils.isEmpty(s.toString())) {
			Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
		} else {
			for (char i : s.toString().toCharArray()) {
				Log.e("jam", i + "1111111111");
			}
		}
	}

}
