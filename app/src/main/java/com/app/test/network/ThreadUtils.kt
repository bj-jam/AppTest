package com.app.test.network

import android.os.Handler
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * 线程池辅助类
 */
object ThreadUtils {
    // 核心线程数
    private const val CORE_POOL_SIZE = 20

    // 最大线程数
    private const val MAX_POOL_SIZE = 150
    private const val KEEP_ALIVE_TIME = 10000L
    private val workQueue: BlockingQueue<Runnable> = ArrayBlockingQueue(10)

    // 线程工厂
    private val threadFactory: ThreadFactory = object : ThreadFactory {
        private val integer = AtomicInteger()
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "ThreadUtils thread:" + integer.getAndIncrement())
        }
    }

    // 线程池
    private lateinit var threadPool: ThreadPoolExecutor

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    fun execute(runnable: Runnable) {
        threadPool.execute(runnable)
    }

    /**
     *
     */
    @JvmOverloads
    fun execute(handler: Handler, url: String,
                hashMap: HashMap<String, String>?, what: Int, timeout: String? = "") {
        JsonCallback(handler, url, hashMap, what).initTimeout(timeout!!)
                .request()
    }

    @JvmOverloads
    fun execute(handler: Handler, url: String,
                hashMap: HashMap<String, String>?, what: Int, arg1: Int, timeout: String? = "") {
        JsonCallback(handler, url, hashMap, what, arg1)
                .initTimeout(timeout!!).request()
    }

    @JvmOverloads
    fun execute(handler: Handler, url: String,
                hashMap: HashMap<String, String>?, what: Int, arg1: Int, arg2: Int,
                timeout: String? = "") {
        JsonCallback(handler, url, hashMap, what, arg1, arg2).initTimeout(
                timeout!!).request()
    }

    init {
        threadPool = ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory)
    }
}