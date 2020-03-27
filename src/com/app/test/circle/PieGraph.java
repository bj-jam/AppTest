package com.app.test.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.app.test.util.DensityUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 */

public class PieGraph extends View {

    /**
     * 饼状图中心x的位置
     */
    private float mCX;
    /**
     * 饼状图的半径
     */
    private float mCY;
    /**
     * 饼状图的半径
     */
    private float mPieRadius;
    /**
     * 文字大小
     */
    private int mTextSize;
    /**
     * 文字大小
     */
    private int mBigSize;
    /**
     * 文字大小
     */
    private int mSmallSize;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 文字的高度
     */
    private float mTextHeight;
    /**
     * 文字的基准线
     */
    private float mTextBottom;
    /**
     * 画文字的时候先在原有的圆上面延伸出来的长度
     */
    private float mMarkerLine1;
    /**
     * mMarkerLine1的基础上延伸出来的水平线的长度
     */
    private float mMarkerLine2;
    /**
     * 绘制类型字体的画笔
     */
    private TextPaint mTextPaint;
    /**
     * 绘制类型字体的画笔
     */
    private TextPaint mBigPaint;
    /**
     * 绘制类型字体的画笔
     */
    private TextPaint mSmallPaint;
    /**
     * 画文字连接线的画笔
     */
    private Paint mLinePaint;
    /**
     * 饼状图信息列表
     */
    private List<PieDataHolder> pieDataHolders;
    /**
     * 饼状图正常时候那个矩形区域
     */
    private RectF mPieNormalRectF;
    /**
     * 饼状图的画笔
     */
    private Paint mPiePaint;
    /**
     * 分割线的画笔
     */
    private Paint mCutLinePaint;
    /**
     * 当前要画的文字的区域
     */
    private Rect mCurrentTextRect;
    /**
     * 确定比例保存几位小数
     */
    private DecimalFormat mDecimalFormat;

    /**
     * 文字矩形区域
     */
    private Rect mBigTextRect;
    /**
     * 文字矩形区域
     */
    private Rect mSmallTextRect;


    public PieGraph(Context context) {
        this(context, null);
    }

