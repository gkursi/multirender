package xyz.qweru.multirender.impl

import glm_.glm
import glm_.mat4x4.Mat4
import glm_.mat4x4.Mat4d
import glm_.vec3.Vec3
import org.joml.Math.sin
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import xyz.qweru.multirender.api.ApiBase
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.API.textureHandler
import xyz.qweru.multirender.impl.config.MRConfig
import xyz.qweru.multirender.api.render.shader.ShaderProgram
import xyz.qweru.multirender.api.render.shader.ShaderProvider
import xyz.qweru.multirender.api.render.shader.ShaderType
import xyz.qweru.multirender.api.render.texture.Texture
import xyz.qweru.multirender.api.util.Profiler
import xyz.qweru.multirender.impl.input.KeyboardImpl
import xyz.qweru.multirender.impl.render.BufferUtils
import xyz.qweru.multirender.impl.render.StateManager
import xyz.qweru.multirender.impl.render.dim2.Context2dImpl
import xyz.qweru.multirender.impl.render.shader.ShaderProgramImpl
import xyz.qweru.multirender.impl.render.shader.ShaderProviderImpl
import xyz.qweru.multirender.impl.render.texture.TextureHandlerImpl
import xyz.qweru.multirender.impl.util.misc.Locks
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

/*
    TODO: callbacks - render, mouse
 */
class ApiBaseImpl : ApiBase {
    private val renderCalls: Deque<(ApiBase) -> Unit> = ConcurrentLinkedDeque()

    private val shaderProvider: ShaderProvider = ShaderProviderImpl()

    private lateinit var shaderProgram: ShaderProgram
    private var vbo: IntArray = intArrayOf(0, 0)
    private var vao: IntArray = intArrayOf(0, 0)
    private var ebo: IntArray = intArrayOf(0, 0)
    private lateinit var texture: Texture
    private lateinit var texture1: Texture
    private lateinit var projMat: Mat4d
    private lateinit var modelMat: Mat4
    private lateinit var viewMat: Mat4

    private var window: Long = NULL
    private var width = 0
    private var height = 0
    private var focused = false

    private lateinit var renderThread: Thread

    override fun onInit() {
        if (Constants.IS_MAC) {
            println("Detected MacOS, using main thread")
            renderThread = Thread.currentThread()
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

            initCoordinates()
            compileShaders()
            createBuffers()

            println("Initialized Multirender")
        }

        while (!glfwWindowShouldClose(window)) {
            val frameStart = System.nanoTime()
            render()
            Profiler.lastFrame = System.nanoTime() - frameStart
        }

        synchronized(Locks.START_STOP) {
            println("Shutting down")
            destroyGlfw()
        }
    }

    override fun recordRenderCall(renderCall: (ApiBase) -> Unit) {
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


        setApi()

        // set handlers
        glfwSetFramebufferSizeCallback(window) { _, w, h ->
            width = w
            height = h
            glViewport(0, 0, width, height)
        }
        glfwSetWindowFocusCallback(window) { _, focused ->
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
        StateManager.applyState(
            StateManager.State.MSAA,
            StateManager.State.BLEND,
            StateManager.State.LINE_SMOOTH,
            StateManager.State.POLYGON_SMOOTH
        )

        API.context2d = Context2dImpl()
    }

    private fun initCoordinates() {
        // create projection matrix (view -> clip)
        // todo change fov, aspect ratio here
        projMat = glm.perspective(glm.radians(45f).toDouble(), (width / height).toDouble(), 0.1, 100.0)

        // create model matrix and world plane
        modelMat = Mat4.identity.rotate(glm.radians(-55.0f), Vec3(1f, 0f, 0f))

        // create view matrix and move it (the scene) backwards (the camera moves forwards)
        viewMat = Mat4.identity.translate(Vec3(0f, 0f, -3f))
    }

    // temporary until i create a proper dynamic renderer
    private fun createBuffers() {
        // ... vertices ...
        // triangle vertices with color
        val vertices = floatArrayOf(
            // positions         // colors          // texture
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, // bottom left
            0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f, // top
        )
        // square vertices (no color)
        floatArrayOf(
            0.0f, 0.0f, 0.0f, // tr2
            0.0f, -0.5f, 0.0f, // br2
            -0.5f, -0.5f, 0.0f, // bl2
            -0.5f, 0.0f, 0.0f, // tl2
        )

        // ... indices ...

        // square indices
        intArrayOf(
            0, 1, 3, // 1st triangle (tr, br, tl)
            1, 2, 3, // 2nd triangle (br, bl, tl)
//            4, 5, 7,
//            5, 6, 7
        )
        val triangleIndices = intArrayOf(
            0, 1, 2
        )

        // ... texture coords ...
        floatArrayOf(
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

        texture = textureHandler.findOrCreateTexture("/images/wall.jpg")
        texture1 = textureHandler.findOrCreateTexture("/images/awesomeface.png")
    }

    private fun compileShaders() {
        val vertexShader = shaderProvider.compileShaderPath("core/first.vsh", ShaderType.Vertex)
        val fragmentShader = shaderProvider.compileShaderPath("core/first-orange.fsh", ShaderType.Fragment)

        shaderProgram = ShaderProgramImpl(vertexShader, fragmentShader)
        // set texture sampler order
        shaderProgram.use()
        shaderProgram.setUniform1i("texture1", 0)
        shaderProgram.setUniform1i("texture2", 1)
        shaderProgram.setUniformMatrix4fv("model", modelMat.toFloatArray())
        shaderProgram.setUniformMatrix4fv("view", viewMat.toFloatArray())
//        shaderProgram.setUniformMatrix4fv("projection", ArrayUtil.toFloatArray(projMat.toDoubleArray()))

        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
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
            (sin(timeValue) / 2.0f) + 0.5f

            shaderProgram.use() // bind program
            // todo revert
            textureHandler.findOrCreateTexture("/images/wall.jpg").bind(0)
            textureHandler.findOrCreateTexture("/images/awesomeface.png").bind(1)
//            texture.bind(0)
//            texture1.bind(1)
            BufferUtils.bindVao(vao[0])

            var trans = Mat4.identity.rotateX(glfwGetTime().toFloat() / 2f)
            trans = trans.rotateY(glfwGetTime().toFloat() / 2f)
            shaderProgram.setUniformMatrix4fv("transform", trans.toFloatArray())

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            trans = Mat4.identity.rotateX(glfwGetTime().toFloat())
            trans = trans.rotateY(glfwGetTime().toFloat())
            shaderProgram.setUniformMatrix4fv("transform", trans.toFloatArray())

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            trans = Mat4.identity.rotateX(glfwGetTime().toFloat() * 2f)
            trans = trans.rotateY(glfwGetTime().toFloat() * 2f)
            shaderProgram.setUniformMatrix4fv("transform", trans.toFloatArray())

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

            trans = Mat4.identity.rotateX(glfwGetTime().toFloat() * 4f)
            trans = trans.rotateY(glfwGetTime().toFloat() * 4f)
            shaderProgram.setUniformMatrix4fv("transform", trans.toFloatArray())

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
        API.keyboardHandler = KeyboardImpl()
        glfwSetKeyCallback(window) { window: Long, key: Int, _: Int, action: Int, modifiers: Int ->
            (API.keyboardHandler as KeyboardImpl).onKey(window, key, action, modifiers)
        }
    }

    private fun setApi() {
        createKeyboardHandler()
        API.textureHandler = TextureHandlerImpl()
    }
}