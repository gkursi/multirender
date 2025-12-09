package multirender.nanovg.util.nvg

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint

interface PropertyTarget {
    fun paint(handle: Long, paint: NVGPaint)
    fun color(handle: Long, color: NVGColor)
}