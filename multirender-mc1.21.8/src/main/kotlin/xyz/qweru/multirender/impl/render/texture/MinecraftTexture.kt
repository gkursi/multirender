package xyz.qweru.multirender.impl.render.texture

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.GpuTextureView
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage
import xyz.qweru.multirender.api.render.texture.Texture
import java.nio.ByteBuffer

/**
 * Wraps minecraft's `GpuTextureView`
 */
class MinecraftTexture(width: Int, height: Int, val content: ByteBuffer, val channels: Int,
                       val handler: MinecraftTextureHandler, val label: String) : Texture(width, height) {
    private var textureId: Int = -1
    private var glTexture: MRGlTexture? = null
    private var glTextureView: GpuTextureView? = null
    private var format: Int = GL_RGB
    private var unloaded = false

    /**
     * Deletes the gpu texture
     */
    override fun deleteGpu() {
        if (textureId == -1) return
        glTexture?.close()
        textureId = -1
        glTexture = null
        glTextureView = null
    }

    fun unload() {
        if (unloaded) return
        STBImage.stbi_image_free(content)
        handler.cache.remove(label)
        unloaded = true
    }

    override fun close() {
        deleteGpu()
        unload()
    }

    override fun isClosed(): Boolean = unloaded

    override fun bind(index: Int) {
        if (textureId == -1) {
            if (unloaded) throw IllegalStateException("Texture#bind called on a closed texture")
            generate(index)
            glTexture = null // reset to prevent desync
        }
        if (glTexture == null) {
            glTexture = MRGlTexture(width, height, textureId)
            glTextureView = MRTextureView(glTexture!!, this)
        }

        RenderSystem.setShaderTexture(index, glTextureView)
    }

    private fun generate(unit: Int) {
        textureId = glGenTextures()
        format = if (channels == 4) GL_RGBA else GL_RGB
        glActiveTexture(GL_TEXTURE0 + unit)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, content)
        glGenerateMipmap(GL_TEXTURE_2D) // todo: make this optional to save resources on textures that won't be scaled
    }

}