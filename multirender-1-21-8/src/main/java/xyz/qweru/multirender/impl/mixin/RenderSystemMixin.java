package xyz.qweru.multirender.impl.mixin;

import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.api.render.event.PostRenderEvent;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "flipFrame", at = @At("HEAD"))
    private static void flipFrame(long l, TracyFrameCapture tracyFrameCapture, CallbackInfo ci) {
        API.INSTANCE.getEvents().post(PostRenderEvent.INSTANCE);
    }
}
