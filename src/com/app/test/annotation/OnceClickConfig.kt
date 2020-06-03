package com.app.test.annotation

object OnceClickConfig {
    private const val DEFAULT_DURING = 1000L
    private var during: Long = 0
    @JvmStatic
    fun getDuring(): Long {
        if (during <= 0L) {
            during = 1000L
        }
        return during
    }

    fun init(debug: Boolean) {
        init(1000L, debug)
    }

    fun init(during: Long, debug: Boolean) {
        OnceClickConfig.during = during
        //        OnceClickLogger.debug(debug);
    }
}