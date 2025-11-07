package xyz.qweru.multirender.impl.render.dim2.pipeline

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.ResourceLocation


object LinePipeline {

    val LINES_NODEPTH_PIPELINE: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(ResourceLocation.tryBuild("renderer", "pipeline/lines_nodepth")!!)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withDepthWrite(true)
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES)
            .build()
    )

    val PLAIN: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(
        RenderPipelines.LINES_SNIPPET)
        .withLocation("multirender/line_no_depth_test")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(true)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val STRIP: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(
        RenderPipelines.LINES_SNIPPET)
        .withLocation("multirender/line_no_depth_test")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINE_STRIP)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(true)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val WITH_TEXTURE: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(
        RenderPipelines.GUI_SNIPPET)
        .withLocation("multirender/textured_line_no_depth_test")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.LINES)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(true)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())
}