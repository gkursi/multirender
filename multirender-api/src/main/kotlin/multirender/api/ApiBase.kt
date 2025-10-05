package xyz.qweru.multirender.api

interface ApiBase {
    fun onInit()
    fun recordRenderCall(renderCall: (ApiBase) -> Unit)
    fun stop()
    fun isOnRenderThread(): Boolean
    fun getDeltaTime(): Float
}