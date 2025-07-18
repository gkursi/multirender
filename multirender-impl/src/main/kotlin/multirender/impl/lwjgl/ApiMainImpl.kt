package xyz.qweru.multirender.impl.lwjgl

import org.joml.Math.sin
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector4f
import org.lwjgl.Version
import org.lwjgl.assimp.AIVector3D
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.Platform
import xyz.qweru.multirender.api.ApiMain
import xyz.qweru.multirender.api.Provider
import xyz.qweru.multirender.api.config.MRConfig
import xyz.qweru.multirender.api.render.shader.ShaderProgram
import xyz.qweru.multirender.api.render.shader.ShaderProvider
import xyz.qweru.multirender.api.render.shader.ShaderType
import xyz.qweru.multirender.api.render.texture.Texture
import xyz.qweru.multirender.api.render.texture.TextureProvider
import xyz.qweru.multirender.api.util.Profiler
import xyz.qweru.multirender.impl.lwjgl.input.KeyboardImpl
import xyz.qweru.multirender.impl.lwjgl.render.BufferUtils
import xyz.qweru.multirender.impl.lwjgl.render.StateManager
import xyz.qweru.multirender.impl.lwjgl.render.dim2.Context2dImpl
import xyz.qweru.multirender.impl.lwjgl.render.shader.ShaderProgramImpl
import xyz.qweru.multirender.impl.lwjgl.render.shader.ShaderProviderImpl
import xyz.qweru.multirender.impl.lwjgl.render.texture.TextureProviderImpl
import xyz.qweru.multirender.impl.lwjgl.util.misc.Locks
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque


/*
    todo: callbacks - render, mouse
 */
class ApiMainImpl : ApiMain {
    private val renderCalls: Deque<(ApiMain) -> Unit> = ConcurrentLinkedDeque()

    private val shaderProvider: ShaderProvider = ShaderProviderImpl()
    private val textureProvider: TextureProvider = TextureProviderImpl()

    private lateinit var shaderProgram: ShaderProgram
    private var vbo: IntArray = intArrayOf(0, 0)
    private var vao: IntArray = intArrayOf(0, 0)
    private var ebo: IntArray = intArrayOf(0, 0)
    private lateinit var texture: Texture
    private lateinit var texture1: Texture

    private var window: Long = NULL
    private var width = 0
    private var height = 0
    private var focused = false

    private lateinit var renderThread: Thread

    override fun onInit() {
        if (Constants.IS_MAC) {
            println("Detected MacOS, using main thread")
            renderThread = Thread.currentThread();
            onInit0()
        } else {
            renderThread = Thread(this::onInit0)
            renderThread.start()
        }
    }

    private fun onInit0() {
        synchronized(Locks.START_STOP) {
            renderThread.name = "MR Render Thread"
            println("Hello LWJGL " + Version.getVersion() + "!")
            initGlfw()
            createWindow()
            initShaders()
            initBuffers()
            println("Initialized Multirender")
            while (!glfwWindowShouldClose(window)) {
                val frameStart = System.nanoTime()
                render()
                Profiler.lastFrame = System.nanoTime() - frameStart
            }
            println("Shutting down")
            destroyGlfw()
        }
    }

    override fun recordRenderCall(renderCall: (ApiMain) -> Unit) {
        synchronized(Locks.RECORD_CALL) {
            renderCalls.push(renderCall)
        }
    }

    override fun stop() {
        synchronized(Locks.START_STOP) {
            println("Received stop")
            if (Thread.currentThread().equals(renderThread)) glfwSetWindowShouldClose(window, true)
            else recordRenderCall { glfwSetWindowShouldClose(window, true); }
        }
    }

    override fun isOnRenderThread(): Boolean {
        return Thread.currentThread().equals(renderThread)
    }

    private fun initGlfw() {
        // init glfw
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")
    }

