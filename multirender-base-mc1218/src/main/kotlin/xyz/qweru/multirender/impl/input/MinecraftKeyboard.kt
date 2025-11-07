package xyz.qweru.multirender.impl.input

import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.input.Input
import xyz.qweru.multirender.api.input.Keyboard
import xyz.qweru.multirender.api.input.event.KeyPressEvent
import xyz.qweru.multirender.impl.util.Globals.client

class MinecraftKeyboard : Keyboard {

    override fun input(key: Int, action: Input) {
        when (action) {
            Input.PRESS -> press(key)
            Input.RELEASE -> release(key)
            Input.CLICK -> {
                press(key)
                release(key)
            }
        }
    }

    fun press(key: Int) {
        client.keyboardHandler.keyPress(client.window.window, key, key, GLFW.GLFW_PRESS, 0)
    }

    fun release(key: Int) {
        client.keyboardHandler.keyPress(client.window.window, key, key, GLFW.GLFW_RELEASE, 0)
    }

    fun onKey(window: Long, key: Int, scan: Int, mod: Int) {
        if (window != client.window?.window) return
        KeyPressEvent.key = key
        KeyPressEvent.mod = mod
        KeyPressEvent.scan = scan
        API.event.post(KeyPressEvent)
    }
}