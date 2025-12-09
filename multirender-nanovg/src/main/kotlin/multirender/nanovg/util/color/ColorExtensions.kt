package multirender.nanovg.util.color

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

fun Color.set(o: NVGColor) = NanoVG.nvgRGBA(
    red.toByte(),
    green.toByte(),
    blue.toByte(),
    alpha.toByte(),
    o
)

operator fun Color.times(factor: Float) =
    Color(
        (red * factor).toInt(),
        (green * factor).toInt(),
        (blue * factor).toInt(),
        (alpha * factor).toInt(),
    )