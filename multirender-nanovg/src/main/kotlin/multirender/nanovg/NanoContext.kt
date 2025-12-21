package multirender.nanovg

import multirender.nanovg.scope.ShapeScope
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.nanovg.NanoVGGL3
import java.lang.AutoCloseable

/**
 * Holder of a NanoVG instance.
 * Call `begin()` and `end()` before and after all draw calls each frame.
 * Use `States` to preserve gl state.
 * Call `shape` to draw.
 * @param ratio - framebuffer width over window width
 */
class NanoContext(private var width: Float, private var height: Float, private var ratio: Float) : AutoCloseable {
    internal val handle = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS)
    private val drawer = ShapeScope(this)
    private var open = true
    private var drawing = false
    internal var colorTransform = floatArrayOf(1f, 1f, 1f, 1f)

    /**
     * @param ratio - framebuffer width over window width
     */
    fun updateTarget(width: Float, height: Float, ratio: Float) {
        this.width = width
        this.height = height
        this.ratio = ratio
    }

    fun begin() {
        assertOpen()
        NanoVG.nvgBeginFrame(handle, width, height, ratio)
        drawing = true
    }

    fun end() {
        assertOpen()
        NanoVG.nvgEndFrame(handle)
    }

    override fun close() {
        assertOpen()
        assert(!drawing) { "Cannot close context while drawing" }
        open = false
        NanoVGGL3.nvgDelete(handle)
    }

    fun shape(block: ShapeScope.() -> Unit) {
        assertOpen()
        drawer.reset()
        block.invoke(drawer)
    }

    fun moveOrigin(x: Float, y: Float) {
        assertOpen()
        NanoVG.nvgTranslate(handle, x, y)
    }

    fun setScissor(x: Float, y: Float, width: Float, height: Float) {
        assertOpen()
        NanoVG.nnvgScissor(handle, x, y, width, height)
    }

    fun clearScissor() {
        assertOpen()
        NanoVG.nnvgResetScissor(handle)
    }

    fun transformColor(r: Float = 1f, g: Float = 1f, b: Float = 1f, a: Float = 1f) {
        colorTransform[0] = r
        colorTransform[1] = g
        colorTransform[2] = b
        colorTransform[3] = a
    }

    private fun assertOpen() {
        if (!open) throw AssertionError("Context is closed")
    }
}