package xyz.qweru.multirender.api.render.shader

interface ShaderProvider {
    fun compileShader(shaderSource: String, shaderType: ShaderType): Int
    fun compileShaderPath(shaderName: String, shaderType: ShaderType): Int
}