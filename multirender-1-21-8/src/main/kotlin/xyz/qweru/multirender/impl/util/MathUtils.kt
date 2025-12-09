package xyz.qweru.multirender.impl.util

import net.minecraft.client.gui.navigation.ScreenRectangle

object MathUtils {
    fun screenRect(x1: Float, y1: Float, x2: Float, y2: Float): ScreenRectangle
        = ScreenRectangle(x1.toInt(), y1.toInt(), (x2 - x1).toInt(), (y2 - y1).toInt())
}