package xyz.qweru.multirender.impl.mixin.mixininterface;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderLayer.MultiPhaseParameters.Builder.class)
public interface RenderLayer$MultiPhaseParameters$BuilderMixin {
    @Invoker("layering")
    RenderLayer.MultiPhaseParameters.Builder mr_layering(RenderPhase.Layering layering);

    @Invoker("target")
    RenderLayer.MultiPhaseParameters.Builder mr_target(RenderPhase.Target target);

    @Invoker("build")
    RenderLayer.MultiPhaseParameters mr_build(boolean affectsOutline);
}
