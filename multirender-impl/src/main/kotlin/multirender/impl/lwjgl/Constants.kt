package xyz.qweru.multirender.impl.lwjgl

import org.lwjgl.system.Platform

object Constants {
    val IS_MAC = Platform.get() == Platform.MACOSX;
}