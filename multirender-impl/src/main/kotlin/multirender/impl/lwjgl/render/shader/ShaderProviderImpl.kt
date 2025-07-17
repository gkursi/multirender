package xyz.qweru.multirender.impl.lwjgl.render.shader

import org.lwjgl.opengl.GL20
import xyz.qweru.multirender.api.render.shader.ShaderProvider
import xyz.qweru.multirender.api.render.shader.ShaderType
import xyz.qweru.multirender.impl.lwjgl.util.misc.Util
import java.io.FileNotFoundException

class ShaderProviderImpl : ShaderProvider {
    override fun compileShader(shaderSource: String, shaderType: ShaderType): Int {
        val shader = GL20.glCreateShader(shaderType.glInt);
        GL20.glShaderSource(shader, shaderSource)
        GL20.glCompileShader(shader);

        Util.logShaderErrorIfPresent(shader, GL20.GL_COMPILE_STATUS) {
            println((if (it) "Compiled" else "Failed to compile") + " shader with id $shader")
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