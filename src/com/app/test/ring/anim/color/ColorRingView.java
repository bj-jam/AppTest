package com.app.test.ring.anim.color;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.app.test.R;
import com.app.test.util.DisplayUtil;

/**
 * 饼图
 */
public class ColorRingView extends View {

    private Resources res;
    private Paint paint;
    /**
     * 数值组
     */
    private int[] numbers;
    /**
     * 比值组
     */
    private String[] percents;
    /**
     * 数值总量
     */
    private int sum = 0;
    /**
     * 百分比数值色值组
     */
    private int[] textColors;
    /**
     * 圆形半径
     */
    private float radius = 0;
    /**
     * 圆形左边画图起点
     */
    private float radiusX = 0;
    /**
     * 圆形顶部画图起点
     */
    private float radiusY = 0;
    /**
     * 圆形矩形区域
     */
    private RectF circleRectF;
    /**
     * 圆形背景矩形区域
     */
    private RectF circleRectFBG;
    /**
     * 文字点坐标
     */
    private float[][] coords;
    /**
     * 正常时文字大小
     */
    private int textSize;
    /**
     * 最小弧度时文字大小
     */
    private int textSizeMin;
    /**
     * 最小弧度时文字到圆边的距离
     */
    private int dis = 10;
    /**
     * 最小度数
     */
    private float minAngle = 15;
    /**
     * 数值占总数弧度数集合
     */
    private float[] radians;
    /**
     * 无数据是文字大小
     */
    private int textSize_;
    /**
     * 文字矩形区域
     */
    private Rect textRect;
    /**
     * 数组值集合不为0的有效个数
     */
    private int count = 0;
    /**
     * 圆形图占控件的比数
     */
    private int grade = 3;
    private BarAnimation anim;
    private int paintWidth;

    public ColorRingView(Context context) {
        super(context);
        init();
    }

    public ColorRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // TODO 初始化
        if (isInEditMode()) {
            // return;
        }
        res = getResources();
        // 文字大小
        textSize = res.getDimensionPixelSize(R.dimen.dp10);
        textSize_ = res.getDimensionPixelSize(R.dimen.dp16);
        textSizeMin = res.getDimensionPixelSize(R.dimen.dp10);
        paintWidth = DisplayUtil.getInstance().dip2px(10);
        dis = 10;

        textRect = new Rect();
        circleRectF = new RectF();

        paint = new Paint();
        // 填充风格
        paint.setStyle(Paint.Style.FILL);
        // 设置是否抗锯齿
        paint.setAntiAlias(true);

