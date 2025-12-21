package xyz.qweru.multirender.api.render.event

object PostRenderEvent

abstract class WindowSize {
    var width: Int = 1
    var height: Int = 1
    var ratio: Float = 1.0f
}

object WindowSizeChangeEvent : WindowSize()
object WindowCreateEvent : WindowSize()