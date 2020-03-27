package com.app.test.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.app.test.R;
import com.app.test.util.DensityUtil;


public class StudyTimeView extends View {
    private final String infoString = "近一周学习";
    private final String timeTextString = "分钟";
    /**
     * 圆弧的范围框
     */
    private RectF mRectangle = new RectF();
    /**
     * 时间的范围
     */
    private Rect timeRect = new Rect();
    /**
     * 描述文字的范围
     */
    private Rect infoTextRect = new Rect();
    /**
     * 时间文字的范围
     */
    private Rect timeTextRect = new Rect();
    /**
     * 背景画笔
     */
    private Paint mDefaultPaint;
    /**
     * 圆弧的画笔
     */
    private Paint mColorPaint;
    /**
     * 分钟时间的画笔
     */
    private Paint timePaint;
    /**
     * "本周累计学习"的画笔
     */
    private Paint infoPaint;
    /**
     * 分钟文字的画笔
     */
    private Paint timeTextPaint;
    /**
     * 圆弧画笔的宽度
     */
    private float circleWidth;
    /**
     * 分钟字体的大小
     */
    private float infoWidth;
    private float textWidth;
    private int mMaxTime;
    private long mCurrentTime;
    private long mCount;
    /**
     * 当前弧度
     */
    private float mCurrentAngle = 0;
    /**
     * 画的最大角度
     */
    private float mPaintAngle;
    /**
     * 最大弧度
     */
    private float mMaxAngle;
    /**
     * 分钟的字体大小
     */
    private int mTimeSize;
    /**
     * 动画
     */
    private BarAnimation anim;
    /**
     * 指针的bitmap
     */
    private Bitmap mBitmap;
    /**
     * 指正转向的
     */
    private Matrix mMatrix;
    /**
     * VIEW的宽度
     */
    private int mWidth;
    /**
     * VIEW的高度
     */
    private int mHeight;
    /**
     * 中心X轴的位置
     */
    private float mCircleX;
    /**
     * 中心Y轴的位置
     */
    private float mCircleY;
    /**
     * 半径
     */
    private int mMaxRadius;
    /**
     * 背景圆形左边画图起点
     */
    private float circleRadiusX = 0;
    /**
     * 背景圆形顶部画图起点
     */
    private float circleRadiusY = 0;
    /**
     * 指针的X轴的位置
     */
    private float thumbX;
    /**
     * 指针的Y轴的位置
     */
    private float thumbY;
    /**
     * 缩放
     */
    private Matrix mScaleMatrix;

    private String timeString = "";

    public StudyTimeView(Context context) {
        super(context);
        init(null, 0);
    }

    public StudyTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StudyTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        circleWidth = DensityUtil.dp2px(10);
        textWidth = DensityUtil.dp2px(14);
        infoWidth = DensityUtil.dp2px(14);
        mTimeSize = DensityUtil.dp2px(40);


        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setColor(0xFFFFFFFF);
        mColorPaint.setStyle(Style.STROKE);
        mColorPaint.setStrokeWidth(circleWidth);
        mColorPaint.setPathEffect(new DashPathEffect(new float[]{3, 10}, 0));


        mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultPaint.setColor(0xFF878B94);
        mDefaultPaint.setStyle(Style.STROKE);
        mDefaultPaint.setStrokeWidth(circleWidth);
        mDefaultPaint.setPathEffect(new DashPathEffect(new float[]{3, 10}, 0));


        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        timePaint.setColor(0xFFFFFFFF);
        timePaint.setStyle(Style.FILL_AND_STROKE);
        timePaint.setTextAlign(Align.LEFT);
        timePaint.setTextSize(mTimeSize);


        timeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        timeTextPaint.setColor(0xCCFFFFFF);
        timeTextPaint.setStyle(Style.FILL_AND_STROKE);
        timeTextPaint.setTextAlign(Align.LEFT);
        timeTextPaint.setTextSize(textWidth);


        infoPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        infoPaint.setColor(0xCCFFFFFF);
        infoPaint.setStyle(Style.FILL_AND_STROKE);
        infoPaint.setTextAlign(Align.LEFT);
        infoPaint.setTextSize(infoWidth);

