package xyz.qweru.multirender.impl.render.dim2

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.GuiRenderState
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fStack
import xyz.qweru.multirender.api.API
import xyz.qweru.multirender.api.render.dim2.Context2d
import xyz.qweru.multirender.api.render.texture.Texture
import xyz.qweru.multirender.impl.render.dim2.pipeline.LinePipeline
import xyz.qweru.multirender.impl.render.dim2.pipeline.QuadPipeline
import xyz.qweru.multirender.impl.render.dim2.state.LineState
import xyz.qweru.multirender.impl.render.dim2.state.QuadState
import xyz.qweru.multirender.impl.render.texture.MinecraftTexture
import xyz.qweru.multirender.impl.util.screenRect
import java.awt.Color

/**
 * Incomplete, currently only supports quads with radius 0
 */
class MinecraftContext2d : Context2d {

    private var building = false
    private lateinit var renderState: GuiRenderState
    private lateinit var matrices: Matrix3x2fStack
    private var texture: Texture? = null
    private var scissor: ScreenRectangle? = null

    fun setTarget(state: GuiRenderState) {
        renderState = state
    }

    fun setMatrices(matrices: Matrix3x2fStack) {
        this.matrices = matrices
    }

    override fun begin() {
        if (!API.base.isOnRenderThread()) throw IllegalThreadStateException("Call to begin() off-thread")
        if (building) throw IllegalStateException("begin() called twice")
        building = true
        renderState.blurBeforeThisStratum()
        renderState.nextStratum()
    }

    override fun end() {
        renderState.nextStratum()
        check("end")
        building = false
    }

    override fun isBuilding(): Boolean = building

    private fun check(source: String) {
        if (!API.base.isOnRenderThread()) throw IllegalThreadStateException("Call to $source() off-thread")
        if (!building) throw IllegalStateException("Call to $source() without calling begin()")
    }

    override fun quad(
        x: Float, y: Float, w: Float, h: Float,
        color1: Color, color2: Color, color3: Color, color4: Color,
        rad1: Int, rad2: Int, rad3: Int, rad4: Int
    ) {
        check("quad")
        if (rad1 > 0f || rad2 > 0f || rad3 > 0f || rad4 > 0f)
            roundedQuadInternal(x, y, w, h, color1.rgb, color2.rgb, color3.rgb, color4.rgb, rad1, rad2, rad3, rad4)
        else
            quadInternal(x, y, w, h, color1.rgb, color2.rgb, color3.rgb, color4.rgb)
    }

    override fun line(
        x: Float, y: Float, x1: Float, y1: Float,
        color1: Color, color2: Color
    ) {
        check("line")
        val tex = getTextureSetup()
        renderState.submitGuiElement(LineState(
//            if (tex == TextureSetup.empty()) LinePipeline.PLAIN else LinePipeline.WITH_TEXTURE,
            LinePipeline.LINES_NODEPTH_PIPELINE,
            tex, Matrix3x2f(matrices), x, y, x1, y1,
            color1 = color1.rgb, color2 = color2.rgb))
    }

    override fun setScissor(x: Float, y: Float, w: Float, h: Float) {
        check("setScissor")
        this.scissor = screenRect(x, y, x + w, y + h)
    }

    override fun clearScissor() {
        check("clearScissor")
        this.scissor = null
    }

    override fun setTexture(texture: Texture) {
        check("setTexture")
        this.texture = texture
    }

    override fun clearTexture() {
        check("clearTexture")
        this.texture = null
    }

    override fun lineWidth(w: Float) {
        check("lineWidth")
        RenderSystem.lineWidth(w)
    }

    private fun quadInternal(
        x: Float, y: Float, w: Float, h: Float,
        color1: Int, color2: Int, color3: Int, color4: Int
    ) {
        val tex = getTextureSetup()
        renderState.submitGuiElement(QuadState(
            if (tex == TextureSetup.noTexture()) QuadPipeline.PLAIN else QuadPipeline.WITH_TEXTURE, tex,
            Matrix3x2f(matrices), x, y, x + w, y + h, scissor = scissor,
            color1 = color1, color2 = color2, color3 = color3, color4 = color4
        ))
    }

    private fun roundedQuadInternal(
        x: Float, y: Float, w: Float, h: Float,
        color1: Int, color2: Int, color3: Int, color4: Int,
        rad1: Int, rad2: Int, rad3: Int, rad4: Int
    ) {

    }

    private fun getTextureSetup(): TextureSetup
        = if (texture == null) TextureSetup.noTexture() else (texture as MinecraftTexture).getTextureSetup()

}