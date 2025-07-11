package xyz.qweru.multirender.api.render

interface Context2d {
    fun msaa(block: () -> Unit)

    class NoopContext2d : Context2d {
        override fun msaa(block: () -> Unit) {
            throw IllegalStateException("Context2d#msaa invoked before initialization")
        }
    }
}