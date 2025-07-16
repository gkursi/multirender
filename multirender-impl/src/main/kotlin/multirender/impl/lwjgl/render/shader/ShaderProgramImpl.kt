package xyz.qweru.multirender.impl.lwjgl.render.shader

import org.lwjgl.opengl.GL20.*
import xyz.qweru.multirender.api.render.shader.ShaderProgram
import xyz.qweru.multirender.impl.lwjgl.util.misc.Util

class ShaderProgramImpl(vertShader: Int, fragShader: Int) : ShaderProgram(vertShader, fragShader) {

    private val shaderProgram: Int = glCreateProgram()

    init {
        glAttachShader(shaderProgram, vertShader);
        glAttachShader(shaderProgram, fragShader);
        glLinkProgram(shaderProgram);

        Util.logProgramErrorIfPresent(shaderProgram, GL_LINK_STATUS) {
            println((if(it) "Linked" else "Failed to link") + " shader program with id $shaderProgram (vert=$vertShader, frag=$fragShader)")
        }
    }

    override fun getProgramId(): Int {
        return shaderProgram
    }

    override fun use() {
        glUseProgram(shaderProgram)
    }

    override fun setUniform1f(name: String, value: Float) {
        val uniform = findUniformOrThrow(name);
        glUniform1f(uniform, value)
    }

    override fun setUniform2f(name: String, value: Float, value2: Float) {
        val uniform = findUniformOrThrow(name);
        glUniform2f(uniform, value, value2)
    }

    override fun setUniform3f(name: String, value: Float, value2: Float, value3: Float) {
        val uniform = findUniformOrThrow(name);
        glUniform3f(uniform, value, value2, value3)
    }

    override fun setUniform4f(name: String, value: Float, value2: Float, value3: Float, value4: Float) {
        val uniform = findUniformOrThrow(name);
        glUniform4f(uniform, value, value2, value3, value4)
    }

    override fun setUniform1i(name: String, value: Int) {
        val uniform = findUniformOrThrow(name);
        glUniform1i(uniform, value)
    }

    override fun setUniform2i(name: String, value: Int, value2: Int) {
        val uniform = findUniformOrThrow(name);
        glUniform2i(uniform, value, value2)
    }

    override fun setUniform3i(name: String, value: Int, value2: Int, value3: Int) {
        val uniform = findUniformOrThrow(name);
        glUniform3i(uniform, value, value2, value3)
    }

    override fun setUniform4i(name: String, value: Int, value2: Int, value3: Int, value4: Int) {
        val uniform = findUniformOrThrow(name);
        glUniform4i(uniform, value, value2, value3, value4)
    }

    override fun setUniform1b(name: String, value: Boolean) {
        val uniform = findUniformOrThrow(name);
        glUniform1i(uniform, if (value) 1 else 0)
    }

    override fun setUniform2b(name: String, value: Boolean, value2: Boolean) {
        val uniform = findUniformOrThrow(name);
        glUniform2i(uniform, if (value) 1 else 0, if (value) 1 else 0)
    }

    override fun setUniform3b(name: String, value: Boolean, value2: Boolean, value3: Boolean) {
        val uniform = findUniformOrThrow(name);
        glUniform3i(uniform, if (value) 1 else 0, if (value) 1 else 0, if (value) 1 else 0)
    }

    override fun setUniform4b(name: String, value: Boolean, value2: Boolean, value3: Boolean, value4: Boolean) {
        val uniform = findUniformOrThrow(name);
        glUniform4i(uniform, if (value) 1 else 0, if (value) 1 else 0, if (value) 1 else 0, if (value) 1 else 0)
    }

    override fun setUniformMatrix4fv(name: String, value: FloatArray) {
        val uniform = findUniformOrThrow(name)
        glUniformMatrix4fv(uniform, false, value)
    }

    private fun findUniformOrThrow(name: String): Int {
        val vertexColorLocation: Int = glGetUniformLocation(shaderProgram, name) // find uniform location
        if (vertexColorLocation != -1) return vertexColorLocation;
        throw IllegalArgumentException("Cannot find uniform $name in program id $shaderProgram")
    }
}