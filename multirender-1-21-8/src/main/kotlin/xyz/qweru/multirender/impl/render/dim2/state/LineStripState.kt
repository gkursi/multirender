package xyz.qweru.multirender.impl.render.dim2.state

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.GuiElementRenderState
import org.joml.Matrix3x2f

class LineStripState(val pipeline: RenderPipeline, val texture: TextureSetup = TextureSetup.noTexture(), val pose: Matrix3x2f,
                     val points: Array<Point>, val bounds: ScreenRectangle, val scissor: ScreenRectangle? = null) : GuiElementRenderState {

    override fun buildVertices(b: VertexConsumer, depth: Float) {
        points.forEach { point ->
            b.addVertexWith2DPose(pose, point.x, point.y, depth).setColor(point.color)
            setUV(b, point.u, point.v)
        }
    }

    private fun setUV(b: VertexConsumer, u: Float, v: Float) {
        if (texture != TextureSetup.noTexture()) b.setUv(u, v).setLight(15)
    }

    override fun pipeline(): RenderPipeline = pipeline
    override fun textureSetup(): TextureSetup = texture
    override fun scissorArea(): ScreenRectangle? = scissor
    override fun bounds(): ScreenRectangle = bounds

    data class Point(val x: Float, val y: Float, val color: Int, val u: Float, val v: Float)
}