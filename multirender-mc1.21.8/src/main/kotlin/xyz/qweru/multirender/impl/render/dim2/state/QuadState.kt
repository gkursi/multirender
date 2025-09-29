package xyz.qweru.multirender.impl.render.dim2.state

import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.gui.ScreenRect
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.texture.TextureSetup
import org.joml.Matrix3x2f
import xyz.qweru.multirender.impl.util.MathUtils

class QuadState(val pipeline: RenderPipeline, val texture: TextureSetup = TextureSetup.empty(), val pose: Matrix3x2f,
                val x1: Float, val y1: Float, val x2: Float, val y2: Float,
                val bounds: ScreenRect = MathUtils.screenRect(x1, y1, x2, y2).transformEachVertex(pose), val scissor: ScreenRect? = null,
                val color1: Int, val color2: Int = color1, val color3: Int = color1, val color4: Int = color1) : SimpleGuiElementRenderState {

    override fun setupVertices(b: VertexConsumer, depth: Float) {
        b.vertex(pose, x1, y1, depth).color(color1)
        setUV(b, 0f, 1f)
        b.vertex(pose, x2, y1, depth).color(color2)
        setUV(b, 1f, 1f)
        b.vertex(pose, x2, y2, depth).color(color3)
        setUV(b, 1f, 0f)
        b.vertex(pose, x1, y2, depth).color(color4)
        setUV(b, 0f, 0f)
    }

    private fun setUV(b: VertexConsumer, u: Float, v: Float) {
        if (texture != TextureSetup.empty()) b.texture(u, v).light(15, 15)
    }

    override fun pipeline(): RenderPipeline = pipeline
    override fun textureSetup(): TextureSetup = texture
    override fun scissorArea(): ScreenRect? = scissor
    override fun bounds(): ScreenRect = bounds
}