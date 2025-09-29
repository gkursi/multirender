package xyz.qweru.multirender.impl.render.texture

import com.mojang.blaze3d.textures.TextureFormat
import net.minecraft.client.texture.GlTexture

/**
 * "accessor" for the GlTexture constructor
 */
class MRGlTexture(width: Int, height: Int, glId: Int)
    : GlTexture(15, "", TextureFormat.RGBA8, width, height, 0, 0, glId) {
        override fun incrementRefCount() {}
        override fun decrementRefCount() = throw AssertionError("This shouldn't be called")
    }