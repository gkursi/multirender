package xyz.qweru.multirender.api.util.color

/**
 * stored as RGBA
 */
data class Color(var rgba: Int) {

    companion object {
        @JvmField
        val RED = Color(r = 255)
        @JvmField
        val GREEN = Color(g = 255)
        @JvmField
        val BLUE = Color(b = 255)
    }

    var red: Int
        get() = (rgba shr 24) and 0xff
        set(value) {
            rgba = (rgba and 0x00FFFFFF) or ((value and 0xFF) shl 24)
        }
    var green: Int
        get() = (rgba shr 16) and 0xff
        set(value) {
            rgba = (rgba and 0xFF00FFFF.toInt()) or ((value and 0xFF) shl 16)
        }
    var blue: Int
        get() = (rgba shr 8) and 0xff
        set(value) {
            rgba = (rgba and 0xFFFF00FF.toInt()) or ((value and 0xFF) shl 8)
        }
    var alpha: Int
        get() = rgba and 0xff
        set(value) {
            rgba = (rgba and 0xFFFFFF00.toInt()) or ((value and 0xFF) shl 8)
        }

    constructor(r: Int = 0, g: Int = 0, b: Int = 0, a: Int = 0)
            : this((r shl 24) + (g shl 16) + (b shl 8) + (a))
}
