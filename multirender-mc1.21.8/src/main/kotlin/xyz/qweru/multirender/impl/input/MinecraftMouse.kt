package xyz.qweru.multirender.impl.input

import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.input.Mouse
import xyz.qweru.multirender.impl.mixin.mixininterface.MouseInvoker
import xyz.qweru.multirender.impl.util.Globals.client
import xyz.qweru.multirender.impl.util.Locks

class MinecraftMouse : Mouse {
    val callbacks: ArrayList<(Long, Double, Double) -> Unit> = ArrayList();

    override fun registerCallback(callback: (Long, Double, Double) -> Unit): Int {
        synchronized(Locks.KEYBOARD_CALLBACK) {
            var i = 0;
            val id = callback.hashCode();
            for (cb in callbacks) {
                if (cb.hashCode() < id) break;
                i++
            }
            callbacks.add(i, callback);
            return callback.hashCode();
        }
    }

    override fun removeCallback(id: Int): Boolean {
        synchronized(Locks.KEYBOARD_CALLBACK) {
            callbacks.binarySearch {
                val code = it.hashCode();
                if (code == id) return@binarySearch 0;
                return@binarySearch if (code > id) -1 else 1
            }.also {
                if (it < 0) return false;
                callbacks.removeAt(it);
                return true
            }
        }
    }

    override fun press(button: Int) {
        (client.mouseHandler as MouseInvoker).invokeOnMouseButton(client.window.window, button, GLFW.GLFW_PRESS, 0)
    }

    override fun release(button: Int) {
        (client.mouseHandler as MouseInvoker).invokeOnMouseButton(client.window.window, button, GLFW.GLFW_RELEASE, 0)
    }

    fun onMove(window: Long, x: Double, y: Double) {
        synchronized(Locks.KEYBOARD_CALLBACK) {
            callbacks.forEach {
                it.invoke(window, x, y);
            }
        }
    }
}