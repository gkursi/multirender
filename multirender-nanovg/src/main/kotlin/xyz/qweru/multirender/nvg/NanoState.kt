package xyz.qweru.multirender.nvg

import xyz.qweru.geo.core.event.Handler
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.render.event.WindowCreateEvent
import xyz.qweru.multirender.api.render.event.WindowSizeChangeEvent
import xyz.qweru.multirender.gl.GlEventHandler
import xyz.qweru.multirender.gl.GlRenderEvent

/**
 * Automatically manages a NanoContext.
 * Requires events `WindowCreateEvent`, `WindowSizeChangeEvent` and `PostRenderEvent` to be implemented.
 */
object NanoState {

    private lateinit var context: NanoContext

    fun init() {
        API.events.subscribe(this)
        GlEventHandler.init()
    }

    fun close() {
        context.close()
    }

    @Handler
    private fun postInit(e: WindowCreateEvent) {
        context = NanoContext(e.width.toFloat(), e.height.toFloat(), e.ratio)
    }

    @Handler
    private fun updateBuffer(e: WindowSizeChangeEvent) {
        context.updateTarget(e.width.toFloat(), e.height.toFloat(), e.ratio)
    }

    @Handler
    private fun postRender(e: GlRenderEvent) {
        NanoRenderEvent.context = context
        context.begin()
        API.events.post(NanoRenderEvent)
        context.end()
    }

}