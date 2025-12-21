package multirender.wm.animation.curve

import multirender.wm.animation.Curve

data class CubicBezier1d(private val p1: Float, private val p2: Float) : Curve {
    override fun get(time: Float): Float =
        Curve.curve1d(time, p1, p2)
}