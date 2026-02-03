package multirender.wm.tiling

import multirender.wm.manager.Context
import multirender.wm.window.Window

object HorizontalTiler : Tiler {
    override fun tile(
        windows: List<Window>,
        width: Float,
        height: Float,
        context: Context
    ) {
        val wm = context.backend
        val count = windows.size
        val width = width / count
        val height = wm.getRemainingHeight()
        var offset = 0.0f

        for (window in windows) {
            window.set(context, offset, 0f, width, height)
            offset += width
        }
    }

}