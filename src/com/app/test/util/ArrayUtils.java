package com.app.test.util;


/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
public class ArrayUtils {
    static final String[] EMPTY_STRING_ARRAY = new String[0];


    static void reverse(Object[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(long[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(int[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(short[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(char[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(byte[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(double[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(float[] array) {
        if (array != null) {
            reverse((float[]) array, 0, array.length);
        }
    }

    public static void reverse(boolean[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array != null) {
            int i = Math.max(startIndexInclusive, 0);

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
            int i = Math.max(startIndexInclusive, 0);
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
            int i = Math.max(startIndexInclusive, 0);
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
            int i = Math.max(startIndexInclusive, 0);
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
            int i = Math.max(startIndexInclusive, 0);
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
            int i = Math.max(startIndexInclusive, 0);
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
            int i = Math.max(startIndexInclusive, 0);
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
}

