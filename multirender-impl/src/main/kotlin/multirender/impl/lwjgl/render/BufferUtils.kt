package xyz.qweru.multirender.impl.lwjgl.render

import org.lwjgl.opengl.GL15

object BufferUtils {
    fun createVbo(vertices: FloatArray): Int {
        val buffers = IntArray(1);
        GL15.glGenBuffers(buffers);
        val vbo = buffers[0];
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(vbo, vertices, GL15.GL_STATIC_DRAW)
        return vbo;
    }
}