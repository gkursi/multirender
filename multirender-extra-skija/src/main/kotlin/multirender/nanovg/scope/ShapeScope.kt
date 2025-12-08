package multirender.nanovg.scope

import multirender.nanovg.Scope
import org.lwjgl.nanovg.NanoVG

@Scope
class ShapeScope(private val handle: Long) {
    private val path = PathScope(handle)
    private val fill = StyleScope(handle)
    private val stroke = StrokeStyleScope(handle)

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
        stroke.apply(NanoVG::nvgFillPaint)
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