    private fun createWindow() {
        // set windows hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_SAMPLES, MRConfig.MSAA_SAMPLES)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, 1)

        // create the window
        window = glfwCreateWindow(300, 300, "Multirenderer", NULL, NULL)
        if (window == NULL)
            throw RuntimeException("Failed to create window")

        // set handlers
        createKeyboardHandler()
        glfwSetFramebufferSizeCallback(window) { _, w, h ->
            width = w
            height = h
            glViewport(0, 0, width, height)
        }
        glfwSetWindowFocusCallback(window) {
            _, focused ->
            this.focused = focused
            println("Window " + (if (focused) "" else "un") + "focused")
        }

        // method to center the window
        stackPush().use { stack ->
            // Get the window size passed to glfwCreateWindow
            val passedWidth = stack.mallocInt(1) // int*
            val passedHeight = stack.mallocInt(1) // int*
            glfwGetWindowSize(window, passedWidth, passedHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                window,
                (vidmode!!.width() - passedWidth[0]) / 2,
                (vidmode.height() - passedHeight[0]) / 2
            )
        }

        // set the windows context as the current context
        glfwMakeContextCurrent(window)
        // vsync config option
        if (MRConfig.VSYNC) glfwSwapInterval(1)
        else glfwSwapInterval(0)
        // show window
        glfwShowWindow(window)
        GL.createCapabilities()

        // get initial window height
        val widthBuff = IntArray(1)
        val heightBuff = IntArray(1)
        glfwGetFramebufferSize(window, widthBuff, heightBuff)
        width = widthBuff[0]
        height = heightBuff[0]
        // set viewport
        glViewport(0, 0, width, height)

        // force reset all states
        StateManager.applyState()
        // apply smoothing for lines and polygons
        StateManager.applyState(StateManager.State.MSAA, StateManager.State.BLEND, StateManager.State.LINE_SMOOTH, StateManager.State.POLYGON_SMOOTH)

        Provider.context2d = Context2dImpl(window)
    }

    // temporary until i create a proper dynamic renderer
    private fun initBuffers() {
        // ... vertices ...
        // triangle vertices with color
        val vertices = floatArrayOf(
            // positions         // colors          // texture
             0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f, // bottom left
             0.0f,  0.5f, 0.0f,  0.0f, 0.0f, 1.0f,  0.5f, 1.0f, // top
        )
        // square vertices (no color)
        val vertices2 = floatArrayOf(
            0.0f, 0.0f, 0.0f, // tr2
            0.0f, -0.5f, 0.0f, // br2
            -0.5f, -0.5f, 0.0f, // bl2
            -0.5f, 0.0f, 0.0f, // tl2
        )

        // ... indices ...

        // square indices
        val squareIndices = intArrayOf(
            0, 1, 3, // 1st triangle (tr, br, tl)
            1, 2, 3, // 2nd triangle (br, bl, tl)
//            4, 5, 7,
//            5, 6, 7
        )
        val triangleIndices = intArrayOf(
            0, 1, 2
        )

        // ... texture coords ...
        val triangleTexCoords = floatArrayOf(
            0.0f, 0.0f, // bottom left
            1.0f, 0.0f, // bottom right,
            0.5f, 1.0f, // top
        )

        // create and bind the buffers (vertex array always has to be first)
        vao[0] = BufferUtils.createVao()
        ebo[0] = BufferUtils.createBuffer()
        vbo[0] = BufferUtils.createBuffer()
//        vao[1] = BufferUtils.createVao()
//        ebo[1] = BufferUtils.createBuffer()
//        vbo[1] = BufferUtils.createBuffer()

        BufferUtils.bindVao(vao[0])
        BufferUtils.bindVbo(vbo[0], vertices)
        BufferUtils.bindEbo(ebo[0], triangleIndices)

        // set vertex attribute pointer (stride - space between vertices on the vbo)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.SIZE_BYTES, 0L)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.SIZE_BYTES, 6L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)

        println("Created triangle vbo with id ${vbo[0]}")
        println("Created vao with id ${vao[0]}")
        println("Created ebo with id ${ebo[0]}")

        // unbind everything (unbind vertex array before element array!!)
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        // bind secondary buffers and repeat everything ...............................................
//        BufferUtils.bindVao(vao[1])
//        BufferUtils.bindVbo(vbo[1], vertices2)
//        BufferUtils.bindEbo(ebo[1], triangleIndices)
//
//        // set vertex attribute (stride - space between vertices on the vbo)
//        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0L)
//        glEnableVertexAttribArray(0)
//
//        println("Created triangle vbo with id ${vbo[1]}")
//        println("Created vao with id ${vao[1]}")
//        println("Created ebo with id ${ebo[1]}")
//
//        // unbind everything (unbind vertex array before element array!!)
//        glBindVertexArray(0)
//        glBindBuffer(GL_ARRAY_BUFFER, 0)
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        texture = textureProvider.findOrCreateTexture("/images/wall.jpg")
        texture1 = textureProvider.findOrCreateTexture("/images/awesomeface.png")
    }

