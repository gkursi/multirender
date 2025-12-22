package multirender.wm

import multirender.wm.animation.Timer
import multirender.wm.backend.BarBackend
import multirender.wm.backend.WMBackend
import multirender.wm.backend.WindowBackend
import multirender.wm.config.WMConfig
import multirender.wm.util.lastOrNull
import multirender.wm.util.reverseFirstOrNull
import multirender.wm.window.Window
import xyz.qweru.multirender.api.API
import java.util.concurrent.CopyOnWriteArrayList

object WindowManager {
    private val windows = CopyOnWriteArrayList<Window>()
    private val floatingWindows = CopyOnWriteArrayList<Window>()

    private lateinit var backend: WMBackend
    private var bar: BarBackend? = null

    private var focused: Window? = null

    private val fadeTimer = Timer(200)
    private val opacity: Float
        get() =
            if (open) {
                fadeTimer.progress
            } else {
                1 - fadeTimer.progress
            }
    var open = false
        set(v) {
            if (v != field) {
                if (v) {
                    backend.open()
                } else {
                    backend.close()
                }
            }

            field = v
            fadeTimer.reset()
        }

    fun setBackend(backend: WMBackend) {
        this.backend = backend
    }

    fun setBar(bar: BarBackend) {
        this.bar = bar
    }

    fun render() {
        backend.setGlobalAlpha(opacity)

        // update windows
        windows.removeIf { it.closed }
        floatingWindows.removeIf { it.closed }

        // update focused window
        val mx = API.mouseHandler.x
        val my = API.mouseHandler.y

        focused = floatingWindows.reverseFirstOrNull { it.contains(mx, my) }
                    ?: windows.firstOrNull { it.contains(mx, my) }

        // draw bar
        bar?.render(WMConfig.barAlignment, WMConfig.barWidth)

        // draw windows
        WMConfig.tiler.render(
            windows = windows, wm = backend,
            width = backend.getRemainingWidth(),
            height = backend.getRemainingHeight()
        )

        floatingWindows.forEach { it.render(backend) }
    }

    fun addWindow(backend: WindowBackend) {
        windows.add(Window(backend))
    }

    fun addFloatingWindow(backend: WindowBackend, x: Float, y: Float, w: Float, h: Float) {
        if (WMConfig.forceTile) {
            addWindow(backend)
        } else {
            floatingWindows.add(Window(backend).apply {
                set(x, y, w, h)
            })
        }
    }

    fun getFocused(): Window? = focused ?: windows.lastOrNull()

    fun closeFocused() {
        if (windows.isEmpty() && floatingWindows.isEmpty()) return
        val window = focused ?: return
        windows.remove(window)
        floatingWindows.remove(window)
        floatingWindows.addFirst(window)
        window.close()
    }
}