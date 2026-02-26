package xyz.qweru.multirender.api.util.color

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Mutable alternative to `java.awt.Color`
 */
data class Color(@JvmField var rgb: Int) {

    companion object {
        val red
            get() = Color(255, 0, 0)
        val green
            get() = Color(0, 255, 0)
        val blue
            get() = Color(0, 0, 255)
        val white
            get() = Color(255, 255, 255)
        val black
            get() = Color(0, 0, 0)
        val transparent
            get() = Color(0, 0, 0, 0)

        val orange
            get() = Color(255, 200, 0)
        val gray
            get() = Color(128, 128, 128)
        val darkGray
            get() = Color(64, 64, 64)
        val pink
            get() = Color(255, 175, 175)
    }

    var red: Int
        get() = (rgb shr 16) and 0xff
        set(value) {
            rgb = (rgb and 0xFF00FFFF.toInt()) or ((value and 0xFF) shl 16)
        }
    var green: Int
        get() = (rgb shr 8) and 0xff
        set(value) {
            rgb = (rgb and 0xFFFF00FF.toInt()) or ((value and 0xFF) shl 8)
        }
    var blue: Int
        get() = rgb and 0xff
        set(value) {
            rgb = (rgb and 0xFFFFFF00.toInt()) or (value and 0xFF)
        }
    var alpha: Int
        get() = (rgb shr 24) and 0xff
        set(value) {
            rgb = (rgb and 0x00FFFFFF) or ((value and 0xFF) shl 24)
        }

    fun factor(factor: Float, alphaF: Float = 1f): Color {
        red = (red * factor).roundToInt().coerceIn(0, 255)
        green = (green * factor).roundToInt().coerceIn(0, 255)
        blue = (blue * factor).roundToInt().coerceIn(0, 255)
        alpha = (alpha * alphaF).roundToInt().coerceIn(0, 255)
        return this
    }

    /**
     * @see java.awt.Color.brighter
     */
    fun brighter(): Color {
        val factor = (1.0 / 0.3).toInt()
        var r = red
        var g = green
        var b = blue

        if (r == 0 && g == 0 && b == 0) {
            red = factor
            green = factor
            blue = factor
            return this
        }

        if (r in 1..<factor) r = factor
        if (g in 1..<factor) g = factor
        if (b in 1..<factor) b = factor

        red = min((r / 0.7).toInt(), 255)
        green = min((g / 0.7).toInt(), 255)
        blue = min((b / 0.7).toInt(), 255)

        return this
    }

    /**
     * @see java.awt.Color.darker
     */
    fun darker(): Color {
        red = max((red * 0.7).roundToInt(), 0)
        green = max((green * 0.7).roundToInt(), 0)
        blue = max((blue * 0.7).roundToInt(), 0)
        return this
    }

    fun copy(r: Int = -1, g: Int = -1, b: Int = -1, a: Int = -1): Color {
        val new = Color(rgb)

        if (r != -1) new.red = r
        if (g != -1) new.green = g
        if (b != -1) new.blue = b
        if (a != -1) new.alpha = a

        return new
    }


    /**
     * Returns black (#000000FF) by default
     */
    constructor(r: Int = 0, g: Int = 0, b: Int = 0, a: Int = 255)
            : this(
        ((a and 0xff) shl 24)
                or ((r and 0xff) shl 16)
                or ((g and 0xff) shl 8)
                or ((b and 0xff) shl 0)
            )
}