        anim = new BarAnimation();
        anim.setDuration(2000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius == 0) {
            int w = getWidth();
            int h = getHeight();
            if (w > h) {
                radius = h / grade;
            } else {
                radius = w / grade;
            }
            radiusX = (w - radius * 2) / 2;// 左边画图起点
            radiusY = (h - radius * 2) / 2;// 顶部画图起点
            // 画图矩形区域——左边起点，底部起点，左边起点+直径，顶部起点+直径
            circleRectFBG = new RectF(radiusX, radiusY, 2 * radius + radiusX, 2
                    * radius + radiusY);
        }
        if (sum > 0) {
            drawPie(canvas);
            drawInfo(canvas);
        } else {
            drawNoData(canvas);
        }
    }

    /**
     * 设置百分比数值色值组
     */
    public void setTextColors(int[] colors) {
        this.textColors = colors;
        initCoords();
        this.startAnimation(anim);
    }

    /**
     * 正常时文字大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 没数据时文字大小
     */
    public void setTextSize_(int textSize) {
        this.textSize_ = textSize;
    }

    /**
     * 最小弧度时文字大小
     */
    public void setTextSizeMin(int textSizeMin) {
        this.textSizeMin = textSizeMin;
    }

    /**
     * 最小弧度时文字到圆边的距离
     */
    public void setDis(int dis) {
        this.dis = dis;
    }

    /**
     * 最小弧度数
     */
    public void setMinAngle(float minAngle) {
        this.minAngle = minAngle;
    }

    /**
     * 设置圆形图占控件的比数
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * 设置饼图数值组
     */
    public void setNumbers(int[] numbers) {
        // TODO 设置饼图数值组
        this.numbers = numbers;
        sum = 0;
        count = 0;
        percents = new String[numbers.length];
        coords = new float[numbers.length][3];
        radians = new float[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] != 0) {// 不为0的有效数组个数，为0不用算比值
                count++;
            }
            sum += numbers[i];// 数组值总量
        }

        float value = 0;// 用于算数值最后一个百分比值
        DecimalFormat df = new DecimalFormat("###.00");// 百分位保留两位小数
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) {// 非有效值数值
                continue;
            }
            if (i == numbers.length - 1) {// 最后一位不用算
                float v = Float.parseFloat(df.format((100 - value)));
                int v_ = (int) v;
                if (v_ == v) {// 去掉小数点末尾带0
                    percents[i] = v_ + "%";
                } else {
                    percents[i] = v + "%";
                }
            } else {
                float percent = 1f * numbers[i] / sum * 100;// 算数值占比百分值
                float v = Float.parseFloat(df.format(percent));// 格式化显示的百分比
                int v_ = (int) v;
                if (v_ == v) {// 去掉小数点末尾带0
                    percents[i] = v_ + "%";
                } else {
                    percents[i] = v + "%";
                }
                value += v;// 比值相加
            }
        }
    }

    /**
     * 计算出弧度数值组和百分比信息坐标
     */
    private void initCoords() {
        float start = 0;// 用于算弧度值最后一个百分比值
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) {// 非有效值数值
                continue;
            }
            if (i == numbers.length - 1) {// 最后一位不用算
                radians[i] = 360 - start;
                if (radians[i] <= minAngle) {// 小于最小弧度值
                    radians[i] = minAngle;
                    coords[i][2] = 1;
                }
            } else {
                radians[i] = 360f * numbers[i] / sum;// 算饼图占比百分值
                if (count == 3) {// 有效数值为3
                    if (i == 0) {
                        if (radians[i] < minAngle) {// 小于最小弧度值
                            radians[i] = minAngle;
                            coords[i][2] = 1;
                        } else if (radians[i] > 360 - 2 * minAngle) {// 大于最大弧度值
                            radians[i] = 360 - 2 * minAngle;
                        }
                    } else if (i == 1) {
                        if (radians[i] < minAngle) {// 小于最小弧度值
                            radians[i] = minAngle;
                            coords[i][2] = 1;
                        } else if (radians[i] > 360 - (radians[i - 1] + minAngle)) {// 大于最大弧度值
                            radians[i] = 360 - (radians[i - 1] + minAngle);
                        }
                    }
                } else if (count == 2) {
                    if (radians[i] < minAngle) {// 小于最小弧度值
                        radians[i] = minAngle;
                        coords[i][2] = 1;
                    } else if (radians[i] > 360 - minAngle) {// 大于最大弧度值
                        radians[i] = 360 - minAngle;
                    }
                }
            }

            // 目标弧度
            float goalAngle = -90 + start + radians[i] / 2;
            start += radians[i];// 弧度值累加
            // 目标值 sinθ=x/radius,cosθ=y/radius
            // 对边
            float goalY = new BigDecimal(Math.sin(goalAngle * Math.PI / 180))
                    .setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            float y = (radius + dis) * goalY;
            // 邻边
            float goalX = new BigDecimal(Math.cos(goalAngle * Math.PI / 180))
                    .setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            float x = (radius + dis) * goalX;
            coords[i][0] = radiusX + radius + x;
            coords[i][1] = radiusY + radius + y;
        }
    }

    /**
     * 当前弧度
     */
    private float mAnglePer;

    /**
     * 画圆饼图
     */
    private void drawPie(Canvas canvas) {
        // TODO 画圆饼图

        // 白色背景
        paint.setColor(Color.parseColor("#FFFFFFFF"));
        // 画扇形——矩形区域，起始点，所占度数，填充扇形，画笔
        canvas.drawArc(circleRectFBG, 0, 360, true, paint);

        // 画占比图
        circleRectF.set(radiusX + paintWidth / 2, radiusY + paintWidth / 2, 2
                * radius + radiusX - paintWidth / 2, 2 * radius + radiusY
                - paintWidth / 2);

        paint.setStrokeMiter(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(paintWidth);
        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND,或方形样式Cap.SQUARE
        paint.setStrokeCap(Cap.ROUND);
        // 计算需要变化颜色的次数
        // 默认就要设置一次颜色
        int count = 1;
        float angler = 0f;
        for (int i = 0; i < numbers.length; i++) {
            // 投票数为0就跳过画饼图
            angler += radians[i];
            if (angler >= mAnglePer) {
                count += i;
                break;
            }
        }
        // 画扇形
        // 已经画过的弧度
        float alreadyAngler = 0f;
        for (int i = 0; i < count; i++) {
            // 画笔的宽度
            paint.setColor(textColors[i]);
            // 扇形起点弧度，下次画的时候累加上次弧度数
            // 画扇形——矩形区域，起始点(已画度数)，所占度数，填充扇形，画笔
            canvas.drawArc(circleRectF, -90 + alreadyAngler, mAnglePer
                    - alreadyAngler, false, paint);
            alreadyAngler += radians[i];
        }
    }

    /**
     * 画百分比提示信息
     */
    private void drawInfo(Canvas canvas) {
        // TODO 画百分比提示信息
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColors[0]);
        paint.setStrokeWidth(1);
        if (count == 1) {// 数据为100%时
            paint.setTextSize(textSize_);
            paint.getTextBounds("100%", 0, "100%".length(), textRect);
            float textL = radiusX + radius - textRect.width() / 2;
            float textB = radiusY + radius + textRect.height() / 2;
            canvas.drawText("100%", textL, textB, paint);
            return;
        }
        float start = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                continue;
            }
            paint.setColor(textColors[i]);
            paint.setTextSize(textSize);
            if (coords[i][2] == 1) {
                paint.setTextSize(textSizeMin);
            }
            paint.getTextBounds(percents[i], 0, percents[i].length(), textRect);
            float textL = 0, textB = 0;
            float goalAngle = -90 + start + radians[i] / 2;
            start += radians[i];
            if (goalAngle <= 0) {
                textL = coords[i][0];
                textB = coords[i][1];
            } else if (goalAngle <= 90) {
                textL = coords[i][0];
                textB = coords[i][1] + textRect.height();
            } else if (goalAngle <= 180) {
                textL = coords[i][0] - textRect.width();
                textB = coords[i][1] + textRect.height();
            } else {
                textL = coords[i][0] - textRect.width();
                textB = coords[i][1];
            }
            textL = textL >= 0 ? textL : 0;
            textB = textB >= 0 ? textB : 0;
            textL = textL + textRect.width() > getWidth() ? getWidth()
                    - textRect.width() : textL;
            textB = textB + textRect.height() > getHeight() ? getHeight()
                    - textRect.height() : textB;
            canvas.drawText(percents[i], textL, textB, paint);
        }
    }

    /**
     * 没数据时的显示图文
     */
    private void drawNoData(Canvas canvas) {
        paint.setStrokeMiter(1);
        paint.setColor(Color.parseColor("#FF999999"));
        // 画扇形——矩形区域，起始点，所占度数，填充扇形，画笔
        canvas.drawArc(circleRectFBG, 0, 360, true, paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setTextSize(textSize_);
        paint.getTextBounds("暂无数据", 0, "暂无数据".length(), textRect);
        float textL = radiusX + radius - textRect.width() / 2;
        float textB = radiusY + radius + textRect.height() / 2;
        canvas.drawText("暂无数据", textL, textB, paint);
    }

    public class BarAnimation extends Animation {
        /**
         * Initializes expand collapse animation, has two types, collapse (1)
         * and expand (0).
         *
         * @param view The view to animate
         * @param type The type of animation: 0 will expand from gone and 0 size
         *             to visible and layout size defined in xml. 1 will collapse
         *             view and set to gone
         */
        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mAnglePer = interpolatedTime * 360;
            } else {
                mAnglePer = 360;
            }
            postInvalidate();
        }
    }
}
