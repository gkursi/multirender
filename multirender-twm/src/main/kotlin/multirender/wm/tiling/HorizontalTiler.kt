package multirender.wm.tiling

import multirender.wm.backend.WMBackend
import multirender.wm.window.Window

object HorizontalTiler : Tiler {
    override fun render(
        windows: List<Window>,
        width: Float, height: Float,
        wm: WMBackend
    ) {
        val count = windows.size
        val width = width / count
        val height = wm.getRemainingHeight()
        var offset = 0.0f

        for (window in windows) {
            window.set(offset, 0f, width, height)
            window.render(wm)
            offset += width
        }
    }

}