package xyz.qweru.multirender.api

import xyz.qweru.multirender.api.input.Keyboard
import xyz.qweru.multirender.api.input.Mouse

object Provider {
    var API: ApiMain = ApiMain.NoopApiMain();
    var MOUSE_HANDLER: Mouse = Mouse.NoopMouse()
    var KEYBOARD_HANDLER: Keyboard = Keyboard.NoopKeyboard()
}