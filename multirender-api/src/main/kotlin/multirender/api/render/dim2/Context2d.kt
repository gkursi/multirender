package xyz.qweru.multirender.api.render.dim2

import xyz.qweru.multirender.api.render.texture.Texture
import java.awt.Color

abstract class Context2d {
    abstract fun begin()
    abstract fun end()
    abstract fun isBuilding(): Boolean

    abstract fun quad(x: Float, y: Float, w: Float, h: Float,
                      color1: Color, color2: Color = color1, color3: Color = color1, color4: Color = color1,
                      rad1: Int, rad2: Int = rad1, rad3: Int = rad1, rad4: Int = rad1)

    abstract fun line(x: Float, y: Float, x1: Float, y1: Float,
                      color1: Color, color2: Color = color1)

    /**
     * Set the active glScissor. Only applies to elements rendered by this class.
     */
    abstract fun setScissor(x: Float, y: Float, w: Float, h: Float)
    abstract fun clearScissor()

    /**
     * Set the active texture. Only applies to elements rendered by this class.
     */
    abstract fun setTexture(texture: Texture)
    abstract fun clearTexture()

    abstract fun lineWidth(w: Float)

    /* For Java usage */

    fun quad(x: Float, y: Float, w: Float, h: Float, color: Color, rad: Int) {
        quad(x, y, w, h, color1 = color, rad1 = rad)
    }

    fun quad(x: Float, y: Float, w: Float, h: Float, color: Color, rad1: Int, rad2: Int, rad3: Int, rad4: Int) {
        quad(x, y, w, h, color1 = color, rad1 = rad1, rad2 = rad2, rad3 = rad3, rad4 = rad4)
    }

    fun quad(x: Float, y: Float, w: Float, h: Float, color1: Color, color2: Color, color3: Color, color4: Color, rad: Int) {
        quad(x, y, w, h, color1 = color1, color2 = color2, color3 = color3, color4 = color4, rad1 = rad)
    }

}