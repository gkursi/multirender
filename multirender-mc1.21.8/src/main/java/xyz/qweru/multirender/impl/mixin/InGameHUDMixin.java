package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.api.render.texture.Texture;

import java.awt.*;

@Mixin(InGameHud.class)
public class InGameHUDMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
//        API.context2d.quad(10, 10, 100, 100, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, 0);
        API.context2d.lineWidth(1f);
        API.context2d.line(10, 10, 110, 110, Color.RED, Color.GREEN);
    }

}
