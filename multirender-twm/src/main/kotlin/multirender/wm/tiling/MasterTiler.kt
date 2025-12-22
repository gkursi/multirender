package multirender.wm.tiling

import multirender.wm.backend.WMBackend
import multirender.wm.config.WMConfig
import multirender.wm.window.Window

/**
 * @see <a href="https://wiki.hypr.land/Configuring/Master-Layout/">Hyprland/Master Layout</a>
 */
object MasterTiler : Tiler {
    override fun render(
        windows: List<Window>,
        width: Float,
        height: Float,
        wm: WMBackend
    ) {
        if (windows.isEmpty()) return

        if (windows.size == 1) {
            windows.first().set(0f, 0f, width, height)
        } else {
            val count = windows.size - 1
            val masterWidth = WMConfig.masterWidth
            val singleWidth = width * (1f - masterWidth)
            val singleHeight = height / count

            windows.forEachIndexed { i, window ->
                if (i == 0) {
                    window.set(0f, 0f, width * masterWidth, height)
                } else {
                    window.set(width * masterWidth, (i - 1) * singleHeight, singleWidth, singleHeight)
                }
            }
        }

        windows.forEach { it.render(wm) }
        wm.restoreOrigin()
    }
}