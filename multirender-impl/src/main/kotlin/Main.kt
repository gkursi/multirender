package xyz.qweru

import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.impl.util.misc.Profiler
import xyz.qweru.multirender.impl.ApiBaseImpl
import java.util.*

fun main() {
    ApiBaseImpl().also {
        API.base = it
        it.onInit()
    }
    var id = -1
    Timer(true).schedule(object : TimerTask() {
        override fun run() {
            id = API.keyboardHandler.registerCallback { window, key, action, mod ->
                if (action == 1) println("Key: $key")
                else if (key == GLFW.GLFW_KEY_ESCAPE) API.base.stop();
            }
        }
    }, 3000)
    Timer(true).schedule(object : TimerTask() {
        override fun run() {
            println("Last frame time: ${Profiler.lastFrame}, approx. fps: ${1000000000f / Profiler.lastFrame}")
        }
    }, 1000, 1000)
}