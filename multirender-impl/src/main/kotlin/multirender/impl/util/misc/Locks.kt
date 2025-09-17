package xyz.qweru.multirender.impl.util.misc

object Locks {
    // main api
    var START_STOP = Object()
    var RENDER = Object()
    var RECORD_CALL = Object()

    // keyboard/mouse
    var KEYBOARD_CALLBACK = Object()
    var MOUSE_CALLBACK = Object()
}