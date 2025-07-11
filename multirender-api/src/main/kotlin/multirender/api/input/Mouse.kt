package xyz.qweru.multirender.api.input

import xyz.qweru.multirender.api.util.CallbackHandler

interface Mouse : CallbackHandler<(Long, Double, Double) -> Unit> {
    /**
     * Callback parameters: `window, x, y`
     * @return callback id
     */
    override fun registerCallback(callback: (Long, Double, Double) -> Unit): Int

    class NoopMouse : Mouse {
        override fun registerCallback(callback: (Long, Double, Double) -> Unit): Int {
            throw IllegalStateException("Called Mouse#registerCallback before initialization")
        }

        override fun removeCallback(id: Int): Boolean {
            throw IllegalStateException("Called Mouse#removeCallback before initialization")
        }
    }
}