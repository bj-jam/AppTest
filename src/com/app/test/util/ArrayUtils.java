package com.app.test.util;


import java.util.Collection;
import java.util.Map;

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
public class ArrayUtils {
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final int INDEX_NOT_FOUND = -1;

    public ArrayUtils() {
    }

    public static void reverse(Object[] array) {
        if (array != null) {
            reverse((Object[]) array, 0, array.length);
        }
    }

    public static void reverse(long[] array) {
        if (array != null) {
            reverse((long[]) array, 0, array.length);
        }
    }

    public static void reverse(int[] array) {
        if (array != null) {
            reverse((int[]) array, 0, array.length);
        }
    }

    public static void reverse(short[] array) {
        if (array != null) {
            reverse((short[]) array, 0, array.length);
        }
    }

    public static void reverse(char[] array) {
        if (array != null) {
            reverse((char[]) array, 0, array.length);
        }
    }

    public static void reverse(byte[] array) {
        if (array != null) {
            reverse((byte[]) array, 0, array.length);
        }
    }

    public static void reverse(double[] array) {
        if (array != null) {
            reverse((double[]) array, 0, array.length);
        }
    }

    public static void reverse(float[] array) {
        if (array != null) {
            reverse((float[]) array, 0, array.length);
        }
    }

    public static void reverse(boolean[] array) {
        if (array != null) {
            reverse((boolean[]) array, 0, array.length);
        }
    }

    public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                boolean tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                byte tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                char tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                double tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                float tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                int tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                long tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                Object tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

            for (int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                short tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return null == obj;
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    public static boolean isNotEmpty(Object obj) {
        return null != obj;
    }

    public static boolean isNotEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    public static boolean isNotEmpty(int[] objs) {
        return null != objs && objs.length > 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return null != charSequence && charSequence.length() > 0;
    }
}

