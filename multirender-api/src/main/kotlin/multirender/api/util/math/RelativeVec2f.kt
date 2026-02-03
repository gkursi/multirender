package xyz.qweru.multirender.api.util.math

import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.render.window.Window

/**
 * Vec2f implementation with relative values
 */
class RelativeVec2f(xValue: RelativeFloat, valueX: RelativeFloat) : Vec2f {
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

    override val x by xValue
    override val y by valueX
}
