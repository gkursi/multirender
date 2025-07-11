package xyz.qweru.multirender.impl.lwjgl.input

import xyz.qweru.multirender.api.input.Keyboard
import xyz.qweru.multirender.impl.lwjgl.util.Locks

class KeyboardImpl : Keyboard {
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

    // 30, 20, 10, 5
    // 17
    // 30, 20, 17, 10, 5

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