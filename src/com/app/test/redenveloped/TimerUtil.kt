package com.app.test.redenveloped

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
class TimerUtil {
    interface TimerCallback {
        fun onCheck(checkCount: Int): Boolean
        fun onEnd()
    }

    private var handler: Handler?
    private var intervalTime: Long = 1000
    private var timerCallback: TimerCallback? = null
    private var atomicInteger: AtomicInteger? = AtomicInteger()
    fun start(timerCallback: TimerCallback) {
        startPolling(0, 500, timerCallback)
    }

    fun startTime(intervalTimeMillis: Long, timerCallback: TimerCallback) {
        startPolling(0, intervalTimeMillis, timerCallback)
    }

    fun startDelayTime(delayTimeMillis: Long, intervalTimeMillis: Long, timerCallback: TimerCallback) {
        startPolling(delayTimeMillis, intervalTimeMillis, timerCallback)
    }

    private fun startPolling(delayTimeMillis: Long, intervalTimeMillis: Long, timerCallback: TimerCallback) {
        reset()
        intervalTime = intervalTimeMillis
        this.timerCallback = timerCallback
        handler?.sendMessageDelayed(message, delayTimeMillis)
    }

    private val message: Message
        get() {
            val obtain = Message.obtain()
            obtain.what = MSG_WHAT
            return obtain
        }

    fun reset() {
        if (atomicInteger == null) {
            atomicInteger = AtomicInteger()
        } else {
            atomicInteger?.set(0)
        }
        handler?.removeCallbacksAndMessages(null)
    }

    fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val MSG_WHAT = 20200420
        fun get(): TimerUtil {
            return TimerUtil()
        }

        fun onDestroy(timerUtil: TimerUtil?) {
            timerUtil?.onDestroy()
        }
    }

    init {
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (MSG_WHAT != msg.what || timerCallback == null) {
                    return
                }
                val isSuccess = atomicInteger?.incrementAndGet()?.let { timerCallback?.onCheck(it) }
                if (isSuccess == true) {
                    timerCallback?.onEnd()
                } else {
                    sendMessageDelayed(message, intervalTime)
                }
            }
        }
    }
}