package com.app.test.glide.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.app.test.R;
import com.app.test.util.DisplayUtil;

public class ImgUtil {

	/**
	 * 圆角bitmap
	 * 
	 * @param pool
	 * @param source
	 * @param radius
	 *            圆角大小（dp）
	 * @return
	 */
	public static Bitmap roundCrop(BitmapPool pool, Bitmap source, float radius) {
		if (source == null)
			return null;

		Bitmap result = pool.get(source.getWidth(), source.getHeight(),
				Bitmap.Config.ARGB_8888);
		if (result == null) {
			result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
					Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
				BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rectF, radius, radius, paint);
		return result;
	}

	/** 圆形bitmap */

	public static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
		if (source == null)
			return null;

		int size = Math.min(source.getWidth(), source.getHeight());
		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;

		// TODO this could be acquired from the pool too
		Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

		Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
		if (result == null) {
			result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP,
				BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		float r = size / 2f;
		canvas.drawCircle(r, r, r, paint);
		return result;
	}

	/** 获取处理后的位图 */
	public static Bitmap getBitmap(Context context, Bitmap bmWeak, int dis,
			String color) {
		if (bmWeak == null) {
			bmWeak = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.code6);
		}

		int size = Math.min(bmWeak.getWidth(), bmWeak.getHeight());
		int radius = size / 2;
		// 创建空白的可绘制的位图
		Bitmap circleWeak = Bitmap.createBitmap(size, size, bmWeak.getConfig());
		// 以空白位图创建画布
		Canvas canvas = new Canvas(circleWeak);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		// 先画圆形
		canvas.drawCircle(radius, radius, radius, paint);
		// 设置图像的叠加模式，取交集上层
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		// 画位图
		canvas.drawBitmap(bmWeak, 0, 0, paint);

		// 颜色为空
		if (TextUtils.isEmpty(color)) {
			color = "white";
		}

		// 将dp转成px
		dis = DisplayUtil.getInstance().dip2px(dis);

		// 带有外圈空白可绘制的位图
		Bitmap colorWeak = Bitmap.createBitmap((radius + dis) * 2,
				(radius + dis) * 2, circleWeak.getConfig());
		// 以空白位图创建画布
		canvas = new Canvas(colorWeak);
		paint = new Paint();
		paint.setAntiAlias(true);
		// 设置外圈颜色
		paint.setColor(Color.parseColor(color));
		// 画圆
		canvas.drawCircle(radius + dis, radius + dis, radius + dis, paint);
		// 画圆形图
		canvas.drawBitmap(circleWeak, dis, dis, paint);

		return colorWeak;
	}

	/**
	 * 生成带白（其他）边的原图，glide加载图片时用的
	 * 
	 * @param pool
	 * @param source
	 * @param dis
	 *            白边的宽度
	 * @param color
	 *            不传默认为白色
	 * @return
	 */
	public static Bitmap getBitmap(BitmapPool pool, Bitmap source, int dis,
			String color) {
		if (source == null) {
			return null;
		}

		int size = Math.min(source.getWidth(), source.getHeight());
		int radius = size / 2;

		Bitmap circleWeak = pool.get(source.getWidth(), source.getHeight(),
				Bitmap.Config.ARGB_8888);
		if (circleWeak == null) {
			circleWeak = Bitmap.createBitmap(source.getWidth(),
					source.getHeight(), Bitmap.Config.ARGB_8888);
		}

		// 创建空白的可绘制的位图
		// 以空白位图创建画布
		Canvas canvas = new Canvas(circleWeak);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		// 先画圆形
		canvas.drawCircle(radius, radius, radius, paint);
		// 设置图像的叠加模式，取交集上层
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		// 画位图
		canvas.drawBitmap(source, 0, 0, paint);

		// 颜色为空
		if (TextUtils.isEmpty(color)) {
			color = "white";
		}

		// 将dp转成px
		dis = DisplayUtil.getInstance().dip2px(dis);

		// 带有外圈空白可绘制的位图
		Bitmap colorWeak = Bitmap.createBitmap((radius + dis) * 2,
				(radius + dis) * 2, circleWeak.getConfig());
		// 以空白位图创建画布
		canvas = new Canvas(colorWeak);
		paint = new Paint();
		paint.setAntiAlias(true);
		// 设置外圈颜色
		paint.setColor(0xFF398cd6);// 当前蓝色
		// 画圆
		canvas.drawCircle(radius + dis, radius + dis, radius + dis, paint);
		// 画圆形图
		canvas.drawBitmap(circleWeak, dis, dis, paint);

		return colorWeak;
	}
}
