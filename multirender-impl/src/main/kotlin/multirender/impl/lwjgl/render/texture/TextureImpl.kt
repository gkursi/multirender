package xyz.qweru.multirender.impl.lwjgl.render.texture

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import xyz.qweru.multirender.api.render.texture.Texture
import java.nio.ByteBuffer

class TextureImpl(width: Int, height: Int, content: ByteBuffer) : Texture(width, height, content) {
    private var textureId: Int = 0;

    override fun bind() {
        bind(0)
    }

    override fun bind(unit: Int) {
        if (textureId == 0) generate(unit)
        else {
            glActiveTexture(GL_TEXTURE0 + unit)
            glBindTexture(GL_TEXTURE_2D, textureId)
        }
    }

    private fun generate(unit: Int) {
        textureId = glGenTextures()
        glActiveTexture(GL_TEXTURE0 + unit)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, content)
        glGenerateMipmap(GL_TEXTURE_2D) // todo: make this optional to save resources on textures that won't be scaled
    }
}