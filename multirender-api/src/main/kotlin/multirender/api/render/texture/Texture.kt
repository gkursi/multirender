package xyz.qweru.multirender.api.render.texture

import java.lang.AutoCloseable

interface Texture : AutoCloseable {

    /**
     * Frees all resources related to this texture.
     * It is up to the user to call this.
     */
    abstract override fun close()
    fun isClosed(): Boolean

    /**
     * Deletes this texture from the gpu vram without unloading it from memory. (re-uploaded on next use)
     */
    fun deleteGpu()

    fun bind(index: Int)

    fun bind() {
        bind(0)
    }
}