package xyz.qweru.multirender.api.input

interface Mouse {
    val x: Float
    val y: Float

    fun input(key: Int, action: Input)
    fun move(dx: Double, dy: Double)
}