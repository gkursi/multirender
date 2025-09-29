package xyz.qweru.multirender.impl.mixin.mixininterface;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderLayer.class)
public interface RenderLayerInvoker {

    @Invoker("of")
    static RenderLayer.MultiPhase of(String name, int size, boolean hasCrumbling, boolean translucent, RenderPipeline pipeline, RenderLayer.MultiPhaseParameters params) {
        throw new AssertionError();
    }

}
