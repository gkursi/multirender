package xyz.qweru.multirender.impl.input

import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.input.Keyboard
import xyz.qweru.multirender.impl.util.Globals
import xyz.qweru.multirender.impl.util.Globals.client
import xyz.qweru.multirender.impl.util.Locks

class MinecraftKeyboard : Keyboard {

    val callbacks: ArrayList<(Long, Int, Int, Int) -> Unit> = ArrayList();

    override fun registerCallback(callback: (Long, Int, Int, Int) -> Unit): Int {
        synchronized(Locks.KEYBOARD_CALLBACK) {
            var i = 0;
            val id = callback.hashCode();
            for (cb in callbacks) {
                if (cb.hashCode() < id) break;
                i++;
            }
            callbacks.add(i, callback);
            return callback.hashCode();
        }
    }

    override fun press(key: Int) {
        client.keyboardHandler.keyPress(client.window.window, key, key, GLFW.GLFW_PRESS, 0)
    }

    override fun release(key: Int) {
        client.keyboardHandler.keyPress(client.window.window, key, key, GLFW.GLFW_RELEASE, 0)
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

    fun onKey(window: Long, key: Int, scan: Int, mod: Int) {
        synchronized(Locks.KEYBOARD_CALLBACK) {
            callbacks.forEach {
                it.invoke(window, key, scan, mod);
            }
        }
    }
}