package multirender.wm.backend

/**
 * Window manager backend
 */
interface WMBackend {
    /**
     * Shift the coordinate system origin
     */
    fun moveOriginBy(x: Float, y: Float)

    /**
     * Restore the coordinate system origin
     */
    fun restoreOrigin()

    fun getMouseX(): Float
    fun getMouseY(): Float

    /**
     * @return width accounting for origin not being at 0,0
     */
    fun getRemainingWidth(): Float
    /**
     * @return height accounting for origin not being at 0,0
     */
    fun getRemainingHeight(): Float

    fun setScissor(x: Float, y: Float, w: Float, h: Float)
    fun clearScissor()

    fun globalAlpha(alpha: Float)
}