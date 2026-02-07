package xyz.qweru.test1218

import xyz.qweru.multirender.nvg.constant.LineJoin
import xyz.qweru.multirender.nvg.NanoRenderEvent
import net.fabricmc.api.ModInitializer
import xyz.qweru.geo.core.event.Handler
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.util.color.Color
import xyz.qweru.multirender.api.util.math.Vec2f

class TestMod : ModInitializer {
    val color = Color(167, 70, 188)

    override fun onInitialize() {
        API.events.subscribe(this)
    }

    @Handler
    fun onRender2d(event: NanoRenderEvent) {
        event.context.apply {
            shape {
                path {
                    rectangle(Vec2f.absolute(20f, 20f), Vec2f.absolute(200f, 200f))
                    ellipse(Vec2f.absolute(50f, 50f), Vec2f.absolute(10f, 10f))
                }
                
                fill {
                    paint = linearGradient(Vec2f.TOP_LEFT, Vec2f.BOTTOM_RIGHT, Color.red, Color.green)
                }

                stroke {
                    paint = solid(Color.blue)
                    width = 7f
                    join = LineJoin.ROUND
                    miterLimit = 20f
                }
            }
        }
    }
}
