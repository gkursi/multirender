package xyz.qweru.multirender.impl

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.ApiBase
import xyz.qweru.multirender.impl.input.MinecraftKeyboard
import xyz.qweru.multirender.impl.input.MinecraftMouse
import xyz.qweru.multirender.impl.render.dim2.MinecraftContext2d
import xyz.qweru.multirender.impl.render.texture.MinecraftTextureHandler
import xyz.qweru.multirender.impl.util.Globals.client

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

        API.textureHandler = MinecraftTextureHandler() // TODO: test when I implement 2d context
        API.context2d = MinecraftContext2d()
    }

    override fun recordRenderCall(renderCall: (ApiBase) -> Unit) = client.execute { renderCall.invoke(this) }

    override fun stop() = client.scheduleStop()

    override fun isOnRenderThread(): Boolean = RenderSystem.isOnRenderThread()
}
