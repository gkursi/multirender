package xyz.qweru.multirender.impl.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
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
import xyz.qweru.multirender.impl.render.dim2.MinecraftContext2d;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private GuiRenderState guiState;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;clear()V"))
    private void onRender2zd(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
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
