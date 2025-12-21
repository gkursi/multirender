package multirender.nanovg.scope

import multirender.nanovg.NanoContext
import multirender.nanovg.Scope
import multirender.nanovg.util.color.times
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

@Scope
class ShapeScope(context: NanoContext) {
    private val handle = context.handle
    private val path = PathScope(context)
    private val fill = StyleScope(context)
    private val stroke = StrokeStyleScope(context)

    private var canSetPath = true
    private var canSetFill = true
    private var canSetStroke = true

    internal fun reset() {
        canSetPath = true
        canSetFill = true
        canSetStroke = true

        path.reset()
        fill.reset()
        stroke.reset()
    }

    /**
     * Start a new path
     */
    fun path(block: PathScope.() -> Unit) {
        assert(canSetPath) { "Cannot set path twice" }
        block.invoke(path)
        canSetPath = false
    }

    fun fill(block: StyleScope.() -> Unit) {
        assert(canSetFill)
        block.invoke(fill)
        fill.apply(NanoVG::nvgFillPaint)
        canSetFill = false
        drawFill()
    }

    fun stroke(block: StrokeStyleScope.() -> Unit) {
        assert(canSetStroke)
        block.invoke(stroke)
        stroke.apply(NanoVG::nvgStrokePaint)
        canSetStroke = false
        drawStroke()
    }

    fun drawFill() {
        NanoVG.nvgFill(handle)
    }

    fun drawStroke() {
        NanoVG.nvgStroke(handle)
    }
}