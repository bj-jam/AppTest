package com.app.test.circle;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.app.test.R;
import com.app.test.utils.DisplayUtil;


/**
 * Created by jam on 2019/9/06.
 */
public class SeekBarView extends View {
    private int width;
    private int height;
    private float perWidth = 0;
    private Paint mPaint;
    private Paint mColorPaint;
    private Paint buttonPaint;
    private Bitmap thumb;
    private Bitmap thumb1;
    private float curValue = 2;
    private int maxValue = 5;
    private ResponseOnTouch responseOnTouch;

    private int currentPosition = 5;

    private int intervalDistance = 100 / (maxValue - 1);

    Rect mTextRect;

    private LinearGradient linearGradient;

    public SeekBarView(Context context) {
        super(context);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        curValue = 0;
        thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_badge_current_spot);
        thumb1 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_badge_spot);

        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);//锯齿不显示
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 7));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xffdddddd);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mColorPaint = new Paint(Paint.DITHER_FLAG);
        mColorPaint.setAntiAlias(true);//锯齿不显示
        mColorPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 7));
        mColorPaint.setStyle(Paint.Style.FILL);
        mColorPaint.setStrokeCap(Paint.Cap.ROUND);


        buttonPaint = new Paint(Paint.DITHER_FLAG);
        buttonPaint.setAntiAlias(true);


        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(DisplayUtil.dip2px(getContext(), 10));

        mTextRect = new Rect();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        //控件的高度
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        perWidth = (width - thumb.getWidth()) / (maxValue - 1f);
        linearGradient = new LinearGradient(0, 0, width, 0,
                Color.parseColor("#ff2BE3D7"),
                Color.parseColor("#ff07A3F0"),
                Shader.TileMode.REPEAT);
    }


    /**
     * 绘制类型字体的画笔
     */
    private TextPaint mTextPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0x33FFFFFF);
        canvas.drawLine(thumb.getWidth() / 2f, thumb.getHeight() / 2f,
                width - thumb.getWidth() / 2f, thumb.getHeight() / 2f, mPaint);
        mColorPaint.setShader(linearGradient);
        //当前进度小于100
        float lineX = 0;
        if (currentPosition > 0 && currentPosition < 100)
            lineX = thumb.getWidth() / 2f
                    + (currentPosition / intervalDistance) * perWidth
                    + currentPosition % intervalDistance * (perWidth / intervalDistance);
        else if (currentPosition >= 100)
            lineX = width - thumb.getWidth() / 2f;
        else
            lineX = thumb.getWidth() / 2f;
        canvas.drawLine(thumb.getWidth() / 2f, thumb.getHeight() / 2f, lineX
                , thumb.getHeight() / 2f, mColorPaint);
        for (int section = 0; section < maxValue; section++) {
            //画等级圆点
            if (section == 0)
                canvas.drawBitmap(thumb1, perWidth * section + thumb1.getWidth() / 2,
                        thumb.getHeight() / 2 - thumb1.getHeight() / 2, buttonPaint);
            else if (section == 4)
                canvas.drawBitmap(thumb1, perWidth * section + thumb1.getWidth() / 2,
                        thumb.getHeight() / 2 - thumb1.getHeight() / 2, buttonPaint);
            else
                canvas.drawBitmap(thumb1, perWidth * section,
                        thumb.getHeight() / 2 - thumb1.getHeight() / 2, buttonPaint);
            //画文字
            String textMarker = "Lv" + (section + 1);
            float textWidth = mTextPaint.measureText(textMarker);
            mTextPaint.getTextBounds(textMarker, 0, textMarker.length(), mTextRect);
            //计算文字位置
            float textX = section * perWidth + thumb.getWidth() / 2f - textWidth / 2;
            float textY = DisplayUtil.dip2px(getContext(), 8) + thumb.getHeight();

            if (section == 4)
                canvas.drawText(textMarker, textX - DisplayUtil.dip2px(getContext(), 2), textY, mTextPaint);
            else
                canvas.drawText(textMarker, textX, textY, mTextPaint);
        }
        float startX = 0;
        if (currentPosition > 0 && currentPosition < 100) {
            if (currentPosition % intervalDistance == 0) {
                startX = perWidth * (currentPosition / intervalDistance);
            } else {
                startX = (currentPosition / intervalDistance) * perWidth
                        + currentPosition % intervalDistance * (perWidth / intervalDistance);
            }
        } else if (currentPosition >= 100) {
            startX = perWidth * (maxValue - 1);
        } else {
            startX = 0;
        }
        //如果超过view的位置
        if (startX >= (thumb.getWidth() / 2 + perWidth * (maxValue - 1)))
            startX = thumb.getWidth() / 2 + perWidth * (maxValue - 1);
        canvas.drawBitmap(thumb, startX, 0, buttonPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                responseTouch((int) event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                responseTouch((int) event.getX());
                break;
            case MotionEvent.ACTION_UP:
//                responseTouch((int) event.getX());
                if (responseOnTouch != null)
                    responseOnTouch.onTouchResponse(curValue);
                break;
        }
        return true;
    }

    /**
     * 触摸之后更新视图
     *
     * @param x
     */
    private void responseTouch(int x) {
        if (x <= width - thumb.getWidth() / 2) {
            curValue = (x + perWidth / 3) / (int) perWidth;
        } else {
            curValue = maxValue - 1;
        }
        invalidate();
    }

    //设置监听
    public void setResponseOnTouch(ResponseOnTouch response) {
        responseOnTouch = response;
    }

    //设置当前进度
    public void setProgress(float progress) {
        curValue = progress;
        invalidate();
    }


    public interface ResponseOnTouch {
        public void onTouchResponse(float volume);
    }
}
