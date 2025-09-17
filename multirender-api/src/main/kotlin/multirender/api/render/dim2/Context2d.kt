package xyz.qweru.multirender.api.render.dim2

interface Context2d {
    fun msaa(block: () -> Unit)
}