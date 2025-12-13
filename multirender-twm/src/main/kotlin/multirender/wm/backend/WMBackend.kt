package multirender.wm.backend

/**
 * Window manager backend
 */
interface WMBackend {
    /**
     * Shift the coordinate system origin
     */
    fun moveOrigin(x: Float, y: Float)

    /**
     * Draw the border of a window
     */
    fun drawBorder(x: Float, y: Float, w: Float, h: Float, radius: Float)

    fun getMouseX(): Float
    fun getMouseY(): Float
}