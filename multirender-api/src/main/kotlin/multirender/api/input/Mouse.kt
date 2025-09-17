package xyz.qweru.multirender.api.input

import xyz.qweru.multirender.api.util.CallbackHandler

interface Mouse : CallbackHandler<(Long, Double, Double) -> Unit> {
    /**
     * Callback parameters: `window, x, y`
     * @return callback id
     */
    override fun registerCallback(callback: (Long, Double, Double) -> Unit): Int

    fun press(button: Int)
    fun release(button: Int)
}