package com.app.test.util;

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
public class ObjectUtils {
    public ObjectUtils() {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }
}
