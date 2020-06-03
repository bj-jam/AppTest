package com.app.test.redenveloped

import android.os.Handler
import android.os.Looper
import android.os.Message

/***
 * created by android on 2019/5/6
 */
class TimeCountDown {
    interface TimeCallback {
        fun onNext(timeSecond: Int)
        fun onComplete()
    }

    private val msgWhat = 6000
    private var handler: Handler?
    private var timeCallback: TimeCallback? = null
    fun start(timeSecond: Int, timeCallback: TimeCallback?) {
        handler?.removeCallbacksAndMessages(null)
        this.timeCallback = timeCallback
        sendTime(timeSecond, timeCallback)
        val message = getMessage(timeSecond)
        handler?.sendMessageDelayed(message, 1000)
    }

    private fun getMessage(time: Int): Message {
        val obtain = Message.obtain()
        obtain.what = msgWhat
        obtain.arg1 = time
        return obtain
    }

    private fun sendTime(timeSecond: Int, timeCallback: TimeCallback?) {
        timeCallback?.onNext(timeSecond)
    }

    fun onDestroy() {
        timeCallback = null
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    fun clear() {
        handler?.removeCallbacksAndMessages(null)
    }

    init {
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msgWhat != msg.what || timeCallback == null) {
                    return
                }
                val time = msg.arg1 - 1
                sendTime(time, timeCallback)
                if (time == 0) {
                    timeCallback?.onComplete()
                    return
                }
                val message = getMessage(time)
                sendMessageDelayed(message, 1000)
            }
        }
    }
}