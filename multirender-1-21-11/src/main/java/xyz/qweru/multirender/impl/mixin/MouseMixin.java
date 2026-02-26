package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.impl.input.MinecraftMouse;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "onMove", at = @At("HEAD"))
    private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        ((MinecraftMouse) API.mouseHandler).onMove(window, x, y);
    }
}