    public PieGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
        initData();
        initTextMetrics();
    }

    private void initData() {
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mBigPaint = new TextPaint();
        mBigPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBigPaint.setColor(0xff333333);
        mBigPaint.setTextSize(mBigSize);

        mSmallPaint = new TextPaint();
        mSmallPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mSmallPaint.setColor(0xff333333);
        mSmallPaint.setTextSize(mSmallSize);


        pieDataHolders = new ArrayList<>();
        mPieNormalRectF = new RectF();

        mPiePaint = new Paint();
        mPiePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.STROKE);
        mPiePaint.setStrokeWidth(80);

        mCutLinePaint = new Paint();
        mCutLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mCutLinePaint.setStyle(Paint.Style.STROKE);
        mCutLinePaint.setStrokeWidth(10);
        mCutLinePaint.setColor(0xffffffff);

        mCurrentTextRect = new Rect();

        mLinePaint = new Paint();
        mLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);

        mDecimalFormat = new DecimalFormat("0.00");

        mBigTextRect = new Rect();
        mSmallTextRect = new Rect();
    }

    /**
     * 获取xml里面定义的属性
     */
    private void initAttrs() {

        mPieRadius = DensityUtil.dp2px(80);
        mTextSize = DensityUtil.dp2px(14);
        mBigSize = DensityUtil.dp2px(16);
        mSmallSize = DensityUtil.dp2px(10);
        mTextColor = 0xff666666;
        mMarkerLine1 = DensityUtil.dp2px(10);
        mMarkerLine2 = DensityUtil.dp2px(65);
    }

    /**
     * 得到位置的高度，基准线啥啥的
     */
    private void initTextMetrics() {
        mTextPaint.setTextSize(mTextSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextBottom = fontMetrics.bottom;
    }

    /**
     * 测量控件大小,这里宽度我们不测了，只是去测量高度，宽度直接用父控件传过来的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 具体的绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPieRectF();
        drawPie(canvas);
        drawText(canvas);
    }

    /**
     * 这里呢，去得到画饼状图的时候的那个矩形
     */
    private void initPieRectF() {
        mPieNormalRectF.left = getWidth() / 2 - mPieRadius;
        mPieNormalRectF.top = getHeight() / 2 - mPieRadius;
        mPieNormalRectF.right = mPieNormalRectF.left + mPieRadius * 2;
        mPieNormalRectF.bottom = mPieNormalRectF.top + mPieRadius * 2;
        mCX = getWidth() / 2;
        mCY = getHeight() / 2;
    }

    /**
     * 画饼状图
     */
    private void drawPie(Canvas canvas) {
        if (pieDataHolders == null || pieDataHolders.size() <= 0) {
            return;
        }
        for (int i = 0; i < pieDataHolders.size(); i++) {
            PieDataHolder pieDataHolder = pieDataHolders.get(i);
            mPiePaint.setColor(pieDataHolder.mColor);
            if (pieDataHolder.mSweepAngel == 0) {
                // 0度的不画
                continue;
            }
            // 正常画圆弧
            canvas.drawArc(mPieNormalRectF, pieDataHolder.mStartAngel, pieDataHolder.mSweepAngel, false, mPiePaint);
        }
        mCutLinePaint.setColor(0xffffffff);
        for (int i = 0; i < pieDataHolders.size(); i++) {
            PieDataHolder pieDataHolder = pieDataHolders.get(i);
            if (pieDataHolder.mSweepAngel == 0) {
                // 0度的不画
                continue;
            }
            float middle = (pieDataHolder.mStartAngel) % 360;
            if (middle < 0) {
                middle += 360;
            }
            Path linePath = new Path();

            linePath.close();
            linePath.moveTo(mCX, mCY);
            // 找到圆边缘上的点
            final float startX = (float) (getWidth() / 2 + (mPieRadius + 50) * Math.cos(Math.toRadians(middle)));

            final float startY = (float) (getHeight() / 2 + (mPieRadius + 50) * Math.sin(Math.toRadians(middle)));

            linePath.lineTo(startX, startY);


            canvas.drawPath(linePath, mCutLinePaint);
        }
        mCutLinePaint.setColor(0XffF6F6F6);

        canvas.drawCircle(mCX, mCY, mPieRadius - 40, mCutLinePaint);
    }


    /**
     * 画文字
     */
    private void drawText(Canvas canvas) {
        mTextPaint.setColor(mTextColor);
        mCurrentTextRect.setEmpty();
        for (int index = 0; index < pieDataHolders.size(); index++) {
            PieDataHolder pieDataHolder = pieDataHolders.get(index);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setColor(pieDataHolder.mColor);
            if (pieDataHolder.mSweepAngel == 0) {
                // 没有比例的不画
                continue;
            }
            String textMarker = pieDataHolder.mMarker;
            if (textMarker == null) {
                continue;
            }
            float textWidth = mTextPaint.measureText(textMarker);
            // 找到圆弧一半的位置，要往这个方向拉出去
            float middle = (pieDataHolder.mStartAngel + pieDataHolder.mSweepAngel / 2) % 360;
            if (middle < 0) {
                middle += 360;
            }
            //画折线
            Path linePath = new Path();
            linePath.close();
            // 找到圆边缘上的点
            final float startX = (float) (getWidth() / 2 + (mPieRadius + 60) * Math.cos(Math.toRadians(middle)));
            final float startY = (float) (getHeight() / 2 + (mPieRadius + 60) * Math.sin(Math.toRadians(middle)));
            linePath.moveTo(startX, startY);
            final float x = (float) (getWidth() / 2 + (mMarkerLine1 + mPieRadius + 60) * Math.cos(Math.toRadians(middle)));
            final float y = (float) (getHeight() / 2 + (mMarkerLine1 + mPieRadius + 60) * Math.sin(Math.toRadians(middle)));
            linePath.lineTo(x, y);

            float landLineX;
            // 左边 右边的判断
            if (270f > middle && middle > 90f) {
                landLineX = x - mMarkerLine2;
            } else {
                landLineX = x + mMarkerLine2;
            }
            linePath.lineTo(landLineX, y); // 画文字线先确定了
            canvas.drawPath(linePath, mLinePaint);

            mLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(startX, startY, 6, mLinePaint);

            //画浅颜色的线
            float bStartX;
            // 左边 右边的判断
            if (270f > middle && middle > 90f) {
                bStartX = x - 10;
            } else {
                bStartX = x + 10;
            }
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setColor(pieDataHolder.mBottomColor);
            Path linePath1 = new Path();
            linePath1.close();
            linePath1.moveTo(bStartX, y + 4);
            linePath1.lineTo(landLineX, y + 4); // 画文字线先确定了
            canvas.drawPath(linePath1, mLinePaint);

            // 继续去确定文字的位置
            if (270f > middle && middle > 90f) {
                // 圆的右边
                // 文字的区域
                mCurrentTextRect.top = (int) (y + mTextHeight / 3);
                mCurrentTextRect.left = (int) (landLineX);
                mCurrentTextRect.bottom = (int) (mCurrentTextRect.top + mTextHeight);
                mCurrentTextRect.right = (int) (mCurrentTextRect.left + textWidth);
            } else {
                // 圆的左边
                // 文字的区域
                mCurrentTextRect.top = (int) (y + mTextHeight / 3);
                mCurrentTextRect.left = (int) (landLineX - textWidth);
                mCurrentTextRect.bottom = (int) (mCurrentTextRect.top + mTextHeight);
                mCurrentTextRect.right = mCurrentTextRect.left;
            }
            canvas.drawText(textMarker, mCurrentTextRect.left, mCurrentTextRect.top + mTextHeight - mTextBottom, mTextPaint);

            mBigPaint.getTextBounds(pieDataHolder.bigInfo, 0, pieDataHolder.bigInfo.length(), mBigTextRect);
            mSmallPaint.getTextBounds(pieDataHolder.smallInfo, 0, pieDataHolder.smallInfo.length(), mSmallTextRect);

            float textX;
            float textY = y - mBigTextRect.height() / 3;
            if (270f > middle && middle > 90f) {
                // 圆的左边文字的区域
                textX = landLineX;
            } else {
                if (pieDataHolder.type == -1) {
                    textX = landLineX - mBigTextRect.width() - mSmallTextRect.width() - 5;
                } else
                    textX = landLineX - mBigTextRect.width();
            }
            float sTextX;
            if (270f > middle && middle > 90f) {
                // 圆的左边文字的区域
                sTextX = landLineX + mBigTextRect.width() + 5;
            } else {
                sTextX = landLineX - mSmallTextRect.width();
            }
            canvas.drawText(pieDataHolder.bigInfo, textX, textY, mBigPaint);
            if (pieDataHolder.type == -1) {
                canvas.drawText(pieDataHolder.smallInfo, sTextX, textY, mSmallPaint);
            }


        }
    }


    /**
     * 设置饼状图数据(给外部调用的)
     */
    public void setPieData(List<PieDataHolder> pieDataList) {
        if (pieDataList == null || pieDataList.size() == 0) {
            return;
        }
        pieDataHolders.clear();
        pieDataHolders.addAll(pieDataList);
        // 计算每个饼状图的比例，开始角度，扫过的角度
        float sum = 0;
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            sum += pieDataHolder.mAllScore;
        }
        float preSum = 0; // 当前位置之前的总的值，算开始角度用的，总共360
        for (int index = 0; index < pieDataList.size(); index++) {
            PieDataHolder pieDataHolder = pieDataHolders.get(index);
            pieDataHolder.mPosition = index;
            pieDataHolder.mRatio = Float.parseFloat(mDecimalFormat.format(pieDataHolder.mAllScore / sum));
            pieDataHolder.mStartAngel = preSum / sum * 360f;
            preSum += pieDataHolder.mAllScore;
            if (index == pieDataList.size() - 1) {
                // 如果是最后一个 目的是避免精度的问题
                pieDataHolder.mSweepAngel = 360 - pieDataHolder.mStartAngel;
            } else {
                pieDataHolder.mSweepAngel = pieDataHolder.mRatio * 360;
            }
        }
        // 这里要调整一下比例，因为精度的原因，有的时候可能加起来不是100%，解决办法呢就是最大的比例直接用100减掉其他的
        int maxRatioPosition = 0;
        float maxRatioValue = 0;
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            if (maxRatioValue < pieDataHolder.mRatio) {
                maxRatioValue = pieDataHolder.mRatio;
                maxRatioPosition = pieDataHolder.mPosition;
            }
        }
        float sumWithOutMax = 0;
        PieDataHolder maxHolder = null;
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            if (pieDataHolder.mPosition != maxRatioPosition) {
                sumWithOutMax += pieDataHolder.mRatio;
            } else {
                maxHolder = pieDataHolder;
            }
        }
        if (maxHolder != null) {
            maxHolder.mRatio = 1 - Float.parseFloat(mDecimalFormat.format(sumWithOutMax));
        }
        invalidate();
    }

    /**
     * 饼状图里面每个饼的信息
     */
    public static final class PieDataHolder {

        /**
         * 具体的值
         */
        private float mAllScore;

        /**
         * 比例
         */
        private float mRatio;

        /**
         * 颜色
         */
        private int mColor;
        /**
         * 颜色
         */
        private int mBottomColor;

        /**
         * 文字标记
         */
        private String mMarker;

        /**
         * 起始弧度
         */
        private float mStartAngel;

        /**
         * 扫过的弧度
         */
        private float mSweepAngel;

        /**
         * 位置下标
         */
        private int mPosition;

        private int type;
        private String bigInfo;
        private String smallInfo;

        public PieDataHolder(int allScore, int color, int mBottomColor, String label, int studentScore, int type) {
            mAllScore = allScore;
            mColor = color;
            mMarker = label;
            this.mBottomColor = mBottomColor;
            this.type = type;

            if (type == -1) {
                bigInfo = studentScore + "";
            } else if (type == 1) {
                bigInfo = "???";
            } else if (type == 2) {
                bigInfo = "????";
            } else if (type == 3) {
                bigInfo = "????";
            }
            smallInfo = "分/" + allScore + "分";
        }
    }
}
