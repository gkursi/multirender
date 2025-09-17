package xyz.qweru.multirender.impl.render

import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30

object BufferUtils {
    fun createBuffer(): Int {
        val buffers = IntArray(1);
        glGenBuffers(buffers);
        val buf = buffers[0]
        return buf;
    }

    fun bindVbo(vbo: Int, vertices: FloatArray) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    }

    fun bindEbo(ebo: Int, indices: IntArray) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    fun createVao(): Int {
        return GL30.glGenVertexArrays()
    }

    fun bindVao(vao: Int) {
        GL30.glBindVertexArray(vao)
    }
}