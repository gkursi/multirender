package xyz.qweru.multirender.nvg.scope

import xyz.qweru.multirender.nvg.NanoContext
import xyz.qweru.multirender.nvg.Scope
import xyz.qweru.multirender.nvg.util.color.set
import xyz.qweru.multirender.api.util.math.Vec2f
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG
import xyz.qweru.multirender.api.util.color.Color
import kotlin.math.roundToInt

@Scope
open class StyleScope(private val context: NanoContext) {
    val handle = context.handle
    private val blend = BlendScope(handle)
    private var canBlend = true

    private val target = NVGPaint.create()
    private val colorA = NVGColor.create()
    private val colorB = NVGColor.create()

    var paint: PaintHolder = PaintHolder(target)
        set(_) {}

    // This creates a single-color gradient instead of an actual NVGColor,
    // because I was too lazy to abstract this further while still keeping it clean.
    fun solid(color: Color) =
        linearGradient(Vec2f.TOP_LEFT, Vec2f.BOTTOM_RIGHT, color, color)

    fun linearGradient(
        from: Vec2f, to: Vec2f,
        start: Color, end: Color
    ): PaintHolder {
        NanoVG.nvgLinearGradient(
            handle,
            from.x, from.y,
            to.x, to.y,
            set(start, colorA),
            set(end, colorB),
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
            center.x, center.y,
            radius.x, radius.y,
            set(start, colorA),
            set(end, colorB),
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
            from.x, from.y,
            to.x - from.x, to.y - from.y,
            radius, feather,
            set(inner, colorA),
            set(inner, colorB),
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

    private fun set(c: Color, nvg: NVGColor): NVGColor {
        val color = Color(
            (c.red * context.colorTransform[0]).roundToInt(),
            (c.green * context.colorTransform[1]).roundToInt(),
            (c.blue * context.colorTransform[2]).roundToInt(),
            (c.alpha * context.colorTransform[3]).roundToInt(),
        )
        return color.set(nvg)
    }

    // prevents requiring nanovg as a separate dependency
    data class PaintHolder(val color: NVGPaint)
}