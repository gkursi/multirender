package multirender.nanovg.util.math

import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.render.window.Window

/**
 * Vec2f implementation with relative values
 */
data class RelativeVec2f(val xValue: RelativeFloat, val yValue: RelativeFloat) : Vec2f {
    companion object {
        private val window: Window
            get() = API.base.getWindow()
    }

    /**
     * @param x 0..1, based on the windows width
     * @param y 0..1, based on the windows height
     */
    constructor(x: Float, y: Float) : this(
        RelativeFloat(x) { window.getWidth() },
        RelativeFloat(y) { window.getHeight() }
    )

    private val screenX by xValue
    private val screenY by yValue

    override fun x(): Float = screenX
    override fun y(): Float = screenY
}
