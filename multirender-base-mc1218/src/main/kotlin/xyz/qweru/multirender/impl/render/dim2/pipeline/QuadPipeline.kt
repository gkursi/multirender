package xyz.qweru.multirender.impl.render.dim2.pipeline

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderPipelines

object QuadPipeline {
    val PLAIN: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(
        RenderPipelines.DEBUG_FILLED_SNIPPET)
        .withLocation("multirender/quad_no_depth_test")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(false)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val WITH_TEXTURE: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(
        RenderPipelines.GUI_SNIPPET)
        .withLocation("multirender/textured_quad_no_depth_test")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(false)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())
}