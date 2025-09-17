package xyz.qweru.multirender.impl.render.dim2

import xyz.qweru.multirender.api.render.dim2.Context2d
import xyz.qweru.multirender.impl.render.StateManager

class Context2dImpl() : Context2d {
    override fun msaa(block: () -> Unit) {
        StateManager.applyAdditionalState(StateManager.State.MSAA)
    }
}