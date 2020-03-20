//package com.app.test.viewpager;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.DashPathEffect;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.RectF;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.app.test.R;
//import com.app.test.utils.CollectionsUtil;
//import com.app.test.utils.DisplayUtil;
//
//import java.util.ArrayList;
//
///**
// * Created by dell on 2017/10/12.
// * 圆圈进度条
// */
//
//public class BrushVideoSeekCircle extends View {
//    private Paint mPaint;
//    private int mKeduLength;
//    private int mProgressRingThick;
//    private Bitmap mIndicatorBmp;
//    private int mKedu2ProgressRingDistance;
//    private int mEdge2KeduDistance;
//    private int mPointDiscreteDistance;
//    private int mWidth;
//    private int mHeight;
//    private float rate;
//    private int KeduCount;
//    private int mPointRadio;
//    private float mMaxRadious;
//    private OnSeekChangeListener mOnSeekArcChangeListener;
//    private float thumbX;
//    private float thumbY;
//    private float mTouchIgnoreRadius;
//    private boolean ignoreContinueMoveTouch;
//    private float currentAngle = -1;
//    private Matrix mThumbnailMatrix;
//    private ArrayList<SeekCircleProgressBean> mList;
//    private RectF mProgressRectF;
//    private boolean isTracking; //用户是否在拖动
//    private Path mTrackingPath;
//    private Paint mTrackingPaint;
//    public float thumbnailProgressFinishAngle;
//    public float thumbnailProgressStartAngle;
//    private float beginTrackingAngle;
//    private int mMirrorRadious;
//    private float mMirrorRate;
//    private Path mMirrorPath;
//    private ArrayList<SeekCircleWorkBean> mWorkPointList;
//    private Bitmap mUnFinishWorkBmp;
//    private Bitmap mFinishWorkBmp;
//    private Matrix mWorkMatrix;
//    private float mWorkScleRate;
//    private int mMPoint2EdgeDistance;
//
//    public BrushVideoSeekCircle(Context context) {
//        super(context);
//        init();
//    }
//
//    public BrushVideoSeekCircle(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public BrushVideoSeekCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setDither(true);
//
//        //虚线
//        mTrackingPaint = new Paint(mPaint);
//        mTrackingPaint.setStyle(Paint.Style.STROKE);
//        mTrackingPaint.setColor(Color.YELLOW);
//        mTrackingPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(),1));
//        mTrackingPaint.setPathEffect(new DashPathEffect(new float[] {DisplayUtil.dip2px(getContext(),2), DisplayUtil.dip2px(getContext(),2)}, 0));
//        mTrackingPath = new Path();
//
//        //放大镜
//        mMirrorRadious = DisplayUtil.dip2px(getContext(), 20);        //放大镜半斤
//        mMirrorRate = 1.5f;        //放大镜放大的倍速
//        mMirrorPath = new Path();
//
//        //作业
//        mUnFinishWorkBmp = BitmapFactory.decodeResource(getResources(), R.drawable.zuoye_unfinish);
//        mFinishWorkBmp = BitmapFactory.decodeResource(getResources(), R.drawable.zuoye_finish);
//        mWorkScleRate = (float) DisplayUtil.dip2px(getContext(), 14) / (float) mUnFinishWorkBmp.getWidth();
//        mWorkMatrix = new Matrix();
//        mWorkMatrix.setScale(mWorkScleRate, mWorkScleRate);
//
//        //整体缩放比率
//        rate = 1;
//        //指示器触摸偏差
//        mTouchIgnoreRadius = DisplayUtil.dip2px(getContext(), 20);
//        //刻度数
//        KeduCount = 36;
//        //刻度线长度
//        mKeduLength = DisplayUtil.dip2px(getContext(),5);
//        //进度环厚度
//        mProgressRingThick = DisplayUtil.dip2px(getContext(),15);
//
//        //视频点的半径
//        mPointRadio = DisplayUtil.dip2px(getContext(), 5);
//        //视频点扩散的距离
////        mPointDiscreteDistance = DisplayUtil.dip2px(getContext(),10);
//        mPointDiscreteDistance = 0;
//        //视频点圆形 到 最外灰圈的距离
//        mMPoint2EdgeDistance = DisplayUtil.dip2px(getContext(), 7);
//        //最外圈灰线到刻度内圆边距的距离
//        mEdge2KeduDistance = DisplayUtil.dip2px(getContext(), 5) + mKeduLength;
//        //刻度线内圆边距到进度环外圆边距的距离
//        mKedu2ProgressRingDistance = mProgressRingThick / 2 + DisplayUtil.dip2px(getContext(), 3);
//        //进度指示器
//        mIndicatorBmp = BitmapFactory.decodeResource(getResources(), R.drawable.cg_indicator);
//        mThumbnailMatrix = new Matrix();
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        mWidth = getMeasuredWidth();
//        mHeight = getMeasuredHeight();
//        mMaxRadious = Math.min(mWidth, mHeight) / 2 * rate - DisplayUtil.dip2px(getContext(),20);//最大半径 (20:指示器超出的长度 )
//
//        canvas.translate(mWidth / 2, mHeight / 2);//平移圆心
//        drawBg(canvas);
//        drawKeDu(canvas);
//        drawPoint(canvas);
//        drawWorkPoint(canvas);
//        drawProgress(canvas);
//
//        //放大镜
//        if (isTracking) {
//            canvas.save();
//            //放大镜圆形离大圆盘圆心的距离
//            float mirrorDiscreteRadio = mMaxRadious - 2.0f * (float) mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance -(float) mProgressRingThick / 2.0f ;
//            mMirrorPath.reset();
//            mMirrorPath.addCircle(getRealCosX(currentAngle,mirrorDiscreteRadio), getRealSinY(currentAngle,mirrorDiscreteRadio), mMirrorRadious, Path.Direction.CW);
//            canvas.clipPath(mMirrorPath);
//
//            drawBg(canvas);//画没放大的背景
//            drawKeDu(canvas);//画没放大的刻度
//            //画放大的进度
//            float scaleOneRadious = mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - (float) mProgressRingThick / 2.0f;
//            float X = getRealCosX(currentAngle,scaleOneRadious * (mMirrorRate - 1.0f));
//            float Y = getRealSinY(currentAngle, scaleOneRadious * (mMirrorRate - 1.0f));
//            canvas.translate(-X,-Y);
//            canvas.scale(mMirrorRate,mMirrorRate);
//            drawProgress(canvas);
//
//            canvas.restore();
//        }
//
//        drawThumbnail(canvas);
//
//    }
//
//
//    /*画指示器*/
//    private void drawThumbnail(Canvas canvas) {
//        if (currentAngle >= 0) {
//            //画瞄准的虚线
//            if (isTracking) {
//                canvas.save();
//                canvas.rotate(currentAngle);
//                mTrackingPath.reset();
//                mTrackingPath.moveTo(0, -mMaxRadious);
//                mTrackingPath.lineTo(0, -mMaxRadious + (mPointDiscreteDistance + mEdge2KeduDistance + mEdge2KeduDistance + mKedu2ProgressRingDistance + mProgressRingThick));
//                canvas.drawPath(mTrackingPath,mTrackingPaint);
////                canvas.drawLine(0, -mMaxRadious, 0, -mMaxRadious + (mPointDiscreteDistance + mEdge2KeduDistance + mEdge2KeduDistance), mTrackingPaint);
//                canvas.restore();
//            }
//
//            thumbX = getRealCosX(currentAngle,mMaxRadious);
//            thumbY = getRealSinY(currentAngle, mMaxRadious);
//
//            if (mIndicatorBmp != null) {
//                int bmpWidth = mIndicatorBmp.getWidth();
//                int bmpHeight = mIndicatorBmp.getHeight();
//
//                canvas.save();
//                canvas.translate(thumbX - (float)bmpWidth / 2.0f, thumbY - (float)bmpHeight / 2.0f);
//                mThumbnailMatrix.setRotate(currentAngle , (float)bmpWidth / 2.0f , (float)bmpHeight / 2.0f);
//                canvas.drawBitmap(mIndicatorBmp , mThumbnailMatrix, null);
//                canvas.restore();
//            }
//        }
//    }
//
//    private void drawKeDu(Canvas canvas) {
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(),1));
//
//        canvas.save();
//        float startY = -(mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance);
//        float stopY = startY - mKeduLength;
//        float angle = (float) 360 / KeduCount;
//        float accuAngle = 0;
//        for (int i = 0; i <= KeduCount; i++) {
//            if (accuAngle <= currentAngle) {
//                mPaint.setColor(Color.parseColor("#ffffff"));
//            } else {
//                mPaint.setColor(Color.parseColor("#1affffff"));
//            }
//            canvas.drawLine(0, startY, 0, stopY , mPaint);
//            canvas.rotate(angle);
//            accuAngle += angle;
//        }
//        canvas.restore();
//    }
//
//    private void drawPoint(Canvas canvas) {
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.parseColor("#22CA92"));
//        if (!CollectionsUtil.isListEmpty(mList)) {
//            for (SeekCircleProgressBean bean : mList) {
//                float angle = bean.endAngle;
//                if (angle >= 0 && angle <= 360) {
//                    canvas.save();
//                    canvas.rotate(angle);
//                    canvas.drawCircle(0, -(mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mMPoint2EdgeDistance - mPointRadio), mPointRadio, mPaint);
//                    canvas.restore();
//                }
//            }
//        }
//    }
//
//    private void drawWorkPoint(Canvas canvas) {
//        if (!CollectionsUtil.isListEmpty(mWorkPointList)) {
//            for (SeekCircleWorkBean bean : mWorkPointList) {
//                float angle = bean.angle;
//                if (angle < 0 )
//                    angle = 0;
//                if(angle > 360)
//                    angle = 360;
//                float radious = mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance;
//                float x = getRealCosX(angle, radious) - mUnFinishWorkBmp.getWidth() / 2.0f * mWorkScleRate;
//                float y = getRealSinY(angle, radious) - mUnFinishWorkBmp.getHeight() / 2.0f * mWorkScleRate;
//                canvas.save();
//                canvas.translate(x,y);
//                if (bean.isComplete) {
//                    canvas.drawBitmap(mFinishWorkBmp, mWorkMatrix, null);
//                } else {
//                    canvas.drawBitmap(mUnFinishWorkBmp, mWorkMatrix, null);
//                }
//                canvas.restore();
//            }
//        }
//    }
//
//    /*画一段一段的进度*/
//    private void drawProgress(Canvas canvas) {
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.parseColor("#22CA92"));
//        mPaint.setStrokeWidth(mProgressRingThick);
//        if (!CollectionsUtil.isListEmpty(mList)) {
//            for (SeekCircleProgressBean bean : mList) {
//                if (!CollectionsUtil.isSetEmpty(bean.progressSet)) {
//                    float preStart =0;
//                    float preEnd = 0;
//                    int preProgress = 0;
//                    int index = 0;
//                    for (int progress :  bean.progressSet) {
////                        if (progress <= 0) {
////                            index ++;
////                            continue;
////                        }
//                        float arcStart = bean.startAngle + progress * bean.unit - bean.unit - 90;
//
//                        float radious = mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - (float) mProgressRingThick / 2.0f;
//                        if (mProgressRectF == null) {
//                            mProgressRectF = new RectF(-radious, -radious, radious, radious);
//                        } else {
//                            mProgressRectF.left = -radious;
//                            mProgressRectF.top = -radious;
//                            mProgressRectF.right = radious;
//                            mProgressRectF.bottom = radious;
//                        }
//
//                        if (index == 0) {
//                            preStart = arcStart;
//                            preEnd = arcStart + bean.unit;
//
//                        }else  {
//                            if (preProgress + 1 != progress) {
//                                //画前面的弧 pre清零
//                                canvas.drawArc(mProgressRectF, preStart, (preEnd > (bean.endAngle - 90 ) ? (bean.endAngle - 90 ) :preEnd ) - preStart, false, mPaint);
//                                preStart = arcStart;
//                                preEnd = arcStart + bean.unit;
//
//                            } else {
//                                preEnd += bean.unit;
//
//                            }
//                        }
//                        if (index == bean.progressSet.size() - 1) {
//                            canvas.drawArc(mProgressRectF, preStart, (preEnd > (bean.endAngle - 90 ) ? (bean.endAngle - 90 ) :preEnd ) - preStart, false, mPaint);
//                        }
//                        preProgress = progress;
//                        index++;
//
////                        int progress = bean.progressList.get(i);
////                        float arcStart = bean.startAngle + progress * bean.unit - bean.unit - 90;
////                        float arcEnd = arcStart + bean.unit;
////                        System.out.println("arcStart===" + arcStart + "........arcEnd===" + arcEnd);
////
////                        if (mProgressRectF == null) {
////                            float radious = mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - (float) mProgressRingThick / 2.0f;
////                            mProgressRectF = new RectF(-radious, -radious, radious, radious);
////                        }
////                        canvas.drawArc(mProgressRectF, arcStart, bean.unit, false, mPaint);
//                    }
//                }
//            }
//        }
//        float rectFRadious = mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - (float) mProgressRingThick / 2.0f;
//        if (mProgressRectF == null) {
//            mProgressRectF = new RectF(-rectFRadious, -rectFRadious, rectFRadious, rectFRadious);
//        } else {
//            mProgressRectF.left = -rectFRadious;
//            mProgressRectF.top = -rectFRadious;
//            mProgressRectF.right = rectFRadious;
//            mProgressRectF.bottom = rectFRadious;
//        }
//        if (thumbnailProgressFinishAngle > thumbnailProgressStartAngle) {
//            canvas.drawArc(mProgressRectF, thumbnailProgressStartAngle - 90, thumbnailProgressFinishAngle - thumbnailProgressStartAngle , false, mPaint);
//        }
//    }
//
//    /*画静态背景*/
//    private void drawBg(Canvas canvas) {
//        //画背景
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.parseColor("#21ffffff"));
//        canvas.drawCircle(0, 0, mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance, mPaint);
//
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.parseColor("#1affffff"));
//        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(),1));
//
//        //话最外面的灰圈
//        canvas.drawCircle(0,0,mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance,mPaint);
//        //画最里边的圈
//        canvas.drawCircle(0,0,mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - mProgressRingThick,mPaint);
//
//        //画进度环
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.parseColor("#333333"));
//        mPaint.setStrokeWidth(mProgressRingThick);
//        canvas.drawCircle(0,0,mMaxRadious - 2 * mPointRadio - mPointDiscreteDistance - mEdge2KeduDistance - mKedu2ProgressRingDistance - (float) mProgressRingThick / 2.0f,mPaint);
//
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (isEnabled()) {
//            this.getParent().requestDisallowInterceptTouchEvent(true);
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    updateOnTouch(event);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    updateOnTouch(event);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if (!ignoreContinueMoveTouch) {
//                        onStopTrackingTouch();
//                    }
//                    ignoreContinueMoveTouch = true;
//                    setPressed(false);
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    if (!ignoreContinueMoveTouch) {
//                        onStopTrackingTouch();
//                    }
//                    ignoreContinueMoveTouch = true;
//                    setPressed(false);
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                    break;
//            }
//            return true;
//        }
//        return false;
//    }
//
//    private void updateOnTouch(MotionEvent event) {
//        boolean ignoreTouch = ignoreTouch(event.getX(), event.getY());
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            ignoreContinueMoveTouch = ignoreTouch;
//            if (ignoreTouch) {
//                return;
//            }
//            onStartTrackingTouch();
//        }
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            //down事件忽略,后面的move事件也忽略
//            if (ignoreContinueMoveTouch) {
//                return;
//            }
//        }
//        setPressed(true);
//        float touchAngle = getTouchDegrees(event.getX(), event.getY());
//        onProgressRefresh(touchAngle, true);
//    }
//
//    public void setCurrentAngle(float angle) {
//        if (isTracking) {
//            return;
//        }
//        this.currentAngle = angle;
//    }
//
//    public float getCurrentAngle() {
//        return currentAngle;
//    }
//
//    /*还原到上一次track的位置*/
//    public void revertTrack() {
//        setCurrentAngle(beginTrackingAngle);
//        invalidate();
//    }
//
//    private void onProgressRefresh(float angle, boolean isFromUser) {
//        if (angle < 0 || angle > 360) {
//            return;
//        }
//        this.currentAngle = angle;
//        if (mOnSeekArcChangeListener != null) {
//            mOnSeekArcChangeListener.onProgressChanged(this, angle, isFromUser);
//        }
//        invalidate();
//    }
//
//
//    private float getTouchDegrees(float xPos, float yPos) {
//        float x = xPos - mWidth / 2;
//        float y = yPos - mHeight / 2;
//        float angle = (float) Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2));
//        if (angle < 0) {
//            angle = 360 + angle;
//        }
//        return angle;
//    }
//
//    private boolean ignoreTouch(float xPos, float yPos) {
//        boolean ignore = false;
//        float diffX = xPos - mWidth / 2 - thumbX;
//        float diffY = yPos - mHeight / 2 -thumbY;
//        float touchRadius = (float) Math.sqrt(((diffY * diffY) + (diffX * diffX)));
////        System.out.println("触摸距离========" + DisplayUtil.px2dip(getContext(),touchRadius));
//        if (touchRadius > mTouchIgnoreRadius) {
//            ignore = true;
//        }
//        return ignore;
//    }
//
//    private void onStopTrackingTouch() {
//        this.isTracking = false;
//        if (mOnSeekArcChangeListener != null) {
//            mOnSeekArcChangeListener.onStopTrackingTouch(this);
//        }
//        invalidate();
//    }
//
//    private void onStartTrackingTouch() {
//        this.isTracking = true;
//        this.beginTrackingAngle = currentAngle;
//        if (mOnSeekArcChangeListener != null) {
//            mOnSeekArcChangeListener.onStartTrackingTouch(this);
//        }
//    }
//
//    private float getRealCosX(float angle, float radius ) {
//        if (angle < 0 || angle > 360) {
//            return 0;
//        }
//        if (angle < 90) { // 一
//            return getCosX(90 - angle, radius);
//        } else if (angle < 180) { //四
//            return getCosX(angle - 90, radius);
//        } else if (angle < 270) { //三
//            return -getCosX(270 - angle, radius);
//        } else { //二
//            return -getCosX(angle - 270,radius);
//        }
//    }
//
//    private float getRealSinY(float angle, float radius) {
//        if (angle < 0 || angle > 360) {
//            return 0;
//        }
//        if (angle < 90) { // 一
//            return -getSinY(90 - angle, radius);
//        } else if (angle < 180) { //四
//            return getSinY(angle - 90, radius);
//        } else if (angle < 270) { //三
//            return getSinY(270 - angle, radius);
//        } else { //二
//            return -getSinY(angle - 270, radius);
//        }
//    }
//
//
//    private float getCosX(float diggre, float radius ) { //diggre  0-90 第一象限角度计算
//        return (float) (radius * Math.cos(Math.toRadians(diggre)));
//    }
//
//    private float getSinY(float diggre, float radius ) { //diggre  0-90
//        return (float) (radius * Math.sin(Math.toRadians(diggre)));
//    }
//
//
//
//    public void setOnSeekArcChangeListener(OnSeekChangeListener l) {
//        mOnSeekArcChangeListener = l;
//    }
//
//    public void setPointList(ArrayList<SeekCircleProgressBean> pointList) {
//        mList = pointList;
//    }
//
//    /*设置作业点的位置*/
//    public void setWorkPointList(ArrayList<SeekCircleWorkBean> workPointList) {
//        this.mWorkPointList = workPointList;
//    }
//
//    public void updateView() {
//        invalidate();
//    }
//
//    public interface OnSeekChangeListener {
//
//        void onProgressChanged(BrushVideoSeekCircle seekCircle, float angle, boolean fromUser);
//
//
//        void onStartTrackingTouch(BrushVideoSeekCircle seekCircle);
//
//
//        void onStopTrackingTouch(BrushVideoSeekCircle seekCircle);
//    }
//
//}
