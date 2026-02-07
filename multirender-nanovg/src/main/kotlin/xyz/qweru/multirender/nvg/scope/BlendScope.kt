package xyz.qweru.multirender.nvg.scope

import xyz.qweru.multirender.nvg.Scope
import xyz.qweru.multirender.nvg.constant.Blend
import org.lwjgl.nanovg.NanoVG

@Scope
class BlendScope(val handle: Long) {
    var source = Blend.SRC_ALPHA
    var destination = Blend.ONE_MINUS_SRC_ALPHA

    internal fun apply() {
        NanoVG.nnvgGlobalCompositeBlendFunc(handle, source.id, destination.id)
    }

    internal fun reset() {
        source = Blend.SRC_ALPHA
        destination = Blend.ONE_MINUS_SRC_ALPHA

        apply() // recover state
    }
}