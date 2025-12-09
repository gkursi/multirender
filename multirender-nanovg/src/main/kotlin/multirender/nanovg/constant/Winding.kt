package multirender.nanovg.constant

import org.lwjgl.nanovg.NanoVG

enum class Winding(val id: Int) {
    HOLE(NanoVG.NVG_CW), SOLID(NanoVG.NVG_CCW)
}