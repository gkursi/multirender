package xyz.qweru.multirender.impl.mixin;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.api.render.event.WindowSizeChangeEvent;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow @Final private long window;
    @Shadow private int width;
    @Shadow private int framebufferWidth;

    @Inject(method = "onFramebufferResize", at = @At("TAIL"))
    private void resize(long l, int i, int j, CallbackInfo ci) {
        if (l != window) return;
        WindowSizeChangeEvent.INSTANCE.setWidth(i);
        WindowSizeChangeEvent.INSTANCE.setHeight(j);
        WindowSizeChangeEvent.INSTANCE.setRatio((float) width / framebufferWidth);
        API.INSTANCE.getEvents().post(WindowSizeChangeEvent.INSTANCE);
    }
}
