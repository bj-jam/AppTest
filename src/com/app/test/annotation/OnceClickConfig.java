package com.app.test.annotation;

public class OnceClickConfig {
    private static final long DEFAULT_DURING = 1000L;
    private static long during;

    public OnceClickConfig() {
    }

    public static Long getDuring() {
        if (during <= 0L) {
            during = 1000L;
        }

        return during;
    }

    public static void init(boolean debug) {
        init(1000L, debug);
    }

    public static void init(long during, boolean debug) {
        OnceClickConfig.during = during;
//        OnceClickLogger.debug(debug);
    }
}
