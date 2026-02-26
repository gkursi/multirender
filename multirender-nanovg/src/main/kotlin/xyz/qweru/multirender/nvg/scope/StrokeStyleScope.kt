package xyz.qweru.multirender.nvg.scope

import xyz.qweru.multirender.nvg.NanoContext
import xyz.qweru.multirender.nvg.constant.LineCap
import xyz.qweru.multirender.nvg.constant.LineJoin
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG

class StrokeStyleScope(context: NanoContext) : StyleScope(context) {
    var width = 1f
    var cap = LineCap.BUTT
    var join = LineJoin.MITER
    var miterLimit = 10f

    override fun apply(consumer: (Long, NVGPaint) -> Unit) {
        NanoVG.nnvgLineCap(handle, cap.id)
        NanoVG.nnvgLineJoin(handle, join.id)
        NanoVG.nnvgMiterLimit(handle, miterLimit)
        NanoVG.nnvgStrokeWidth(handle, width)
        super.apply(consumer)
    }

    override fun reset() {
        width = 1f
        cap = LineCap.BUTT
        join = LineJoin.MITER
        miterLimit = 10f
        super.reset()
    }
}