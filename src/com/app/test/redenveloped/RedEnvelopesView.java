package com.app.test.redenveloped;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedEnvelopesView extends View {
    private Map<Integer, RedEnvelopesHelper> map = new ConcurrentHashMap<>();

    private int totalHeight;
    private boolean fingerIsUp;

    public RedEnvelopesView(Context context) {
        super(context);
    }

    public RedEnvelopesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RedEnvelopesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RedEnvelopesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(int totalHeight) {
        this.totalHeight = totalHeight;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setTotalHeight(h);
    }

    public void addRedPacketData(int index, RedEnvelopesHelper redPacket) {
        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        map.put(index, redPacket);
    }

    public void startDraw() {
        invalidate();
    }

    public boolean isEnd() {
        return map == null || map.size() == 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isEnd()) {
            return;
        }
        Iterator<Map.Entry<Integer, RedEnvelopesHelper>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            RedEnvelopesHelper redPacket = iterator.next().getValue();
            if (redPacket.getY() > getTotalHeight() || redPacket.isEnd()) {
                clearRedPacketFromMap(map, redPacket);
                continue;
            }
            if (redPacket.getBitmap() == null) {
                continue;
            }
            switch (redPacket.getRedPacketStatus()) {
                case RedEnvelopesHelper.status_3:
                    if (!redPacket.getBitmap().isRecycled()) {
                        canvas.drawBitmap(redPacket.getBitmap(), redPacket.getX(), redPacket.getY(), null);
                    }
                    break;
                case RedEnvelopesHelper.status_2:
                    if (!redPacket.getBitmap().isRecycled()) {
                        canvas.drawBitmap(redPacket.getClickBitmap(), redPacket.getMatrix(), null);
                    }
                    break;
                case RedEnvelopesHelper.status_1:
                    if (!redPacket.getBitmap().isRecycled()) {
                        canvas.drawBitmap(redPacket.getGoldBitmap(), redPacket.getMatrix(), null);
                    }
                    break;
                case RedEnvelopesHelper.status_0:
                    clearRedPacketFromMap(map, redPacket);
                    break;
            }

        }

        invalidate();
    }

    private void clearRedPacketFromMap(Map<Integer, RedEnvelopesHelper> map, RedEnvelopesHelper redPacket) {
        if (map == null || redPacket == null) {
            return;
        }
        RedEnvelopesHelper removeRedPacket = map.remove(redPacket.getIndex());
        removeRedPacket = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX;
        int eventY;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                fingerIsUp = false;
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                clickRedPacket(eventX, eventY);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                fingerIsUp = false;
                int actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                eventX = (int) event.getX(event.findPointerIndex(pointerId));
                eventY = (int) event.getY(event.findPointerIndex(pointerId));
                clickRedPacket(eventX, eventY);

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                fingerIsUp = true;
                break;
        }
        return true;
    }

    public void clickRedPacket(int x, int y) {
        if (map == null) {
            return;
        }
        for (Map.Entry<Integer, RedEnvelopesHelper> next : map.entrySet()) {
            if (fingerIsUp) {
                return;
            }
            final RedEnvelopesHelper redPacket = next.getValue();
            redPacket.saveCurrentXY();
            if (redPacket.isCanClick() && redPacket.constant(x, y)) {
                redPacket.clickRedPacket();
            }
        }
    }
}
