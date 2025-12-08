package multirender.nanovg.scope

import multirender.nanovg.Scope
import multirender.nanovg.constant.Winding
import org.lwjgl.nanovg.NanoVG

@Scope
class PathConfigScope(val handle: Long) {
    /**
     * Rotation of the path (in radians)
     */
    var angle = 0.0f

    var scaleX = 1.0f
    var scaleY = 1.0f

    var winding = Winding.SOLID

    fun reset() {
        angle = 0.0f
        scaleX = 1.0f
        scaleY = 1.0f
        winding = Winding.SOLID
        NanoVG.nvgRestore(handle)
    }

    fun applyPre() {
        NanoVG.nvgSave(handle)
        applyRotate()
        applyScale()
    }

    fun applyPost() {
        applyWinding()
    }

    private fun applyRotate() {
        if (angle == 0.0f) return
        NanoVG.nnvgRotate(handle, angle)
    }

    private fun applyScale() {
        if (scaleX == 1.0f && scaleY == 1.0f) return
        NanoVG.nnvgScale(handle, scaleX, scaleY)
    }

    private fun applyWinding() {
        if (winding == Winding.SOLID) return
        NanoVG.nnvgPathWinding(handle, winding.id)
    }
}