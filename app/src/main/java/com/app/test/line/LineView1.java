package com.app.test.line;

import java.util.Date;

import com.app.test.R;
import com.app.test.test.CalendarUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 */
public class LineView1 extends View {

    private Resources res;
    private Paint paint;
    /**
     * 控件宽高
     */
    private int w = 0, h = 0;
    /**
     * 背景线区域宽度
     */
    private int drawW = 0;
    /**
     * 背景线区域高度
     */
    private int drawH = 0;
    /**
     * 背景线区域到控件边框横向边距
     */
    private int disLineW = 120;
    /**
     * 背景线区域到控件边框下边距
     */
    private int disLineH = 100;
    /**
     * 数据画线颜色
     */
    private int drawColor = 0;
    /**
     * 背景线颜色
     */
    private int lineColor = 0;
    /**
     * 背景线字体大小
     */
    private int lineSize = 14;
    /**
     * 文本字体大小
     */
    private int textSize = 0;
    /**
     * 数据数值组
     */
    private int[] numbers;
    /**
     * 数据数值组中最大值，用于纵坐标单位计算
     */
    private int maxNumber;
    /**
     * 纵坐标值组
     */
    private String[] numbersY;
    /**
     * 文本矩形工具，用于计算字体宽高
     */
    private Rect textRect;
    // 日历工具类
    private CalendarUtil calendarUtil;
    private String anyMonth = "";
    private boolean isAnyMonth = false;
    private boolean isCurveLine = false;// 是否是曲线图，false表示折线图，true表示曲线图并且填充

    public LineView1(Context context) {
        super(context);
        init();
    }

    public LineView1(Context context, AttributeSet attrs) {
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
        calendarUtil = new CalendarUtil();
        res = getResources();
        // 文字大小
        textSize = res.getDimensionPixelSize(R.dimen.dp10);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);// 去锯齿

        lineColor = res.getColor(R.color.text_color9);
        drawColor = res.getColor(R.color.course_text);

