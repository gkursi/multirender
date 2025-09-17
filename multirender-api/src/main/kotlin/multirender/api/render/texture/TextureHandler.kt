package xyz.qweru.multirender.api.render.texture

interface TextureHandler {
    /**
     * If not previously cached, create a texture from image data (from jvm resources)
     */
    fun findOrCreateTexture(resourcePath: String, forceReload: Boolean = false): Texture

    /**
     * If not previously cached, create a texture from the given data
     */
    fun findOrCreateTexture(label: String, forceReload: Boolean, content: ByteArray): Texture
}