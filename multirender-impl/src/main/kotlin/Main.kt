package xyz.qweru

import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.Provider
import xyz.qweru.multirender.api.config.MRConfig
import xyz.qweru.multirender.api.util.Profiler
import xyz.qweru.multirender.impl.lwjgl.ApiMainImpl
import xyz.qweru.multirender.impl.lwjgl.render.StateManager
import java.util.Timer
import java.util.TimerTask

fun main() {
    ApiMainImpl().also {
        Provider.API = it
        it.onInit()
    }
    var id = -1
    Timer(true).schedule(object : TimerTask() {
        override fun run() {
            id = Provider.keyboardHandler.registerCallback { window, key, action, mod ->
                if (action == 1) println("Key: $key")
                else if (key == GLFW.GLFW_KEY_ESCAPE) Provider.API.stop();
            }
        }
    }, 3000)
    Timer(true).schedule(object : TimerTask() {
        override fun run() {
            println("Last frame time: ${Profiler.lastFrame}, approx. fps: ${1000000000f / Profiler.lastFrame}")
        }
    }, 1000, 1000)
}