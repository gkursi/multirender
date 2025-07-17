package xyz.qweru.multirender.impl.lwjgl.render.texture

import org.jetbrains.annotations.NotNull
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import xyz.qweru.multirender.api.render.texture.Texture
import xyz.qweru.multirender.api.render.texture.TextureProvider
import java.nio.ByteBuffer

class TextureProviderImpl : TextureProvider {

    val cache: HashMap<String, Texture> = HashMap()

    override fun findOrCreateTexture(path: String, forceReload: Boolean): Texture {
        return cache.takeIf { !forceReload } ?.computeIfAbsent(path, this::createTexture0)
                                             ?: createTexture0(path).also { cache[path] = it }
    }

    private fun createTexture0(path: String): Texture {
        val content: ByteBuffer?
        val width: Int
        val height: Int
        val size: Int

        MemoryStack.stackPush().use {
            val w = it.mallocInt(1)
            val h = it.mallocInt(1)
            val channels = it.mallocInt(1)

            val contentStream = javaClass.getResourceAsStream(path);
            if (contentStream == null) throw IllegalArgumentException("Could not find image $path")
            val bytes = contentStream.readAllBytes();
            val byteBuffer = BufferUtils.createByteBuffer(bytes.size);
            byteBuffer.put(bytes)
            byteBuffer.flip()

            content = STBImage.stbi_load_from_memory(byteBuffer, w, h, channels, 0)
            if (content == null) throw RuntimeException("Could not load image $path")
            width = w.get()
            height = h.get()
            size = bytes.size;
        }

        println("Loaded $path with size $size")
        return TextureImpl(width, height, content!!)
    }
}