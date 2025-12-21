package multirender.wm.animation

data class Timer(var length: Int) {
    private var time = 0L

    val progressNS: Long
        get() = System.nanoTime() - time
    val progress: Float
        get() = ((progressNS / 1_000_000.0) / length).toFloat().coerceIn(0f, 1f)

    init { reset() }

    fun reset() {
        time = System.nanoTime()
    }
}
