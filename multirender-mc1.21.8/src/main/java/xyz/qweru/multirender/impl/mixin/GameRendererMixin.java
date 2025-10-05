package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.impl.Multirender;
import xyz.qweru.multirender.impl.render.dim2.MinecraftContext2d;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private GuiRenderState guiState;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;clear()V"))
    private void onRender2zd(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        // calculate deltatime
        long frame = System.currentTimeMillis();
        Multirender multirender = (Multirender) API.base;
        multirender.setDt((frame - multirender.getLf()) / 1000f);
        multirender.setLf(frame);
        // set up 2d context
        MinecraftContext2d ctx = ((MinecraftContext2d) API.context2d);
        ctx.setTarget(guiState);
        ctx.begin();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"))
    private void postRender2d(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        API.context2d.end();
    }

}
