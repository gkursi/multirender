package xyz.qweru.multirender.impl.lwjgl

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import xyz.qweru.multirender.api.ApiMain
import xyz.qweru.multirender.api.Provider
import xyz.qweru.multirender.api.config.MRConfig
import xyz.qweru.multirender.api.render.shader.ShaderProvider
import xyz.qweru.multirender.impl.lwjgl.input.KeyboardImpl
import xyz.qweru.multirender.impl.lwjgl.render.shader.ShaderProviderImpl
import xyz.qweru.multirender.impl.lwjgl.util.Locks
import java.util.*
import java.util.concurrent.*

/*
    todo: callbacks - key, render, mouse
 */
class ApiMainImpl : ApiMain {
    private val renderCalls: Deque<(ApiMain) -> Unit> = ConcurrentLinkedDeque()

    private val shaderProvider: ShaderProvider = ShaderProviderImpl();
    private var vertexShader: Int = 0;

    private var window: Long = NULL
    private var width = 0;
    private var height = 0;

    private lateinit var renderThread: Thread

    override fun onInit() {
        synchronized(Locks.START_STOP) {
            println("Hello LWJGL " + Version.getVersion() + "!")
            renderThread = Thread {
                initGlfw()
                createWindow()
                initShaders();
                println("Initialized Multirender")
                while (!glfwWindowShouldClose(window)) {
                    render()
                }
                println("Shutting down")
                destroyGlfw()
            }
            renderThread.name = "Render Thread"
            renderThread.start()
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

    private fun initGlfw() {
        // init glfw
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")
    }

    private fun createWindow() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        window = glfwCreateWindow(300, 300, "Multirenderer", NULL, NULL)
        if (window == NULL)
            throw RuntimeException("Failed to create window")

        createKeyboardHandler()
        glfwSetFramebufferSizeCallback(window) { window, w, h ->
            width = w;
            height = h;
            glViewport(0, 0, width, height)
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

        glfwMakeContextCurrent(window)
        if (MRConfig.VSYNC) glfwSwapInterval(1)
        glfwShowWindow(window)
        GL.createCapabilities()
        glClearColor(6.0f, 0.1f, 0.6f, 1.0f)

        val widthBuff = IntArray(1)
        val heightBuff = IntArray(1)
        glfwGetFramebufferSize(window, widthBuff, heightBuff)
        width = widthBuff[0];
        height = heightBuff[0];
        glViewport(0, 0, width, height);
    }

    private fun initShaders() {
        vertexShader = shaderProvider.compileVertexShader("""
            #version 330 core
            layout (location = 0) in vec3 aPos;

            void main()
            {
                gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
            }
        """.trimIndent())
    }

    private fun destroyGlfw() {
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun render() {
        synchronized(Locks.RENDER) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            // render here
            val vertices = floatArrayOf(
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
            )

            // record any stored render calls
            synchronized(Locks.RECORD_CALL) {
                renderCalls.forEach {
                    it.invoke(this)
                }
            }

            // swap buffers and poll events
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    private fun createKeyboardHandler() {
        Provider.KEYBOARD_HANDLER = KeyboardImpl()
        glfwSetKeyCallback(window) { window: Long, key: Int, scancode: Int, action: Int, modifiers: Int ->
            if (Provider.KEYBOARD_HANDLER is KeyboardImpl) {
                (Provider.KEYBOARD_HANDLER as KeyboardImpl).onKey(window, key, action, modifiers)
            }
        }
    }
}