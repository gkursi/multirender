package multirender.wm.manager

import multirender.wm.animation.Timer
import multirender.wm.bar.Bar
import multirender.wm.config.Config
import multirender.wm.window.Renderer
import multirender.wm.config.DefaultConfig
import multirender.wm.notification.NotificationManager
import multirender.wm.util.reverseFirstOrNull
import multirender.wm.window.Window
import xyz.qweru.multirender.api.API
import java.util.concurrent.CopyOnWriteArrayList

object WindowManager {
    private val windows = CopyOnWriteArrayList<Window>()
    private val floatingWindows = CopyOnWriteArrayList<Window>()

    private lateinit var backend: Backend
    private var bar: Bar? = null

    private var focused: Window? = null

    private val fadeTimer = Timer(200)
    private val opacity: Float
        get() =
            if (open) {
                fadeTimer.progress
            } else {
                1 - fadeTimer.progress
            }

    var config: Config = DefaultConfig
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

    fun setBackend(backend: Backend) {
        this.backend = backend
    }

    fun setBar(bar: Bar) {
        this.bar = bar
    }

    fun render() {
        val mx = API.mouseHandler.x
        val my = API.mouseHandler.y
        val context = Context(
            backend, config,
            mouseX = mx,
            mouseY = my
        )

        backend.setGlobalAlpha(opacity)

        // update windows
        windows.removeIf { it.closed }
        floatingWindows.removeIf { it.closed }

        // update focused window
        focused = floatingWindows.reverseFirstOrNull { it.contains(mx, my) }
                    ?: windows.firstOrNull { it.contains(mx, my) }

        // update notifications
        NotificationManager.collect()

        // draw bar
        bar?.render(context)

        // draw windows
        config.tiler.tile(
            windows = windows, context = context,
            width = backend.getRemainingWidth(),
            height = backend.getRemainingHeight()
        )

        windows.render(context)
        floatingWindows.render(context)

        // draw notifications
        NotificationManager.render(context)
        backend.restoreOrigin()
    }

    fun addWindow(backend: Renderer) {
        windows.add(Window(backend))
    }

    fun addFloatingWindow(backend: Renderer, x: Float, y: Float, w: Float, h: Float) {
        if (DefaultConfig.forceTile) {
            addWindow(backend)
        } else {
            floatingWindows.add(Window(backend).apply {
                set(Context(this@WindowManager.backend, config), x, y, w, h)
            })
        }
    }

    fun getFocused(): Window? = focused

    fun closeFocused() {
        if (windows.isEmpty() && floatingWindows.isEmpty()) return
        val window = focused ?: return
        windows.remove(window)
        floatingWindows.remove(window)
        floatingWindows.addFirst(window)
        window.close(Context(backend, config))
    }

    fun Collection<Window>.render(context: Context) = forEach { it.render(context) }
}