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
    private val handle = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS)
    private val drawer = ShapeScope(handle)
    private var open = true
    private var drawing = false

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

    private fun assertOpen() {
        if (!open) throw AssertionError("Context is closed")
    }
}