package com.app.test.redenveloped

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentHashMap

class RedEnvelopesView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var map: MutableMap<Int, RedEnvelopesHelper> = ConcurrentHashMap()
    var totalHeight = 0
    private var fingerIsUp = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        totalHeight = h
    }

    fun addRedPacketData(index: Int, redPacket: RedEnvelopesHelper) {
        map[index] = redPacket
    }

    fun startDraw() {
        invalidate()
    }

    val isEnd: Boolean
        get() = map.isEmpty()

    override fun onDraw(canvas: Canvas) {
        if (isEnd) {
            return
        }
        val iterator: Iterator<Map.Entry<Int, RedEnvelopesHelper>> = map.entries.iterator()
        while (iterator.hasNext()) {
            val redPacket = iterator.next().value
            if (redPacket.moveY > totalHeight || redPacket.isEnd) {
                clearRedPacketFromMap(map, redPacket)
                continue
            }
            if (redPacket.bitmap == null) {
                continue
            }
            when (redPacket.redPacketStatus) {
                RedEnvelopesHelper.status_3 -> if (redPacket.bitmap?.isRecycled != true) {
                    redPacket.bitmap?.also {
                        canvas.drawBitmap(it, redPacket.moveX.toFloat(), redPacket.moveY.toFloat(), null)
                    }
                }
                RedEnvelopesHelper.status_2 -> if (redPacket.clickBitmap?.isRecycled != true) {
                    redPacket.clickBitmap?.also {
                        canvas.drawBitmap(it, redPacket.getMatrix(), null)
                    }

                }
                RedEnvelopesHelper.status_1 -> if (redPacket.goldBitmap?.isRecycled != true) {
                    redPacket.goldBitmap?.also {
                        canvas.drawBitmap(it, redPacket.getMatrix(), null)
                    }
                }
                RedEnvelopesHelper.status_0 -> clearRedPacketFromMap(map, redPacket)
            }
        }
        invalidate()
    }

    private fun clearRedPacketFromMap(map: MutableMap<Int, RedEnvelopesHelper>?, redPacket: RedEnvelopesHelper?) {
        if (map == null || redPacket == null) {
            return
        }
        var removeRedPacket = map.remove(redPacket.index)
        removeRedPacket = null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX: Int
        val eventY: Int
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                fingerIsUp = false
                eventX = event.x.toInt()
                eventY = event.y.toInt()
                clickRedPacket(eventX, eventY)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                fingerIsUp = false
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                eventX = event.getX(event.findPointerIndex(pointerId)).toInt()
                eventY = event.getY(event.findPointerIndex(pointerId)).toInt()
                clickRedPacket(eventX, eventY)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> fingerIsUp = true
        }
        return true
    }

    private fun clickRedPacket(x: Int, y: Int) {
        for ((_, redPacket) in map!!) {
            if (fingerIsUp) {
                return
            }
            redPacket.saveCurrentXY()
            if (redPacket.isCanClick && redPacket.constant(x, y)) {
                redPacket.clickRedPacket()
            }
        }
    }
}