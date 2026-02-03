package multirender.wm.tiling

import multirender.wm.config.DefaultConfig
import multirender.wm.manager.Context
import multirender.wm.window.Window

/**
 * @see <a href="https://wiki.hypr.land/Configuring/Master-Layout/">Hyprland/Master Layout</a>
 */
object MasterTiler : Tiler {
    override fun tile(
        windows: List<Window>,
        width: Float,
        height: Float,
        context: Context
    ) {
        if (windows.isEmpty()) return

        if (windows.size == 1) {
            windows.first().set(context, 0f, 0f, width, height)
        } else {
            val count = windows.size - 1
            val masterWidth = DefaultConfig.masterWidth
            val singleWidth = width * (1f - masterWidth)
            val singleHeight = height / count

            windows.forEachIndexed { i, window ->
                if (i == 0) {
                    window.set(context, 0f, 0f, width * masterWidth, height)
                } else {
                    window.set(context, width * masterWidth, (i - 1) * singleHeight, singleWidth, singleHeight)
                }
            }
        }
    }
}