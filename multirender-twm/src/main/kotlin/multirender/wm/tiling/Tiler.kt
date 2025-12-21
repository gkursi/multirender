package multirender.wm.tiling

import multirender.wm.backend.WMBackend
import multirender.wm.window.Window

interface Tiler {
    fun render(windows: List<Window>, width: Float, height: Float, wm: WMBackend)
}