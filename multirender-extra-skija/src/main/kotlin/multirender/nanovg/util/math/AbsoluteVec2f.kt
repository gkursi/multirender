package multirender.nanovg.util.math

data class AbsoluteVec2f(val x: Float, val y: Float) : Vec2f {
    override fun x(): Float = x
    override fun y(): Float = y
}