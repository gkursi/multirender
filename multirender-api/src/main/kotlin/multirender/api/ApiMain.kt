package xyz.qweru.multirender.api

interface ApiMain {
    fun onInit()
    fun recordRenderCall(renderCall: (ApiMain) -> Unit)
    fun stop()
    fun isOnRenderThread(): Boolean

    class NoopApiMain : ApiMain {
        override fun onInit() {
            throw IllegalStateException("ApiMain#onInit called before initialization")
        }

        override fun recordRenderCall(renderCall: (ApiMain) -> Unit) {
            throw IllegalStateException("ApiMain#recordRenderCall called before initialization")
        }

        override fun stop() {
            throw IllegalStateException("ApiMain#stop called before initialization")
        }

        override fun isOnRenderThread(): Boolean {
            throw IllegalStateException("ApiMain#isOnRenderThread called before initialization")
        }
    }
}