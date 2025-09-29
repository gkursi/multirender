package xyz.qweru.multirender.impl.render.dim2.pipeline

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.render.VertexFormats
import xyz.qweru.multirender.impl.mixin.mixininterface.RenderPipelinesAccessor

object LinePipeline {
    val PLAIN: RenderPipeline = RenderPipelinesAccessor.mr_register(RenderPipeline.builder(
        RenderPipelinesAccessor.mr_getPositionColorSnippet())
        .withLocation("multirender/line_no_depth_test")
        .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINES)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(false)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val STRIP: RenderPipeline = RenderPipelinesAccessor.mr_register(RenderPipeline.builder(
        RenderPipelinesAccessor.mr_getPositionColorSnippet())
        .withLocation("multirender/line_no_depth_test")
        .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINE_STRIP)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(false)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val WITH_TEXTURE: RenderPipeline = RenderPipelinesAccessor.mr_register(RenderPipeline.builder(
        RenderPipelinesAccessor.mr_getPositionTexColorSnippet())
        .withLocation("multirender/textured_line_no_depth_test")
        .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.LINES)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
        .withDepthWrite(false)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())
}