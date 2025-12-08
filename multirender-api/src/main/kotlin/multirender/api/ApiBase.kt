package xyz.qweru.multirender.api

import xyz.qweru.multirender.api.render.window.Window

interface ApiBase {
    fun onInit()
    fun recordRenderCall(renderCall: (ApiBase) -> Unit)
    fun stop()
    fun isOnRenderThread(): Boolean
    fun getDeltaTime(): Float
    fun getWindow(): Window
}