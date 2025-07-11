package xyz.qweru.multirender.api.render.shader

interface ShaderProvider {
    fun compileVertexShader(source: String): Int
}