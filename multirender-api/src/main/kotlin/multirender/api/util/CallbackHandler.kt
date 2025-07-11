package xyz.qweru.multirender.api.util

interface CallbackHandler<T> {
    /**
     * @return callback id
     */
    fun registerCallback(callback: T): Int

    /**
     * @param id the id returned by `registerCallback`
     * @return `true` if the callback was removed, `false` otherwise
     */
    fun removeCallback(id: Int): Boolean
}