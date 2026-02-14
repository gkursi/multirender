package xyz.qweru.multirender.impl.render.window

import xyz.qweru.multirender.api.render.window.Window
import xyz.qweru.multirender.impl.util.Globals.client

object MinecraftWindow : Window {
    override fun getWidth(): Float =
        client.window.width.toFloat()

    override fun getHeight(): Float =
        client.window.height.toFloat()
}