package multirender.wm.animation.curve

import multirender.wm.animation.Curve

// todo
data class CubicBezier(
    private val x1: Float, private val y1: Float,
    private val x2: Float, private val y2: Float
) : Curve {
    override fun get(time: Float): Float = 0f
}