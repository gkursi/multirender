package xyz.qweru.multirender.impl.render.texture

import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import xyz.qweru.multirender.api.render.texture.Texture
import xyz.qweru.multirender.api.render.texture.TextureHandler
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import kotlin.use

/**
 * Loads textures using STBImage
 */
class MinecraftTextureHandler : TextureHandler {
    val cache: ConcurrentHashMap<String, Texture> = ConcurrentHashMap()

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("[MR] Clearing textures")
            closeAll()
            println("[MR] Finished")
            System.out.flush()
        })
    }

    override fun findOrCreateTexture(resourcePath: String, forceReload: Boolean): Texture {
        return cache.takeIf { !forceReload } ?.computeIfAbsent(resourcePath, this::createTexture0)
            ?: createTexture0(resourcePath).also { cache[resourcePath] = it }
    }

    override fun findOrCreateTexture(label: String, forceReload: Boolean, content: ByteArray): Texture {
        return cache.takeIf { !forceReload } ?.computeIfAbsent(label) { createTexture0(content, label) }
            ?: createTexture0(content, label).also { cache[label] = it }
    }

    fun closeAll() {
        cache.entries.removeIf { entry ->
            (entry.value as MinecraftTexture).unload()
            true
        }
    }

    private fun createTexture0(path: String): Texture {
        val contentStream = javaClass.getResourceAsStream(path);
        if (contentStream == null) throw IllegalArgumentException("Could not find image $path")
        return createTexture0(contentStream.readAllBytes(), path)
    }

    private fun createTexture0(bytes: ByteArray, label: String): Texture {
        val content: ByteBuffer?
        val width: Int
        val height: Int
        val channels: Int

        val byteBuffer = BufferUtils.createByteBuffer(bytes.size);
        byteBuffer.put(bytes)
        byteBuffer.flip()

        MemoryStack.stackPush().use {
            val w = it.mallocInt(1)
            val h = it.mallocInt(1)
            val ch = it.mallocInt(1)

            content = STBImage.stbi_load_from_memory(byteBuffer, w, h, ch, 0)
            if (content == null) throw RuntimeException("Could not load image $label")
            width = w.get()
            height = h.get()
            channels = ch.get()
        }

        return MinecraftTexture(width, height, content!!, channels, this, label)
    }
}