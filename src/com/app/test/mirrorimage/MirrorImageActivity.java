package com.app.test.mirrorimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.test.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MirrorImageActivity extends Activity {
	private ImageView imageView1;
	private ImageView imageView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_rotate);
		initViews();
	}

	private void initViews() {
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);

		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.code6);
		// imageView1.setImageBitmap(bm);
		Picasso.get().load(R.drawable.my_gif_view).into(imageView1);
		Bitmap modBm = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				bm.getConfig());
		// 以生成的第二张图片做画板
		Canvas canvas = new Canvas(modBm);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		Matrix matrix = new Matrix();
		// matrix.setRotate(90, bm.getWidth()/2, bm.getHeight()/2);
		// matrix.setTranslate(20, 20);
		// 镜子效果
		// 创建矩阵，并水平平移
		matrix.setScale(-1, 1);
		matrix.postTranslate(bm.getWidth(), 0);
		// 使用矩阵绘制镜像图片
		canvas.drawBitmap(bm, matrix, paint);
		imageView2.setImageBitmap(modBm);
	}

	/**
	 * 加载数据
	 */
	public void initData() {
		/**
		 * 根据ImageView大小，显示图片 <br>
		 * .fit() 说明：控件不能设置成wrap_content,也就是必须有大小才行,fit()才让图片的宽高等于控件的宽高，设置fit()，
		 * 不能再调用resize() <br>
		 * .placeholder(R.drawable.topic_tom) 说明：当图片没有加载上的时候，显示的图片<br>
		 * .error(R.drawable.topic_sky) 说明：当图片加载错误的时候，显示图片 <br>
		 * .into(img_one) 说明：将图片加载到哪个控件中
		 */
		Picasso.get()
				.load("http://g.hiphotos.baidu.com/image/pic/item/c9fcc3cec3fdfc03e426845ed03f8794a5c226fd.jpg")
				.fit()//
				.placeholder(R.drawable.code6)//
				.error(R.drawable.code6)//
				.into(imageView2);
		/**
		 * 通过程序代码，来显示图片大小<br>
		 * .resize(200, 150) 说明：为图片重新定义大小<br>
		 * .centerCrop() 说明：图片要填充整个控件，去两边留中间<br>
		 */
		Picasso.get()
				.load("http://d.hiphotos.baidu.com/image/h%3D200/sign=745574b6a2ec08fa390014a769ee3d4d/cb8065380cd79123148b447daf345982b2b78054.jpg")
				.resize(200, 150).centerCrop().placeholder(R.drawable.column)
				.error(R.drawable.column).into(imageView2);

		/**
		 * 加载本地数据库,图片的大小，取消于控件设置的大小
		 */
		Picasso.get().load(R.drawable.column).into(imageView2);

		/**
		 * 截取图片 <br>
		 * .transform(new CropSquareTransformation()) 说明：通过程序截取图片
		 */
		Picasso.get()
				.load("http://g.hiphotos.baidu.com/image/pic/item/6c224f4a20a446230761b9b79c22720e0df3d7bf.jpg")
				.transform(new CropSquareTransformation())
				.placeholder(R.drawable.column).error(R.drawable.column)
				.into(imageView2);
	}

	/**
	 * picasso的Transformation方法，对图片进行截取 Created by cg on 2016/2/1.
	 */
	public class CropSquareTransformation implements Transformation {

		// 截取从宽度和高度最小作为bitmap的宽度和高度
		@Override
		public Bitmap transform(Bitmap source) {
			int size = Math.min(source.getWidth(), source.getHeight());
			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;
			Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
			if (result != source) {
				source.recycle();// 释放bitmap
			}
			return result;
		}

		@Override
		public String key() {
			return "square()";
		}
	}
}