    private fun initShaders() {
        val vertexShader = shaderProvider.compileShaderPath("core/first.vsh", ShaderType.Vertex)
        val fragmentShader = shaderProvider.compileShaderPath("core/first-orange.fsh", ShaderType.Fragment)

        shaderProgram = ShaderProgramImpl(vertexShader, fragmentShader)
        // set texture sampler order
        shaderProgram.use()
        shaderProgram.setUniform1i("texture1", 0)
        shaderProgram.setUniform1i("texture2", 1)
        shaderProgram

        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)

        var trans: Matrix4f = Matrix4f(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f)
        trans = trans.translate(0.5f, -0.5f, 0.0f)
        trans = trans.rotate(Quaternionf().rotationX(glfwGetTime().toFloat()).rotationY(glfwGetTime().toFloat()))
        shaderProgram.setUniformMatrix4fv("transform", trans.get(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)))
    }

    private fun destroyGlfw() {
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun render() {
        synchronized(Locks.RENDER) {
            glClearColor(0.2f, 0.3f, 0.3f, 0.0f)
            glClear(GL_COLOR_BUFFER_BIT) // clear the framebuffer

            // get green value based on time
            val timeValue = glfwGetTime().toFloat()
            val value: Float = (sin(timeValue) / 2.0f) + 0.5f

            shaderProgram.use(); // bind program
            textureProvider.findOrCreateTexture("/images/wall.jpg").bind(0)
            textureProvider.findOrCreateTexture("/images/awesomeface.png").bind(1)
//            texture.bind(0)
//            texture1.bind(1)
            BufferUtils.bindVao(vao[0]);

            var trans = Matrix4f(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f)
            trans = trans.rotate(Quaternionf().rotationX(glfwGetTime().toFloat() / 2f))
            trans = trans.rotate(Quaternionf().rotationY(glfwGetTime().toFloat() / 2f))
//            trans = trans.translate(-0.5f, 0.5f, 0.0f)
            shaderProgram.setUniformMatrix4fv("transform", trans.get(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)))

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            trans = Matrix4f(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f)
            trans = trans.rotate(Quaternionf().rotationX(glfwGetTime().toFloat()))
            trans = trans.rotate(Quaternionf().rotationY(glfwGetTime().toFloat()))
//            trans = trans.translate(0.5f, -0.5f, 0.0f)
            shaderProgram.setUniformMatrix4fv("transform", trans.get(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)))

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            trans = Matrix4f(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f)
            trans = trans.rotate(Quaternionf().rotationX(glfwGetTime().toFloat() * 2f))
            trans = trans.rotate(Quaternionf().rotationY(glfwGetTime().toFloat() * 2f))
//            trans = trans.translate(-0.5f, 0.5f, 0.0f)
            shaderProgram.setUniformMatrix4fv("transform", trans.get(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)))

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            trans = Matrix4f(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f)
            trans = trans.rotate(Quaternionf().rotationX(glfwGetTime().toFloat() * 4f))
            trans = trans.rotate(Quaternionf().rotationY(glfwGetTime().toFloat() * 4f))
//            trans = trans.translate(-0.5f, 0.5f, 0.0f)
            shaderProgram.setUniformMatrix4fv("transform", trans.get(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)))

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

//            BufferUtils.bindVao(vao[1]);
//            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            BufferUtils.bindVao(0)

            // record any stored render calls
            synchronized(Locks.RECORD_CALL) {
                renderCalls.forEach { it.invoke(this) }
                renderCalls.clear()
            }

            // swap buffers and poll events
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    private fun createKeyboardHandler() {
        Provider.keyboardHandler = KeyboardImpl()
        glfwSetKeyCallback(window) { window: Long, key: Int, _: Int, action: Int, modifiers: Int ->
            if (Provider.keyboardHandler is KeyboardImpl) {
                (Provider.keyboardHandler as KeyboardImpl).onKey(window, key, action, modifiers)
            }
        }
    }
}