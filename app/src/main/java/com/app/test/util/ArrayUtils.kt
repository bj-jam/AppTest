package com.app.test.util

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
object ArrayUtils {
    @JvmField
    val EMPTY_STRING_ARRAY = arrayOfNulls<String>(0)
    fun reverse(array: Array<Any?>?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: LongArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: IntArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: ShortArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: CharArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: ByteArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: DoubleArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: FloatArray?) {
        if (array != null) {
            reverse(array as FloatArray?, 0, array.size)
        }
    }

    fun reverse(array: BooleanArray?) {
        if (array != null) {
            reverse(array, 0, array.size)
        }
    }

    fun reverse(array: BooleanArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: ByteArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: CharArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: DoubleArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: FloatArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: IntArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: LongArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = Math.max(startIndexInclusive, 0)
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: Array<Any?>?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = if (startIndexInclusive < 0) 0 else startIndexInclusive
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }

    fun reverse(array: ShortArray?, startIndexInclusive: Int, endIndexExclusive: Int) {
        if (array != null) {
            var i = if (startIndexInclusive < 0) 0 else startIndexInclusive
            var j = Math.min(array.size, endIndexExclusive) - 1
            while (j > i) {
                val tmp = array[j]
                array[j] = array[i]
                array[i] = tmp
                --j
                ++i
            }
        }
    }
}