package xyz.qweru.multirender.impl.render.texture

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.GpuTextureView
import net.minecraft.client.gui.render.TextureSetup
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import xyz.qweru.multirender.api.render.texture.Texture
import java.nio.ByteBuffer

/**
 * Wraps minecraft's `GpuTextureView`
 */
class MinecraftTexture(val width: Int, val height: Int, val content: ByteBuffer, val channels: Int,
                       val handler: MinecraftTextureHandler, val label: String) : Texture {
    private var textureId: Int = -1
    private var glTexture: MRGlTexture? = null
    private var glTextureView: GpuTextureView? = null
    private var format: Int = GL_RGB
    private var unloaded = false

    override fun deleteGpu() {
        if (textureId == -1) return
        glTexture?.close()
        textureId = -1
        glTexture = null
        glTextureView = null
    }

    /**
     * Unloads the resources used by the texture
     */
    fun unload() {
        if (unloaded) return
        handler.cache.remove(label)
        STBImage.stbi_image_free(content)
        unloaded = true
    }

    override fun close() {
        deleteGpu()
        unload()
    }

    override fun isClosed(): Boolean = unloaded

    override fun bind(index: Int) {
        createView(index)
        RenderSystem.setShaderTexture(index, glTextureView)
    }

    fun getTextureSetup(): TextureSetup {
        createView(0)
        return TextureSetup.singleTexture(glTextureView!!)
    }

    private fun generate(unit: Int) {
        textureId = glGenTextures()
        format = if (channels == 4) GL_RGBA else GL_RGB
        glActiveTexture(GL_TEXTURE0 + unit)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, content)
    }

    private fun createView(index: Int) {
        if (textureId == -1) {
            if (unloaded) throw IllegalStateException("Attempted usage of unloaded texture!")
            generate(index)
            glTexture = null // reset to prevent desync
        }
        if (glTexture == null) {
            glTexture = MRGlTexture(width, height, textureId)
            glTextureView = MRTextureView(glTexture!!, this)
        }
    }

}