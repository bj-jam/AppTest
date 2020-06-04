package com.app.test.util;

import java.text.DecimalFormat;

import com.app.test.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆环进度条
 * 
 * @author lcx
 */
public class ProgressBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	public int[] numbers;
	// 百分比
	private String percentString;
	/** 数值占总数弧度数集合 */
	private float radians;
	/** 圆形左边画图起点 */
	private int centreX;
	/** 圆形顶部画图起点 */
	private int centreY;
	// 圆环的半径
	private int radius;
	private RectF oval;
	/** 圆环色值组 */
	private int[] colors;
	private Rect textRect;

	public ProgressBar(Context context) {
		this(context, null);
		init();
	}

	public ProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init();
	}

	public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		textRect = new Rect();
		/**
		 * 计算半径
		 */
		if (getWidth() > getHeight()) {
			radius = (int) (getHeight() / 2 - roundWidth / 2); // 圆环的半径
		} else {
			radius = (int) (getWidth() / 2 - roundWidth / 2); // 圆环的半径

		}
		/** 圆形左边画图起点 */
		centreX = (getWidth() - 2 * radius) / 2;
		/** 圆形顶部画图起点 */
		centreY = (getHeight() - 2 * radius) / 2;

		drawProgressBar(canvas);
		drawInfo(canvas);
	}

	private void init() {
		paint = new Paint();
		numbers = new int[] {};
		// 获取自定义属性和默认值
		roundWidth = 5;
	}

	private void drawProgressBar(Canvas canvas) {

		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		// canvas.drawCircle(centreX, centreY, radius, paint); // 画出圆环
		oval = new RectF(centreX, centreY, 2 * radius + centreX, 2 * radius
				+ centreY); // 用于定义的圆弧的形状和大小的界限

		// 设置圆环的颜色
		paint.setColor(colors[0]);
		// 扇形起点弧度，下次画的时候累加上次弧度数
		// 画弧度:矩形区域，起始点(已画度数)，所占度数，填充扇形，画笔
		canvas.drawArc(oval, -90, radians, false, paint);

	}

	private void drawInfo(Canvas canvas) {
		// 画百分比
		paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dp12));
		paint.getTextBounds(percentString, 0, percentString.length(), textRect);
		paint.setStyle(Paint.Style.FILL);
		float textL = centreX + radius - textRect.width() / 2;
		float textB = centreY + radius + textRect.height() + 15;
		canvas.drawText(percentString, textL, textB, paint);
		// 画人数
		paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dp18));
		paint.getTextBounds(numbers[0] + "", 0, (numbers[0] + "").length(),
				textRect);
		// 加粗
		paint.setFakeBoldText(true);
		paint.setStyle(Paint.Style.FILL);
		// 开始X坐标
		float L = centreX + radius - textRect.width() / 2 - roundWidth;
		// 开始Y坐标
		float B = centreY + radius;
		// 画数字
		canvas.drawText(numbers[0] + "", L, B, paint);
	}

	/** 设置圆环色值组 */
	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;

		if (numbers.length > 1) {
			radians = 360f * numbers[0] / numbers[1];
		}
		DecimalFormat df = new DecimalFormat("###.00");// 百分位保留两位小数
		float percent = 1f * numbers[0] / numbers[1] * 100;// 算数值占比百分值
		float v = Float.parseFloat(df.format(percent));// 格式化显示的百分比
		int v_ = (int) v;

		if (v >= (v_ + 0.5)) {
			v_ = v_ + 1;
		}
		percentString = v_ + "%";
	}
}
