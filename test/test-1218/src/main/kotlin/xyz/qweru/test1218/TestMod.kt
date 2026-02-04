package xyz.qweru.test1218

import multirender.nanovg.constant.Blend
import multirender.nanovg.constant.LineJoin
import multirender.nanovg.event.NanoRenderEvent
import multirender.nanovg.constant.Winding
import multirender.nanovg.util.color.times
import multirender.nanovg.util.math.Vec2f
import net.fabricmc.api.ModInitializer
import net.minecraft.world.phys.Vec2
import xyz.qweru.geo.core.event.Handler
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.util.color.Color

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
