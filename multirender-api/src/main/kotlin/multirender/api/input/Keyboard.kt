package xyz.qweru.multirender.api.input

import xyz.qweru.multirender.api.util.CallbackHandler

interface Keyboard : CallbackHandler<(Long, Int, Int, Int) -> Unit> {
    /**
     * Callback parameters: `window, key, action, modifiers`
     * @return callback id
     */
    override fun registerCallback(callback: (Long, Int, Int, Int) -> Unit): Int

    class NoopKeyboard : Keyboard {
        override fun registerCallback(callback: (Long, Int, Int, Int) -> Unit): Int {
            throw IllegalStateException("Called Keyboard#registerCallback before initialization")
        }

        override fun removeCallback(id: Int): Boolean {
            throw IllegalStateException("Called Keyboard#removeCallback before initialization")
        }
    }
}