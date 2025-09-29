package xyz.qweru.multirender.impl.render.dim2.state

import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.gui.ScreenRect
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.texture.TextureSetup
import org.joml.Matrix3x2f

class LineStripState(val pipeline: RenderPipeline, val texture: TextureSetup = TextureSetup.empty(), val pose: Matrix3x2f,
                     val points: Array<Point>, val bounds: ScreenRect, val scissor: ScreenRect? = null) : SimpleGuiElementRenderState {

    override fun setupVertices(b: VertexConsumer, depth: Float) {
        points.forEach { point ->
            b.vertex(pose, point.x, point.y, depth).color(point.color)
            setUV(b, point.u, point.v)
        }
    }

    private fun setUV(b: VertexConsumer, u: Float, v: Float) {
        if (texture != TextureSetup.empty()) b.texture(u, v).light(15, 15)
    }

    override fun pipeline(): RenderPipeline = pipeline
    override fun textureSetup(): TextureSetup = texture
    override fun scissorArea(): ScreenRect? = scissor
    override fun bounds(): ScreenRect = bounds

    data class Point(val x: Float, val y: Float, val color: Int, val u: Float, val v: Float)
}