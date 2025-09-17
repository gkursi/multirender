package xyz.qweru.multirender.impl.util.math

object ArrayUtil {
    fun toFloatArray(doubleArray: DoubleArray): FloatArray {
        val floatArray = FloatArray(doubleArray.size)
        doubleArray.forEachIndexed { i, it -> floatArray[i] = it.toFloat() }
        return floatArray;
    }
}