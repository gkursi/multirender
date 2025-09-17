package xyz.qweru.multirender.api

import xyz.qweru.multirender.api.input.Keyboard
import xyz.qweru.multirender.api.input.Mouse
import xyz.qweru.multirender.api.render.dim2.Context2d
import xyz.qweru.multirender.api.render.texture.TextureHandler

object API {
    lateinit var base: ApiBase

    lateinit var mouseHandler: Mouse
    lateinit var keyboardHandler: Keyboard

    // render
    lateinit var context2d: Context2d
    lateinit var textureHandler: TextureHandler
}