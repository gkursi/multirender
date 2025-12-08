package xyz.qweru.multirender.impl.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.api.render.event.WindowCreateEvent;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final private Window window;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void postInit(GameConfig gameConfig, CallbackInfo ci) {
        WindowCreateEvent.INSTANCE.setWidth(this.window.getWidth());
        WindowCreateEvent.INSTANCE.setHeight(this.window.getHeight());
        WindowCreateEvent.INSTANCE.setRatio((float) this.window.getScreenWidth() / this.window.getWidth());
        API.INSTANCE.getEvents().post(WindowCreateEvent.INSTANCE);
    }
}
