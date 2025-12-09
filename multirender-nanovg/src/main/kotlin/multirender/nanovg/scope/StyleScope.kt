package multirender.nanovg.scope

import multirender.nanovg.Scope
import multirender.nanovg.util.color.set
import multirender.nanovg.util.math.Vec2f
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

@Scope
open class StyleScope(val handle: Long) {
    private val blend = BlendScope(handle)
    private var canBlend = true

    private val target = NVGPaint.create()
    private val colorA = NVGColor.create()
    private val colorB = NVGColor.create()

    var paint: PaintHolder = PaintHolder(target)
        set(_) {}

    // This creates a single-color gradient instead of an actual NVGColor,
    // because I was too lazy to abstract this further, while still keeping it clean.
    fun solid(color: Color) =
        linearGradient(Vec2f.TOP_LEFT, Vec2f.BOTTOM_RIGHT, color, color)

    fun linearGradient(
        from: Vec2f, to: Vec2f,
        start: Color, end: Color
    ): PaintHolder {
        NanoVG.nvgLinearGradient(
            handle,
            from.x(), from.y(),
            to.x(), to.y(),
            start.set(colorA),
            end.set(colorB),
            target
        )
        return paint
    }

    /**
     * @param radius x - inRadius, y - outRadius
     */
    fun radialGradient(
        center: Vec2f, radius: Vec2f,
        start: Color, end: Color
    ): PaintHolder {
        NanoVG.nvgRadialGradient(
            handle,
            center.x(), center.y(),
            radius.x(), radius.y(),
            start.set(colorA),
            end.set(colorB),
            target
        )
        return paint
    }

    /**
     * @param feather the feather value. This defines how blurry the border of the rectangle is.
     * @param radius corner radius
     */
    fun boxGradient(
        from: Vec2f, to: Vec2f,
        inner: Color, outer: Color,
        radius: Float, feather: Float = 10f
    ): PaintHolder {
        NanoVG.nvgBoxGradient(
            handle,
            from.x(), from.y(),
            to.x() - from.x(), to.y() - from.y(),
            radius, feather,
            inner.set(colorA),
            outer.set(colorB),
            target
        )
        return paint
    }

    fun blend(block: BlendScope.() -> Unit) {
        assert(canBlend) { "Cannot set blend twice" }
        block.invoke(blend)
        canBlend = false
    }

    internal open fun reset() {
        blend.reset()
        canBlend = true
    }

    internal open fun apply(consumer: (Long, NVGPaint) -> Unit) {
        blend.apply()
        consumer.invoke(handle, target)
    }

    // prevents requiring nanovg as a separate dependency
    data class PaintHolder(val color: NVGPaint)
}