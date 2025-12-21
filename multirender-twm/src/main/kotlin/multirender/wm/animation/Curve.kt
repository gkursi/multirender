package multirender.wm.animation

interface Curve {
    companion object {
        fun curve1d(t: Float, a: Float, b: Float) =
            3 * (1 - t) * (1 - t) * t * a +
            3 * (1 - t) * t * t * b +
            t * t * t
    }

    fun get(time: Float): Float
}