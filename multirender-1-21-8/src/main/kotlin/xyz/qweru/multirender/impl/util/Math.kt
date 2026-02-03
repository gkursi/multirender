package xyz.qweru.multirender.impl.util

import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.world.phys.Vec3
import xyz.qweru.multirender.api.util.math.Vec3d

fun screenRect(x1: Float, y1: Float, x2: Float, y2: Float): ScreenRectangle
    = ScreenRectangle(x1.toInt(), y1.toInt(), (x2 - x1).toInt(), (y2 - y1).toInt())

fun Vec3.toVec3d() = Vec3d.of(x, y, z)