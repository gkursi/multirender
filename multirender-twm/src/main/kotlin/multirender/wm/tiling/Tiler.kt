package multirender.wm.tiling

import multirender.wm.manager.Context
import multirender.wm.window.Window

interface Tiler {
    fun tile(windows: List<Window>, width: Float, height: Float, context: Context)
}