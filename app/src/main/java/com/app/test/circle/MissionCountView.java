package com.app.test.circle;

import android.content.Context;
import android.graphics.Canvas;
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


public class MissionCountView extends View {
    private String infoString = "目标完成0%";
    private final String timeTextString = "关";
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
     * 分钟文字的画笔
     */
    private Paint circlePaint;
    /**
     * 分钟文字的画笔
     */
    private Paint backPaint;
    /**
     * 圆弧画笔的宽度
     */
    private float circleWidth;
    /**
     * 分钟字体的大小
     */
    private float infoWidth;
    private float textWidth;
    private int mText;
    private int mCount;
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
     * 分钟的大小
     */
    private int mTimeSize;
    /**
     * 动画
     */
    private BarAnimation anim;
    /**
     * VIEW的宽度
     */
    private float mWidth;
    /**
     * VIEW的高度
     */
    private float mHeight;
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
    private float mMaxRadius;
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
    private String timeString = "";

    public MissionCountView(Context context) {
        super(context);
        init(null, 0);
    }

    public MissionCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MissionCountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        circleWidth = DensityUtil.dp2px(5);
        textWidth = DensityUtil.dp2px(15);
        infoWidth = DensityUtil.dp2px(16);
        mTimeSize = DensityUtil.dp2px(54);


        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setColor(0xFF08E4B3);//绿
        mColorPaint.setStyle(Style.STROKE);
        mColorPaint.setStrokeWidth(circleWidth);


        mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultPaint.setColor(0x4DFFFFFF);
        mDefaultPaint.setStyle(Style.STROKE);
        mDefaultPaint.setStrokeWidth(circleWidth);


        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        timePaint.setColor(0xFFFFFFFF);
        timePaint.setStyle(Style.FILL_AND_STROKE);
        timePaint.setTextAlign(Align.LEFT);
        timePaint.setTextSize(mTimeSize);


        timeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        timeTextPaint.setColor(0xCCFFFFFF);
        timeTextPaint.setStyle(Style.FILL_AND_STROKE);
        timeTextPaint.setTextAlign(Align.LEFT);
        timeTextPaint.setTextSize(infoWidth);


        infoPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        infoPaint.setColor(0xCCFFFFFF);
        infoPaint.setStyle(Style.FILL_AND_STROKE);
        infoPaint.setTextAlign(Align.LEFT);
        infoPaint.setTextSize(textWidth);


        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        circlePaint.setColor(0xFF08E4B3);
        circlePaint.setStyle(Style.FILL_AND_STROKE);
        circlePaint.setTextAlign(Align.LEFT);
        circlePaint.setTextSize(textWidth);


        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        backPaint.setColor(getResources().getColor(R.color.transparent_50));
        backPaint.setStyle(Style.FILL_AND_STROKE);
        backPaint.setTextAlign(Align.LEFT);
        backPaint.setTextSize(circleWidth);


        mText = 0;
        mMaxAngle = 270;

        anim = new BarAnimation();
        anim.setDuration(2000);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mMaxRadius = Math.min(mWidth, mHeight) / 2 - circleWidth * 2;
        // 计算圆的起点
        // 左边背景圆起点
        circleRadiusX = (mWidth - mMaxRadius * 2) / 2;
        // 顶部背景圆起点
        circleRadiusY = (mHeight - mMaxRadius * 2) / 2;
        // 画图矩形区域——左边起点，底部起点，左边起点+直径，顶部起点+直径

        mCircleX = mWidth / 2;
        mCircleY = mHeight / 2;
        mRectangle = new RectF(circleRadiusX, circleRadiusY, 2 * mMaxRadius + circleRadiusX, 2 * mMaxRadius + circleRadiusY);
        canvas.drawCircle(mCircleX, mCircleY, mMaxRadius, backPaint);
        canvas.drawArc(mRectangle, -225, mMaxAngle, false, mDefaultPaint);
        canvas.drawArc(mRectangle, -225, mCurrentAngle, false, mColorPaint);


        timeString = mCount + "";

        timePaint.getTextBounds(timeString, 0, timeString.length(), timeRect);
        timeTextPaint.getTextBounds(timeTextString, 0, timeTextString.length(), timeTextRect);
        infoPaint.getTextBounds(infoString, 0, infoString.length(), infoTextRect);
        //画时间
        canvas.drawText(timeString, mCircleX - (timeRect.width() + timeTextRect.width()) / 2, mCircleY + timeRect.height() / 4, timePaint);
        //画"关"
        canvas.drawText(timeTextString, mCircleX + timeRect.width() / 2, mCircleY + timeRect.height() / 4, timeTextPaint);
        //画描述
        canvas.drawText(infoString, mCircleX - infoTextRect.width() / 2, mCircleY - textWidth * 3 + circleWidth, infoPaint);

        drawThumbnail(canvas);
    }


    /**
     * 画指示器
     */
    private void drawThumbnail(Canvas canvas) {
        float angle;
        if (mCurrentAngle < 5) {
            angle = 5;
        } else if (mCurrentAngle > 265) {
            angle = 265;
        } else {
            angle = mCurrentAngle;
        }
        thumbX = (float) (mCircleX + getRealCosX(angle, mMaxRadius));
        thumbY = (float) (mCircleY + getRealSinY(angle, mMaxRadius));
        circlePaint.setColor(0x2608E4B3);
        canvas.drawCircle(thumbX, thumbY, 18, circlePaint);
        circlePaint.setColor(0x8008E4B3);
        canvas.drawCircle(thumbX, thumbY, 13, circlePaint);
        circlePaint.setColor(0xFF08E4B3);
        canvas.drawCircle(thumbX, thumbY, 8, circlePaint);
    }

    /**
     * 计算x轴的偏移位置
     *
     * @param angle
     * @param radius
     * @return
     */
    private double getRealCosX(float angle, float radius) {
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
    private double getRealSinY(float angle, float radius) {
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

    private double getCosX(float diggre, float radius) { //diggre  0-90 第一象限角度计算
        return (radius * Math.cos(Math.toRadians(diggre)));
    }

    private double getSinY(float diggre, float radius) { //diggre  0-90
        return (radius * Math.sin(Math.toRadians(diggre)));
    }


    public void startCustomAnimation() {
        this.startAnimation(anim);
    }

    public void setDate(int percentComplete, int missionCount, int curMission) {
        infoString = "目标完成" + percentComplete + "%";
        mText = curMission;
//        if (missionCount <= curMission) {
//            mPaintAngle = mMaxAngle;
//        } else {
//            mPaintAngle = (float) curMission / (float) missionCount * mMaxAngle;
//        }
        mPaintAngle = (float) percentComplete / (float) 100 * mMaxAngle;
        this.startAnimation(anim);
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
                mCount = (int) (interpolatedTime * mText);
            } else {
                mCurrentAngle = mPaintAngle;
                mCount = mText;
            }
            postInvalidate();
        }
    }
}
