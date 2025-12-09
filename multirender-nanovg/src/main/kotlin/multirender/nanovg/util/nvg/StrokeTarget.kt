package multirender.nanovg.util.nvg

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG

object StrokeTarget : PropertyTarget {
    override fun paint(handle: Long, paint: NVGPaint) {
        NanoVG.nnvgStrokePaint(handle, paint.address());
    }

    override fun color(handle: Long, color: NVGColor) {
        NanoVG.nnvgStrokeColor(handle, color.address())
    }
}