package xyz.qweru.multirender.api.render.dim2

import xyz.qweru.multirender.api.render.texture.Texture
import xyz.qweru.multirender.api.util.color.Color

interface Context2d {
    fun begin()
    fun end()
    fun isBuilding(): Boolean

    fun quad(
        x: Float, y: Float, w: Float, h: Float,
        color1: Color, color2: Color = color1, color3: Color = color1, color4: Color = color1,
        rad1: Int, rad2: Int = rad1, rad3: Int = rad1, rad4: Int = rad1
    )

    fun line(
        x: Float, y: Float, x1: Float, y1: Float,
        color1: Color, color2: Color = color1
    )

    /**
     * Set the active glScissor. Only applies to elements rendered by this class.
     */
    fun setScissor(x: Float, y: Float, w: Float, h: Float)
    fun clearScissor()

    /**
     * Set the active texture. Only applies to elements rendered by this class.
     */
    fun setTexture(texture: Texture)
    fun clearTexture()

    fun lineWidth(w: Float)
}