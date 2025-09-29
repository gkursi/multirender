package xyz.qweru.multirender.impl.render.texture

import net.minecraft.client.texture.GlTextureView

class MRTextureView(gpuTexture: MRGlTexture, val texture: MinecraftTexture) : GlTextureView(gpuTexture, 0, 0) {
    override fun close() {
        texture.close()
    }

    override fun isClosed(): Boolean {
        return texture.isClosed()
    }
}