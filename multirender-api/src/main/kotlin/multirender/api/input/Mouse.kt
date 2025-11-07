package xyz.qweru.multirender.api.input

interface Mouse {
    fun input(key: Int, action: Input)
    fun move(dx: Double, dy: Double)
}