package xyz.qweru.multirender.impl.lwjgl.render.shader

import org.lwjgl.opengl.GL20
import xyz.qweru.multirender.api.render.shader.ShaderProvider
import xyz.qweru.multirender.api.render.shader.ShaderType
import java.io.FileNotFoundException

class ShaderProviderImpl : ShaderProvider {
    override fun compileShader(shaderSource: String, shaderType: ShaderType): Int {
        val shader = GL20.glCreateShader(shaderType.glInt);
        GL20.glShaderSource(shader, shaderSource)
        GL20.glCompileShader(shader);

        // check for errors
        val buf = IntArray(1);
        GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, buf);
        if (buf[0] != 1) {
            println("Warning: ${shaderType.name} shader compilation failed ")
            System.out.flush()
            println(GL20.glGetShaderInfoLog(shader))
        } else {
            println("Compiled ${shaderType.name} shader to id $shader")
        }

        return shader;
    }

    override fun compileShaderPath(shaderName: String, shaderType: ShaderType): Int {
        val reader = object {}.javaClass.getResourceAsStream("/shaders/$shaderName");
        if (reader != null) {
            return compileShader(String(reader.readAllBytes()), shaderType);
        } else {
            throw RuntimeException(FileNotFoundException("/shaders/$shaderName"))
        }
    }
}