package xyz.qweru.multirender.impl.lwjgl.render.shader

import org.lwjgl.opengl.GL20
import xyz.qweru.multirender.api.render.shader.ShaderProvider

class ShaderProviderImpl : ShaderProvider {
    override fun compileVertexShader(source: String): Int {
        val shader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(shader, source)
        GL20.glCompileShader(shader);

        // check for errors
        val buf = IntArray(1);
        GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, buf);
        if (buf[0] != 1) {
            println("Warning: shader compilation failed")
            println(GL20.glGetShaderInfoLog(shader))
        } else {
            println("Compiled shader to id $shader")
        }

        return shader;
    }
}