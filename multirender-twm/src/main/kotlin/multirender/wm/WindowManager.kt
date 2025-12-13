package multirender.wm

import multirender.wm.backend.WMBackend
import multirender.wm.backend.WindowBackend
import multirender.wm.tiling.HorizontalTiler
import multirender.wm.tiling.TilingStrategy
import multirender.wm.window.Window
import xyz.qweru.multirender.api.API
import java.util.concurrent.ConcurrentHashMap

object WindowManager {
    private val screen = API.base.getWindow()
    private val tiler: TilingStrategy = HorizontalTiler()
    private val windows: MutableSet<Window> = ConcurrentHashMap.newKeySet()
    private lateinit var backend: WMBackend

    fun setBackend(backend: WMBackend) {
        this.backend = backend
    }

    /**
     * Add a new window
     * @param x Approx. window x position, defaults to 0.5 * screenWidth
     * @param y Approx. window y position, defaults to 0.5 * screenHeight
     */
    fun addWindow(backend: WindowBackend, x: Float = screen.getWidth() * .5f, y: Float = screen.getHeight() * .5f) {
        windows.add(Window(backend))
    }
}