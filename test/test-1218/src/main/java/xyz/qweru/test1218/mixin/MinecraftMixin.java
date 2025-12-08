package xyz.qweru.test1218.mixin;

import com.mojang.blaze3d.platform.Window;
import multirender.nanovg.NanoState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.api.render.event.WindowCreateEvent;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private static void initSkija(GameConfig gameConfig, CallbackInfo ci) {
        NanoState.INSTANCE.init();
        Window window = Minecraft.getInstance().getWindow();
        WindowCreateEvent.INSTANCE.setWidth(window.getWidth());
        WindowCreateEvent.INSTANCE.setHeight(window.getHeight());
        API.INSTANCE.getEvents().post(WindowCreateEvent.INSTANCE);
    }
}
