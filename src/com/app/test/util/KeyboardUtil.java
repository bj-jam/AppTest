package com.app.test.util;

import com.app.test.R;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class KeyboardUtil {
	private Context ctx;
	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k2;// 数字键盘
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写

	private EditText ed;

	private View examLayout;

	private int screen_height;

	public KeyboardUtil(Activity act, Context ctx, View examLayout) {
		this.act = act;
		this.ctx = ctx;
		k2 = new Keyboard(ctx, R.xml.symbols);
//		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k2);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
		screen_height = 145;
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	final int[] locations = new int[2];
	float y = 0;
	boolean isMove = false;

	public void showKeyboard(EditText edit) {
		this.ed = edit;
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
			ed.getLocationOnScreen(locations);
			y = screen_height - locations[1];
			Log.e("jam", locations[1] + "------------>");
			// if (y > keyboardView.getHeight()) {
			// Toast.makeText(ctx, "不需要移动", Toast.LENGTH_LONG).show();
			// } else {
			// // animation(examLayout,);
			// isMove = true;
			// Toast.makeText(ctx,
			// "需要移动--->" + (keyboardView.getHeight() - y),
			// Toast.LENGTH_LONG).show();
			// animation(examLayout, 0, keyboardView.getHeight() - y);
			// }
		}
	}

	/** 平移动画 */
	private void animation(Object v, float f, float f1) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", f,
				f1);
		animator.setDuration(200);
		animator.start();
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.GONE);
			if (isMove) {
				isMove = false;
				animation(examLayout, -(keyboardView.getHeight() - y), 0);
			}
		}
	}
}
