package xyz.qweru.multirender.impl

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW
import xyz.qweru.multirender.api.ApiBase
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.impl.input.MinecraftKeyboard
import xyz.qweru.multirender.impl.input.MinecraftMouse
import xyz.qweru.multirender.impl.render.texture.MinecraftTextureHandler
import xyz.qweru.multirender.impl.util.Globals.client
import java.util.*

class Multirender : ModInitializer, ApiBase {
    private var init = false

    override fun onInitialize() {
        client = MinecraftClient.getInstance()
        API.base = this
        onInit()
    }

    override fun onInit() {
        if (init) throw IllegalStateException("onInit called twice")
        init = true

        API.keyboardHandler = MinecraftKeyboard()
        API.mouseHandler = MinecraftMouse()

        API.textureHandler = MinecraftTextureHandler()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                API.keyboardHandler.press(GLFW.GLFW_KEY_SPACE)
                API.keyboardHandler.release(GLFW.GLFW_KEY_SPACE)
                API.mouseHandler.press(GLFW.GLFW_MOUSE_BUTTON_1)
                API.mouseHandler.release(GLFW.GLFW_MOUSE_BUTTON_1)
            }
        }, 2000, 500)
    }

    override fun recordRenderCall(renderCall: (ApiBase) -> Unit) = client.execute { renderCall.invoke(this) }

    override fun stop() = client.scheduleStop()

    override fun isOnRenderThread(): Boolean = RenderSystem.isOnRenderThread()
}
