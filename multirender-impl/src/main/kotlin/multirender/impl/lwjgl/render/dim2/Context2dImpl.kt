package xyz.qweru.multirender.impl.lwjgl.render.dim2

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13
import xyz.qweru.multirender.api.render.Context2d
import xyz.qweru.multirender.impl.lwjgl.render.StateManager

class Context2dImpl(windowHandle: Int) : Context2d {
    override fun msaa(block: () -> Unit) {
        StateManager.applyAdditionalState(StateManager.State.MSAA)
    }
}