package xyz.qweru.multirender.gl

import xyz.qweru.geo.core.event.Handler
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.render.event.PostRenderEvent

object GlEventHandler {
    fun init() {
        API.events.subscribe(this)
    }

    @Handler
    private fun postRender(e: PostRenderEvent) {
        val state = GlState().save()
        API.events.post(GlRenderEvent)
        state.restore()
    }
}