package multirender.wm

import multirender.wm.animation.Timer
import multirender.wm.backend.BarBackend
import multirender.wm.backend.WMBackend
import multirender.wm.backend.WindowBackend
import multirender.wm.config.WMConfig
import multirender.wm.window.Window
import java.util.concurrent.CopyOnWriteArrayList

object WindowManager {
    private val windows = CopyOnWriteArrayList<Window>()
    private val floatingWindows = CopyOnWriteArrayList<Window>()
    private lateinit var backend: WMBackend
    private var bar: BarBackend? = null

    private val fadeTimer = Timer(200)
    var open = false
        set(v) {
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
        backend.globalAlpha(
            if (open) {
                fadeTimer.progress
            } else {
                1 - fadeTimer.progress
            }
        )
        bar?.render(WMConfig.barAlignment, WMConfig.barWidth)
        WMConfig.tiler.render(windows, backend.getRemainingWidth(), backend.getRemainingHeight(), backend)
        backend.restoreOrigin()
        floatingWindows.forEach { it.render(backend) }
        windows.removeIf { it.closed }
        floatingWindows.removeIf { it.closed }
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

    fun getFocused(): Window = windows.last()

    fun closeFocused() {
        if (windows.isEmpty()) return
        val window = windows.removeLast()
        floatingWindows.add(window)
        window.close()
    }
}