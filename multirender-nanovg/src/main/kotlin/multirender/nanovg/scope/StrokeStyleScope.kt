package multirender.nanovg.scope

import multirender.nanovg.constant.LineCap
import multirender.nanovg.constant.LineJoin
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG

class StrokeStyleScope(handle: Long) : StyleScope(handle) {
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