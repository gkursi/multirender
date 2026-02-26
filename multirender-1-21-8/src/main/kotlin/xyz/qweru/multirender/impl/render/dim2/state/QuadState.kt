package xyz.qweru.multirender.impl.render.dim2.state

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.GuiElementRenderState
import org.joml.Matrix3x2f
import xyz.qweru.multirender.impl.util.screenRect

class QuadState(val pipeline: RenderPipeline, val texture: TextureSetup = TextureSetup.noTexture(), val pose: Matrix3x2f,
                val x1: Float, val y1: Float, val x2: Float, val y2: Float,
                val bounds: ScreenRectangle = screenRect(x1, y1, x2, y2).transformMaxBounds(pose), val scissor: ScreenRectangle? = null,
                val color1: Int, val color2: Int = color1, val color3: Int = color1, val color4: Int = color1) : GuiElementRenderState {

    override fun buildVertices(b: VertexConsumer, depth: Float) {
        b.addVertexWith2DPose(pose, x1, y1, depth).setColor(color1)
        setUV(b, 0f, 1f)
        b.addVertexWith2DPose(pose, x2, y1, depth).setColor(color2)
        setUV(b, 1f, 1f)
        b.addVertexWith2DPose(pose, x2, y2, depth).setColor(color3)
        setUV(b, 1f, 0f)
        b.addVertexWith2DPose(pose, x1, y2, depth).setColor(color4)
        setUV(b, 0f, 0f)
    }

    private fun setUV(b: VertexConsumer, u: Float, v: Float) {
        if (texture != TextureSetup.noTexture()) b.setUv(u, v).setLight(15)
    }

    override fun pipeline(): RenderPipeline = pipeline
    override fun textureSetup(): TextureSetup = texture
    override fun scissorArea(): ScreenRectangle? = scissor
    override fun bounds(): ScreenRectangle = bounds
}