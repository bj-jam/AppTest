package com.app.test.util;

import java.io.File;
import java.io.FileOutputStream;

import com.app.test.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class D {

	public static void initCacheBitmap(Context context, String path, View v) {
		try {
			// 扩展宽度
			int disW = DisplayUtil.getInstance().dip2px(20);
			// 扩展高度
			int disH = DisplayUtil.getInstance().dip2px(100);
			// 文字颜色
			int textColor = context.getResources()
					.getColor(R.color.text_color3);

			// 获取控件的屏幕截图
			v.setDrawingCacheEnabled(true);
			Bitmap viewBitmap = v.getDrawingCache();
			int viewW = viewBitmap.getWidth();
			int viewH = viewBitmap.getHeight();
			Bitmap bm = Bitmap.createBitmap(viewW + disW, viewH + disH,
					viewBitmap.getConfig());

			// 创建可修改的位图进行作图
			Canvas canvas = new Canvas(bm);
			// 设置背景颜色
			canvas.drawColor(context.getResources().getColor(R.color.white));
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			canvas.drawBitmap(viewBitmap, disW / 2, disH * 2 / 5, paint);

			// 画顶部文字
			String text = "欢迎到此一游";
			Rect textRect = new Rect();
			paint.setColor(textColor);
			paint.setStrokeWidth(DisplayUtil.getInstance().dip2px(2));
			paint.setTextSize(DisplayUtil.getInstance().dip2px(16));
			paint.getTextBounds(text, 0, text.length(), textRect);
			// 文字左边距
			int x = (viewW + disW - textRect.width()) / 2;
			// 文字右边距
			int y = (disH * 2 / 5 - textRect.height()) / 2 + textRect.height();
			canvas.drawText(text, x, y, paint);

			// 画底部文字
			text = "欢迎到此一游";
			textRect = new Rect();
			paint.setColor(textColor);
			paint.setStrokeWidth(DisplayUtil.getInstance().dip2px(2));
			paint.setTextSize(DisplayUtil.getInstance().dip2px(16));
			paint.getTextBounds(text, 0, text.length(), textRect);
			// 文字左边距
			x = (viewW + disW - textRect.width()) / 2;
			// 文字右边距
			y = disH
					* 2
					/ 5
					+ viewH
					+ ((disH * 3 / 5 - textRect.height()) / 2 + textRect
							.height());
			canvas.drawText(text, x, y, paint);

			// 保存图片
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