        numbersY = new String[]{"0", "0", "0", "0", "0", "0"};
        textRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (w == 0) {
            w = getWidth();
            h = getHeight();
            drawW = w - 2 * disLineW;
            drawH = h - disLineH;
        }
        if (numbers == null) {
            numbers = new int[30];
        }
        drawLine(canvas);
    }

    /**
     * 设置线状图数值组
     */
    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
        this.isAnyMonth = false;
        this.isCurveLine = false;

        maxNumber = 0;
        // 得到最大数组值
        for (int i = 0; i < numbers.length; i++) {
            if (maxNumber < numbers[i]) {
                maxNumber = numbers[i];
            }
        }
        // 得到纵坐标数组值
        int coordY[] = new int[]{ //
                0, 10, 20, 40, 80, 100, 2 * 100, 3 * 100, 6 * 100, 10 * 100, //
                20 * 100, 40 * 100, 80 * 100, 2 * 100 * 100, //
                8 * 100 * 100, 100 * 100 * 100 //
        };

        if (maxNumber == 0) {
            int disNum = coordY[1] / 5;
            for (int j = 1; j < numbersY.length; j++) {
                numbersY[j] = disNum * j + "";
            }
        } else {
            for (int i = 0; i < coordY.length - 1; i++) {
                if (maxNumber > coordY[i] && maxNumber <= coordY[i + 1]) {
                    // 5个纵向刻度
                    int disNum = coordY[i + 1] / 5;
                    for (int j = 1; j < numbersY.length; j++) {
                        numbersY[j] = disNum * j + "";
                    }
                }
            }
        }
    }

    /**
     * 设置线状图数值组
     */
    public void setNumbers(int[] numbers, boolean isAnyMonth, String month,
                           boolean isCurveLine) {
        this.numbers = numbers;
        this.anyMonth = month;
        this.isAnyMonth = isAnyMonth;
        this.isCurveLine = isCurveLine;

        maxNumber = 0;
        // 得到最大数组值
        for (int i = 0; i < numbers.length; i++) {
            if (maxNumber < numbers[i]) {
                maxNumber = numbers[i];
            }
        }
        // 得到纵坐标数组值
        int coordY[] = new int[]{ //
                0, 10, 20, 40, 80, 100, 2 * 100, 3 * 100, 6 * 100, 10 * 100, //
                20 * 100, 40 * 100, 80 * 100, 2 * 100 * 100, //
                8 * 100 * 100, 100 * 100 * 100 //
        };

        if (maxNumber == 0) {
            int disNum = coordY[1] / 5;
            for (int j = 1; j < numbersY.length; j++) {
                numbersY[j] = disNum * j + "";
            }
        } else {
            for (int i = 0; i < coordY.length - 1; i++) {
                if (maxNumber > coordY[i] && maxNumber <= coordY[i + 1]) {
                    // 5个纵向刻度
                    int disNum = coordY[i + 1] / 5;
                    for (int j = 1; j < numbersY.length; j++) {
                        numbersY[j] = disNum * j + "";
                    }
                }
            }
        }
    }

    /**
     * 画线状信息图
     */
    private void drawLine(Canvas canvas) {
        // 横坐标间隔单位长度
        float tempDis1 = drawW * 1f / (numbers.length - 1);
        canvas.drawLine(disLineW, 0, disLineW, drawH, paint);
        for (int i = 0; i < numbers.length; i++) {
            String info = "";
            // 竖线背景
            paint.setColor(lineColor);
            paint.setStrokeWidth(1);
            paint.setTextSize(lineSize);
            if (isAnyMonth) {// 任意月
                if (i % 5 == 4) {
                    if (i < 9) {
                        info = anyMonth + "-" + "0" + String.valueOf(i + 1);
                    } else {
                        info = anyMonth + "-" + String.valueOf(i + 1);
                    }
                } else if (i == 0) {
                    info = anyMonth + "-" + "01";
                }
            } else {// 本周或者最近30天
                if (numbers.length > 7) {// 数组大于7，表示是最近30天或者任意月
                    if (i % 5 == 0 || i == numbers.length - 1) {
                        info = calendarUtil.convertDateToString(
                                calendarUtil.getDateBefore(new Date(),
                                        numbers.length - i - 1)).substring(5,
                                10);
                    }
                } else {// 小于等于7表示星期数组
                    if (i + 1 == 1) {
                        info = "星期一";
                    } else if (i + 1 == 2) {
                        info = "星期二";
                    } else if (i + 1 == 3) {
                        info = "星期三";
                    } else if (i + 1 == 4) {
                        info = "星期四";
                    } else if (i + 1 == 5) {
                        info = "星期五";
                    } else if (i + 1 == 6) {
                        info = "星期六";
                    } else if (i + 1 == 7) {
                        info = "星期日";
                    }
                }
            }
            paint.setStrokeWidth(1);
            paint.setColor(lineColor);
            paint.setTextSize(textSize);
            paint.getTextBounds(info, 0, info.length(), textRect);
            canvas.drawText(info, disLineW + tempDis1 * i - textRect.width()
                    / 2, drawH + textRect.height() + 25, paint);
        }
        paint.setStrokeWidth(1);
        paint.setColor(lineColor);
        paint.setTextSize(textSize);
        if (numbers.length > 7) {
            paint.getTextBounds("日期", 0, "日期".length(), textRect);
            canvas.drawText("日期", w - disLineW + 50, drawH + textRect.height()
                    + 20, paint);
        } else {
            paint.getTextBounds("", 0, "".length(), textRect);
            canvas.drawText("", w - disLineW + 10, drawH + textRect.height(),
                    paint);

        }

        // 纵坐标间隔单位长度
        float tempDis2 = drawH * 1f / numbersY.length;
        canvas.drawLine(disLineW, drawH, w - disLineW, drawH, paint);
        for (int i = 0; i < numbersY.length; i++) {
            // 横线背景
            paint.setColor(lineColor);
            paint.setStrokeWidth(1);
            paint.setTextSize(lineSize);
            // 纵向刻度值
            paint.setTextSize(textSize);
            paint.getTextBounds(numbersY[i], 0, numbersY[i].length(), textRect);
            if (i > 0) {
                canvas.drawText(numbersY[i], disLineW - textRect.width() - 20,
                        drawH - tempDis2 * i + textRect.height() / 2, paint);
            } else {
                canvas.drawText(numbersY[i], disLineW - textRect.width() - 20,
                        drawH - tempDis2 * i, paint);
            }
        }
        paint.setStrokeWidth(1);
        paint.setColor(lineColor);
        paint.setTextSize(textSize);
        paint.getTextBounds("人数", 0, "人数".length(), textRect);
        canvas.drawText("人数", disLineW - textRect.width() - 10,
                textRect.height(), paint);

        // 数据量级图线
        for (int i = 1; maxNumber >= 0 && i < numbers.length; i++) {
            paint.setStrokeWidth(1);
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            float x1 = disLineW + tempDis1 * (i - 1);
            float y1 = (drawH - tempDis2) * numbers[i - 1]
                    / Integer.parseInt(numbersY[numbersY.length - 1]);
            float x2 = x1 + tempDis1;
            float y2 = (drawH - tempDis2) * numbers[i]
                    / Integer.parseInt(numbersY[numbersY.length - 1]);
            Path path = new Path();
            if (isCurveLine) {
                float wt = (x1 + x2) / 2;
                path.moveTo(x1, drawH);
                path.lineTo(x1, drawH - y1);
                path.cubicTo(wt, drawH - y1, wt, drawH - y2, x2, drawH - y2);
                path.lineTo(x2, drawH);
                path.close();
                // 画阴影部分
                canvas.drawPath(path, paint);
                // 阴影部分的竖线画不到。重新画
                canvas.drawLine(disLineW + tempDis1 * i, drawH - y2, disLineW
                        + tempDis1 * i, drawH, paint);

                paint.setColor(res.getColor(R.color.red));
                paint.setStyle(Paint.Style.STROKE);

                Path path2 = new Path();

                path2.moveTo(x1, drawH - y1);
                path2.cubicTo(wt, drawH - y1, wt, drawH - y2, x2, drawH - y2);
                canvas.drawPath(path2, paint);
            } else {
                canvas.drawLine(x1, drawH - y1, x2, drawH - y2, paint);
            }
        }

    }
}
