package xyz.qweru.multirender.impl.input

import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.input.Input
import xyz.qweru.multirender.api.input.Mouse
import xyz.qweru.multirender.api.input.event.MouseMoveEvent
import xyz.qweru.multirender.impl.mixin.mixininterface.MouseInvoker
import xyz.qweru.multirender.impl.util.Globals.client
import xyz.qweru.multirender.impl.util.Locks

class MinecraftMouse : Mouse {

    fun press(button: Int) {
        (client.mouseHandler as MouseInvoker).invokeOnMouseButton(client.window.window, button, GLFW.GLFW_PRESS, 0)
    }

    fun release(button: Int) {
        (client.mouseHandler as MouseInvoker).invokeOnMouseButton(client.window.window, button, GLFW.GLFW_RELEASE, 0)
    }

    fun onMove(window: Long, x: Double, y: Double) {
        if (window != client.window?.window) return
        MouseMoveEvent.x = x
        MouseMoveEvent.y = y
        API.event.post(MouseMoveEvent)
    }

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

    override fun move(dx: Double, dy: Double) {
        val invoker = client.mouseHandler as MouseInvoker
        invoker.setDeltaX(dx)
        invoker.setDeltaY(dy)
    }
}