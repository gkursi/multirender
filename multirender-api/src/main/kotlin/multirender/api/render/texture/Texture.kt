package xyz.qweru.multirender.api.render.texture

import java.nio.ByteBuffer

abstract class Texture(val width: Int, val height: Int, val content: ByteBuffer) {
    abstract fun bind()
    abstract fun bind(unit: Int)
}