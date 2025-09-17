package xyz.qweru.multirender.api.render.texture

import java.lang.AutoCloseable

abstract class Texture(val width: Int, val height: Int) : AutoCloseable {

    /**
     * Frees all resources related to this texture.
     * It is up to the user to call this.
     */
    abstract override fun close()
    abstract fun isClosed(): Boolean

    /**
     * Deletes this texture from the gpu vram without unloading it from memory.
     */
    abstract fun deleteGpu()

    abstract fun bind(index: Int)

    open fun bind() {
        bind(0)
    }
}