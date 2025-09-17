package xyz.qweru.multirender.impl.render.texture

import com.mojang.blaze3d.textures.GpuTextureView

class MRTextureView(gpuTexture: MRGlTexture, val texture: MinecraftTexture) : GpuTextureView(gpuTexture, 0, 0) {
    override fun close() {
        texture.close()
    }

    override fun isClosed(): Boolean {
        return texture.isClosed()
    }
}