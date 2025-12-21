package multirender.nanovg.scope

import multirender.nanovg.NanoContext
import multirender.nanovg.Scope
import multirender.nanovg.constant.Winding
import multirender.nanovg.util.math.RelativeFloat
import multirender.nanovg.util.math.Vec2f
import org.lwjgl.nanovg.NanoVG

@Scope
class PathScope(val context: NanoContext) {
    val handle = context.handle
    val subConfig = PathConfigScope(handle)

    internal fun reset() {
        NanoVG.nvgBeginPath(handle)
    }

    inline fun rectangle(
        from: Vec2f, to: Vec2f,
        block: PathConfigScope.() -> Unit = {}
    ) = withConfig(block) {
        NanoVG.nvgRect(handle, from.x(), from.y(), to.x() - from.x(), to.y() - from.y())
        block.invoke(subConfig)
    }

    inline fun roundedRectangle(
        from: Vec2f, to: Vec2f, radius: Float,
        block: PathConfigScope.() -> Unit = {}
    ) = withConfig(block) {
        NanoVG.nvgRoundedRect(handle,
            from.x(), from.y(),
            to.x() - from.x(), to.y() - from.y(),
            radius
        )
    }

    inline fun roundedRectangle(
        from: Vec2f, to: Vec2f,
        radiusTL: Float, radiusTR: Float,
        radiusBL: Float, radiusBR: Float,
        block: PathConfigScope.() -> Unit = {}
    ) = withConfig(block) {
        NanoVG.nvgRoundedRectVarying(
            handle, from.x(), from.y(), to.x() - from.x(), to.y() - from.y(),
            radiusTL, radiusTR, radiusBL, radiusBR
        )
    }

    inline fun ellipse(
        center: Vec2f, radius: Vec2f,
        block: PathConfigScope.() -> Unit = {}
    ) = withConfig(block) {
        NanoVG.nvgEllipse(handle, center.x(), center.y(), radius.x(), radius.y())
    }

    inline fun circle(
        center: Vec2f, radius: RelativeFloat,
        block: PathConfigScope.() -> Unit = {}
    ) = withConfig(block) {
        circle(center, radius.f * radius.supplier.invoke())
    }

    inline fun circle(
        center: Vec2f, radius: Float,
        block: PathConfigScope.() -> Unit = {}
    ) = withConfig(block) {
        NanoVG.nvgCircle(handle,center.x(), center.y(), radius)
    }

    /**
     * Applies the given config to the given block
     */
    inline fun withConfig(configScope: PathConfigScope.() -> Unit, block: () -> Unit) {
        configScope.invoke(subConfig)
        subConfig.applyPre()
        block.invoke()
        subConfig.applyPost()
        subConfig.reset()
    }
}