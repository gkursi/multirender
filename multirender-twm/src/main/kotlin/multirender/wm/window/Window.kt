package multirender.wm.window

import multirender.wm.manager.WindowManager
import multirender.wm.animation.Timer
import multirender.wm.config.Config
import multirender.wm.manager.Context

class Window(private val backend: Renderer) {
    private lateinit var config: Config
    private var opening = true
    private var closing = false
    internal var closed = false
        private set

    private var x = Property()
    private var y = Property()
    private var width = Property()
    private var height = Property()

    val sizeAnimation = Timer(400)
    val positionAnimation = Timer(400)

    fun set(context: Context, x: Float, y: Float, w: Float, h: Float) {
        config = context.config
        if (closing) return
        if (this.x.end == -1f) {
            // set to initial positions
            this.x.value = x
            this.y.value = y
        }

        if (this.x.end != x || this.y.end != y) {
            this.x.end = x
            this.y.end = y
            this.x.reset()
            this.y.reset()
            positionAnimation.length = config.moveTime
            positionAnimation.reset()
        }

        if (this.width.end != w || this.height.end != h) {
            this.width.end = w
            this.height.end = h
            this.width.reset()
            this.height.reset()
            sizeAnimation.length = config.resizeTime
            sizeAnimation.reset()
        }
    }

    fun render(ctx: Context) {
        val target = ctx.backend
        config = ctx.config

        interpolateProperties()

        val gapLeft = gapSize(Direction.BACKWARDS, x.end, width.end, target.getRemainingWidth())
        val gapRight = gapSize(Direction.FORWARDS, x.end, width.end, target.getRemainingWidth())
        val gapUp = gapSize(Direction.BACKWARDS, y.end, height.end, target.getRemainingHeight())
        val gapDown = gapSize(Direction.FORWARDS, y.end, height.end, target.getRemainingHeight())

        val x = x.value + gapLeft
        val y = y.value + gapUp

        target.moveOriginBy(x, y)
        target.setScissor(
            width.center(), height.center(),
            width.value - gapRight * 2, height.value - gapDown * 2
        )

        if (!isFocused()) {
            target.mulGlobalAlpha(0.9f)
        }

        backend.render()

        if (!isFocused()) {
            target.mulGlobalAlpha(1 / 0.9f)
        }

        target.clearScissor()
        target.moveOriginBy(-x, -y)
    }

    fun close(context: Context) {
        set(context, x.end, y.end, 0f, 0f)
        closing = true
    }

    fun contains(x: Float, y: Float): Boolean =
        x >= this.x.value && y >= this.y.value
                && x <= this.x.value + width.value
                && y <= this.y.value + height.value

    private fun interpolateProperties() {
        val size = config.resizeCurve.get(sizeAnimation.progress)
        val pos = config.moveCurve.get(positionAnimation.progress)

        x.lerp(pos)
        y.lerp(pos)

        width.lerp(size)
        height.lerp(size)

        if (opening) {
            opening = width.value / width.end < 0.99f && height.value / height.end < 0.99f
        } else if (closing) {
            closed = width.value < 0.01f && height.value < 0.01f
        }
    }

    private fun gapSize(direction: Direction, position: Float, size: Float, max: Float): Float = when (direction) {
        Direction.BACKWARDS -> if (position <= 0f) config.outerGap else config.innerGap
        Direction.FORWARDS -> if (position + size >= max) config.innerGap else config.outerGap
    }

    private inner class Property(var start: Float = 0f, var end: Float = -1f, var value: Float = 0f) {
        fun reset() {
            start = value
        }

        fun lerp(progress: Float) {
            value = start + (end - start) * progress
        }

        fun center(): Float = 0.5f *
            if (closing) {
                start - value
            } else if (opening) {
                end - value
            } else {
                0f
            }
    }

    private fun isFocused(): Boolean =
        this == WindowManager.getFocused()

    private enum class Direction {
        FORWARDS, BACKWARDS
    }
}