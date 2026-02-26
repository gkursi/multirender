package xyz.qweru.multirender.api.util.math

interface Vec3d {
    companion object {
        val ZERO = of(0.0, 0.0, 0.0)

        fun of(x: Double, y: Double, z: Double): Vec3d =
            AbsoluteVec3d(x, y, z)
    }

    val x: Double
    val y: Double
    val z: Double
}