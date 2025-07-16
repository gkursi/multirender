package xyz.qweru.multirender.api.render.shader

abstract class ShaderProgram(val vertShader: Int, val fragShader: Int) {
    abstract fun getProgramId(): Int
    abstract fun use()

    // floats
    abstract fun setUniform1f(name: String, value: Float)
    abstract fun setUniform2f(name: String, value: Float, value2: Float)
    abstract fun setUniform3f(name: String, value: Float, value2: Float, value3: Float)
    abstract fun setUniform4f(name: String, value: Float, value2: Float, value3: Float, value4: Float)

    // ints
    abstract fun setUniform1i(name: String, value: Int)
    abstract fun setUniform2i(name: String, value: Int, value2: Int)
    abstract fun setUniform3i(name: String, value: Int, value2: Int, value3: Int)
    abstract fun setUniform4i(name: String, value: Int, value2: Int, value3: Int, value4: Int)

    // bools
    abstract fun setUniform1b(name: String, value: Boolean)
    abstract fun setUniform2b(name: String, value: Boolean, value2: Boolean)
    abstract fun setUniform3b(name: String, value: Boolean, value2: Boolean, value3: Boolean)
    abstract fun setUniform4b(name: String, value: Boolean, value2: Boolean, value3: Boolean, value4: Boolean)

    // matrix
    abstract fun setUniformMatrix4fv(name: String, value: FloatArray)
}