        //进度指示器
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ndicator);
        mMatrix = new Matrix();
        mScaleMatrix = new Matrix();
        mScaleMatrix.postScale(0.7f, 0.7f);
        // 得到新的图片
        mBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), mScaleMatrix, true);
        if (!b.isRecycled()) {
            b.recycle();
        }

        mMaxTime = 150;
        mMaxAngle = 270;

        anim = new BarAnimation();
        anim.setDuration(2000);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mMaxRadius = (Math.min(mWidth, mHeight) - (int) (circleWidth * 3)) / 2;
        // 计算圆的起点
        // 左边背景圆起点
        circleRadiusX = (mWidth - mMaxRadius * 2) / 2;
        // 顶部背景圆起点
        circleRadiusY = (mHeight - mMaxRadius * 2) / 2;
        // 画图矩形区域——左边起点，底部起点，左边起点+直径，顶部起点+直径

        mCircleX = circleRadiusX + mMaxRadius;
        mCircleY = circleRadiusY + mMaxRadius;
        mRectangle = new RectF(circleRadiusX, circleRadiusY, 2 * mMaxRadius + circleRadiusX, 2 * mMaxRadius + circleRadiusY);

        canvas.drawArc(mRectangle, -225, mMaxAngle, false, mDefaultPaint);
        canvas.drawArc(mRectangle, -225, mCurrentAngle, false, mColorPaint);

        drawThumbnail(canvas);
        timeString = mCount + "";

        timePaint.getTextBounds(timeString, 0, timeString.length(), timeRect);
        timeTextPaint.getTextBounds(timeTextString, 0, timeTextString.length(), timeTextRect);
        infoPaint.getTextBounds(infoString, 0, infoString.length(), infoTextRect);
        //画时间
        canvas.drawText(timeString, mCircleX - (timeRect.width() * 1.1f + timeTextRect.width()) / 2, mCircleY + timeRect.height() / 2, timePaint);
        //画分钟
        canvas.drawText(timeTextString, mCircleX + timeRect.width() / 3f, mCircleY + timeRect.height() / 2, timeTextPaint);
        //画描述
        canvas.drawText(infoString, mCircleX - infoTextRect.width() / 2, mCircleY - (textWidth + infoWidth), infoPaint);
    }


    /**
     * 画指示器
     */
    private void drawThumbnail(Canvas canvas) {
        float p = mCurrentAngle - 135;
        thumbX = mCircleX + getRealCosX(mCurrentAngle, mMaxRadius + DensityUtil.dp2px(5));
        thumbY = mCircleY + getRealSinY(mCurrentAngle, mMaxRadius + DensityUtil.dp2px(5));
        if (mBitmap != null) {
            //计算
            int bmpWidth = mBitmap.getWidth();
            int bmpHeight = mBitmap.getHeight();
            canvas.save();
            canvas.translate(thumbX - (float) bmpWidth / 2.0f, thumbY - (float) bmpHeight / 2.0f);
            mMatrix.setRotate(p, (float) bmpWidth / 2.0f, (float) bmpHeight / 2.0f);
            canvas.drawBitmap(mBitmap, mMatrix, null);
            canvas.restore();

        }
    }

    /**
     * 计算x轴的偏移位置
     *
     * @param angle
     * @param radius
     * @return
     */
    private float getRealCosX(float angle, float radius) {
        if (angle < 45) {
            return -getCosX(45 - angle, radius);
        } else if (angle < 135) {
            return -getCosX(angle - 45, radius);
        } else if (angle < 225) {
            return getCosX(225 - angle, radius);
        } else if (angle <= 270) {
            return getCosX(angle - 225, radius);
        } else {
            return -getCosX(45 - angle, radius);
        }
    }

    /**
     * 计算Y轴的偏移位置
     *
     * @param angle
     * @param radius
     * @return
     */
    private float getRealSinY(float angle, float radius) {
        if (angle < 45) {
            return getSinY(45 - angle, radius);
        } else if (angle < 135) {
            return -getSinY(angle - 45, radius);
        } else if (angle < 225) {
            return -getSinY(225 - angle, radius);
        } else if (angle <= 270) {
            return getSinY(angle - 225, radius);
        } else {
            return getSinY(45 - angle, radius);
        }

    }

    private float getCosX(float diggre, float radius) { //diggre  0-90 第一象限角度计算
        return (float) (radius * Math.cos(Math.toRadians(diggre)));
    }

    private float getSinY(float diggre, float radius) { //diggre  0-90
        return (float) (radius * Math.sin(Math.toRadians(diggre)));
    }


    public void startCustomAnimation() {
        this.startAnimation(anim);
    }

    public void setText(long time) {
        mCurrentTime = time;
        if (time >= mMaxTime) {
            mPaintAngle = mMaxAngle;
        } else {
            mPaintAngle = (float) time / (float) mMaxTime * mMaxAngle;
        }
        this.startAnimation(anim);
    }

    public long getCurrentTime() {
        return mCurrentTime;
    }

    public class BarAnimation extends Animation {
        /**
         * Initializes expand collapse animation, has two types, collapse (1)
         * and expand (0).
         * The view to animate
         * The type of animation: 0 will expand from gone and 0 size
         * to visible and layout size defined in xml. 1 will collapse
         * view and set to gone
         */
        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mCurrentAngle = interpolatedTime * mPaintAngle;
                mCount = (long) (interpolatedTime * mCurrentTime);
            } else {
                mCurrentAngle = mPaintAngle;
                mCount = mCurrentTime;
            }
            postInvalidate();
        }
    }
}
