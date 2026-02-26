package xyz.qweru.multirender.api.util.math

interface Vec2f {
    companion object {
        /**
         * Top left corner
         */
        val TOP_LEFT = absolute(0f, 0f)

        /**
         * Bottom right corner
         */
        val BOTTOM_RIGHT = relative(1f, 1f)

        fun absolute(x: Float, y: Float): Vec2f =
            AbsoluteVec2f(x, y)

        /**
         * @param x 0..1, based on the windows width
         * @param y 0..1, based on the windows height
         */
        fun relative(x: Float, y: Float): Vec2f =
            RelativeVec2f(x, y)
    }

    val x: Float
    val y: Float
}