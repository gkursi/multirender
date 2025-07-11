package xyz.qweru.multirender.impl.lwjgl.util

import org.lwjgl.opengl.GL20

object Util {
    /**
     * @param handler will be invoked with true on success and false on fail
     */
    fun logShaderErrorIfPresent(shader: Int, parameter: Int, handler: (Boolean) -> Unit) {
        val buf = IntArray(1);
        GL20.glGetShaderiv(shader, parameter, buf);
        handler.invoke(buf[0] == 1);
        System.out.flush()
        if (buf[0] != 1) {
            System.err.println(GL20.glGetShaderInfoLog(shader))
            System.err.flush()
        }
    }

    /**
     * @param handler will be invoked with true on success and false on fail
     */
    fun logProgramErrorIfPresent(program: Int, parameter: Int, handler: (Boolean) -> Unit) {
        val buf = IntArray(1);
        GL20.glGetProgramiv(program, parameter, buf);
        handler.invoke(buf[0] == 1);
        System.out.flush()
        if (buf[0] != 1) {
            System.err.println(GL20.glGetProgramInfoLog(program))
            System.err.flush()
        }
    }
}