package xyz.qweru.multirender.impl.input

import net.minecraft.client.input.MouseButtonInfo
import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.input.Input
import xyz.qweru.multirender.api.input.Mouse
import xyz.qweru.multirender.api.input.event.MouseMoveEvent
import xyz.qweru.multirender.impl.mixin.mixininterface.MouseInvoker
import xyz.qweru.multirender.impl.util.Globals.client

class MinecraftMouse : Mouse {

    override val x: Float
        get() = (client.mouseHandler as MouseInvoker).xPos.toFloat()

    override val y: Float
        get() = (client.mouseHandler as MouseInvoker).yPos.toFloat()

    fun press(button: Int) {
        (client.mouseHandler as MouseInvoker).invokeOnButton(
            client.window.handle(),
            MouseButtonInfo(
                button,
                0
            ),
            GLFW.GLFW_PRESS
        )
    }

    fun release(button: Int) {
        (client.mouseHandler as MouseInvoker).invokeOnButton(
            client.window.handle(),
            MouseButtonInfo(
                button,
                0
            ),
            GLFW.GLFW_RELEASE
        )
    }

    fun onMove(window: Long, x: Double, y: Double) {
        if (window != client.window.handle()) return
        MouseMoveEvent.x = x
        MouseMoveEvent.y = y
        API.events.post(MouseMoveEvent)
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