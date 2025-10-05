package xyz.qweru.multirender.impl.render.texture

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage
import xyz.qweru.multirender.api.render.texture.Texture
import java.nio.ByteBuffer

class TextureImpl(val width: Int, val height: Int, val content: ByteBuffer, val channels: Int) : Texture {
    private var textureId: Int = -1
    private var format: Int = GL_RGB
    private var unloaded = false

    /**
     * Deletes the gpu texture
     */
    override fun deleteGpu() {
        if (textureId == -1) return
        glDeleteTextures(textureId)
        textureId = -1
    }

    fun unload() {
        if (unloaded) return
        STBImage.stbi_image_free(content)
        unloaded = true
    }

    override fun close() {
        deleteGpu()
        unload()
    }

    override fun isClosed(): Boolean = unloaded

    override fun bind(index: Int) {
        if (unloaded) throw IllegalStateException("Texture#bind called on an unloaded texture")
        if (textureId == -1) generate(index)
        else {
            glActiveTexture(GL_TEXTURE0 + index)
            glBindTexture(GL_TEXTURE_2D, textureId)
        }
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