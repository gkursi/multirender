package xyz.qweru.multirender.api.input

import xyz.qweru.multirender.api.util.CallbackHandler

interface Keyboard : CallbackHandler<(Long, Int, Int, Int) -> Unit> {
    /**
     * Callback parameters: `window, key, action, modifiers`
     * @return callback id
     */
    override fun registerCallback(callback: (Long, Int, Int, Int) -> Unit): Int

    fun press(key: Int)
    fun release(key: Int)
}