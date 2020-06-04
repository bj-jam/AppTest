package com.app.test.glide.img;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideRoundTransform extends BitmapTransformation {

	private float radius = 0f;

	public GlideRoundTransform(Context context) {
		this(context, 4);
	}

	public GlideRoundTransform(Context context, int dp) {
		super(context);
		this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
	}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
			int outWidth, int outHeight) {
		return ImgUtil.roundCrop(pool, toTransform, radius);
	}

	@Override
	public String getId() {
		return getClass().getName() + Math.round(radius);
	}
}
