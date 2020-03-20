package com.app.test.util;

import com.app.test.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RoundProgressBarNew extends View {

	/** 未下载 */
	public static final int state1 = 1;
	/** 正在下载 */
	public static final int state2 = state1 + 1;
	/** 停止下载 */
	public static final int state3 = state2 + 1;
	/** 已完成 */
	public static final int state4 = state3 + 1;
	/** 下载过 */
	public static final int state5 = state4 + 1;

	/** 画笔对象 */
	private Paint paint;
	/** max最大进度值，progress当前进度值 */
	private long max = -1, progress = 0;
	/** 当前进度值百分比 */
	private String proText = "0%";
	/** 控件宽度 */
	private int viewW = -1;
	/** green下载完成，black进度值颜色，gray下载未完成 */
	private int green, black, gray;

	/** 下载状态：1-未下载，2-正在下载，3-停止，4-已完成，5-下载过 */
	private int downState = 3;

	public RoundProgressBarNew(Context context) {
		super(context);
		init();
	}

	public RoundProgressBarNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RoundProgressBarNew(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);

		green = getResources().getColor(R.color.course_text);
		black = getResources().getColor(R.color.black);
		gray = getResources().getColor(R.color.grey);
	}

	/** 下载状态：1-未下载，2-正在下载，3-停止，4-已完成，5-下载过 */
	public void setDownState(int downState) {
		this.downState = downState;
	}

	/** 设置视频时长 */
	public void setMaxSize(long max) {
		this.max = max;
	}

	/** 更新下载进度 */
	public void setProgress(long progress) {
		if (max <= 0) {
			return;
		}
		this.progress = progress;
		if (progress >= max) {
			progress = max;
		}
		proText = progress * 100 / max + "%";
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (viewW == -1) {
			viewW = getWidth();
		}

		strokeRound(canvas);
	}

	/** 下载进度信息 */
	private void strokeRound(Canvas canvas) {
		if (downState == state4) {
			// 下载完成的打勾
			paint.setStrokeWidth(DisplayUtil.getInstance().dip2px(1));
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(green);
			// 474
			// 130 245
			// 200 310
			// 345 160
			canvas.drawLine(viewW * 26 / 95, viewW * 49 / 95, viewW * 8 / 19, viewW * 62 / 95, paint);
			canvas.drawLine(viewW * 69 / 95, viewW * 32 / 95, viewW * 8 / 19, viewW * 62 / 95, paint);
		} else if (downState == state1) {
			// 下载图标
//			SoftReference<Bitmap> sr = new SoftReference<Bitmap>(BitmapFactory
//					.decodeResource(this.getContext().getResources(), R.drawable.course_directory_download));
//			canvas.drawBitmap(sr.get(), 0, 0, paint);
		} else {
			// 内层进度
			int dis = DisplayUtil.getInstance().dip2px(3);
			paint.setColor(green);
			paint.setStrokeWidth(dis);
			paint.setStyle(Paint.Style.STROKE);
			RectF rf = new RectF(dis, dis, viewW - dis, viewW - dis);
			if (max <= 0) {
				canvas.drawArc(rf, -90, 0, false, paint);
			} else {
				canvas.drawArc(rf, -90, progress * 360f / max, false, paint);
			}

			if (downState == state2) {
				// 外层圆圈
				paint.setStrokeWidth(DisplayUtil.getInstance().dip2px(1));
				paint.setStyle(Paint.Style.STROKE);
				// if (downState == state4) {// 完成下载
				paint.setColor(green);
				// } else {// 未完成
				// paint.setColor(gray);
				// }
				canvas.drawCircle(viewW / 2, viewW / 2, viewW / 2 - DisplayUtil.getInstance().dip2px(1), paint);
				paint.setColor(green);// 设置绿色
				paint.setStyle(Paint.Style.FILL);// 设置填满
				int rectW = viewW / 4;
				canvas.drawRect(viewW / 2 - rectW / 2, viewW / 2 - rectW / 2, viewW / 2 + rectW / 2,
						viewW / 2 + rectW / 2, paint);// 正方形
			} else if (downState == state3 || downState == state5) {
				// 外层圆圈
				paint.setStrokeWidth(DisplayUtil.getInstance().dip2px(1));
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(gray);
				canvas.drawCircle(viewW / 2, viewW / 2, viewW / 2 - DisplayUtil.getInstance().dip2px(1), paint);
				paint.setColor(gray);// 设置绿色
				paint.setStyle(Paint.Style.FILL);// 设置填满
				int rectW = viewW / 4;
				canvas.drawRect(viewW / 2 - rectW / 2, viewW / 2 - rectW / 2, viewW / 2 + rectW / 2,
						viewW / 2 + rectW / 2, paint);// 正方形
			}
		}
	}
}
