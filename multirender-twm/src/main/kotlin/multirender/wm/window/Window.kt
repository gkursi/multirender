package multirender.wm.window

import multirender.wm.WindowManager
import multirender.wm.backend.WindowBackend

class Window(private val backend: WindowBackend) {
    internal var life = System.nanoTime()
        private set
    internal var closing = false
        private set

    // These are
    internal var x = 0.0f
    internal var y = 0.0f
    internal var width = 0.0f
    internal var height = 0.0f

    /**
     * Assumes this window is in the `WindowManager`
     */
    fun render() {
        backend.render()
    }

    fun close() {
        closing = true
        life = System.nanoTime()
    }

    fun contains(x: Float, y: Float): Boolean =
        x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height

}