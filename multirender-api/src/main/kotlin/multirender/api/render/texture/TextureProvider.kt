package xyz.qweru.multirender.api.render.texture

interface TextureProvider {
    fun findOrCreateTexture(path: String, forceReload: Boolean = false): Texture
}