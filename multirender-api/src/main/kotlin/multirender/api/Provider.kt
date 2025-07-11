package xyz.qweru.multirender.api

import xyz.qweru.multirender.api.input.Keyboard
import xyz.qweru.multirender.api.input.Mouse
import xyz.qweru.multirender.api.render.Context2d

object Provider {
    var API: ApiMain = ApiMain.NoopApiMain();

    // input
    var mouseHandler: Mouse = Mouse.NoopMouse()
    var keyboardHandler: Keyboard = Keyboard.NoopKeyboard()

    // render
    var context2d: Context2d = Context2d.NoopContext2d();
}