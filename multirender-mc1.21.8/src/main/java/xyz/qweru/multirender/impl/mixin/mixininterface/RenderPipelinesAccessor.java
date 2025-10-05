package xyz.qweru.multirender.impl.mixin.mixininterface;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Accessor("POSITION_COLOR_SNIPPET")
    static RenderPipeline.Snippet mr_getPositionColorSnippet() {
        throw new AssertionError();
    }

    @Accessor("RENDERTYPE_LINES_SNIPPET")
    static RenderPipeline.Snippet mr_getLineSnippet() {
        throw new AssertionError();
    }

    @Accessor("POSITION_TEX_COLOR_SNIPPET")
    static RenderPipeline.Snippet mr_getPositionTexColorSnippet() {
        throw new AssertionError();
    }

    @Invoker("register")
    static RenderPipeline mr_register(RenderPipeline pipeline) {
        throw new AssertionError();
    }
}
