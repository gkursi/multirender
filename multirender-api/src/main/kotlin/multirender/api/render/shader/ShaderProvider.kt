package xyz.qweru.multirender.api.render.shader

import java.io.FileNotFoundException

interface ShaderProvider {
    fun compileShader(shaderSource: String, shaderType: ShaderType): Int

    /**
     * Compile a shader from jar resources
     */
    fun compileShaderPath(shaderName: String, shaderType: ShaderType): Int {
        val reader = this.javaClass.getResourceAsStream("/shaders/$shaderName");
        if (reader != null) {
            return compileShader(String(reader.readAllBytes()), shaderType);
        } else {
            throw RuntimeException(FileNotFoundException("/shaders/$shaderName"))
        }
    }
}