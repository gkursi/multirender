package xyz.qweru.multirender.api.render.shader

interface ShaderProvider {
    fun compileShader(vert: String, frag: String): ShaderProgram
}