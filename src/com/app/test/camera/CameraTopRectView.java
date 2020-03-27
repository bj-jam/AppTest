package com.app.test.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.app.test.util.DensityUtil;

public class CameraTopRectView extends View {

    private int mWidth;//view的宽度
    private int mHeight;//view的高度


    private int rectWidth;//选择框的宽度
    private int rectHeight;//选择框的高度

    private int topX;//选择框左上起始点的X的位置
    private int topY;//选择框左上起始点的Y的位置

    private static final int LINE_WIDTH = 2;//选择框边框线条的宽度
    private int leftMargin = 60;//选择框距离左边的位置


    private Paint linePaint;//选择框边框线条的画笔
    private Paint paint;//选择框以外的区域的画笔

    public CameraTopRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub  

        rectWidth = DensityUtil.dp2px(380);
        rectHeight = DensityUtil.dp2px(235);
        leftMargin = DensityUtil.dp2px(50);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(0xFF00c896);
        linePaint.setStyle(Style.STROKE);
        linePaint.setStrokeWidth(LINE_WIDTH);// 设置线宽  
        linePaint.setAlpha(255);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xb0000000);
        paint.setStyle(Style.FILL);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub  
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        // 选择框的左上点位置
        topX = leftMargin;
        topY = (mHeight - rectHeight) / 2;

        //上区域
        canvas.drawRect(0, 0, mWidth, topY, paint);
        //下区域
        canvas.drawRect(0, topY + rectHeight, mWidth, mHeight, paint);
        //左区域
        canvas.drawRect(0, topY, leftMargin, topY + rectHeight, paint);
        //右区域
        canvas.drawRect(topX + rectWidth, topY, mWidth, topY + rectHeight, paint);


        //上线
        canvas.drawLine(topX, topY, topX + rectWidth, topY,
                linePaint);
        //左线
        canvas.drawLine(topX, topY, topX, topY + rectHeight,
                linePaint);
        //右线
        canvas.drawLine(topX + rectWidth, topY, topX + rectWidth, topY + rectHeight,
                linePaint);
        //下线
        canvas.drawLine(topX, topY + rectHeight, topX + rectWidth, topY + rectHeight,
                linePaint);

    }

    public int getTopY() {
        return topY;
    }

    public int getTopX() {
        return topX;
    }


    public int getViewWidth() {
        return rectWidth;
    }

    public int getViewHeight() {
        return rectHeight;
    }

}  