package xyz.qweru.multirender.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.impl.render.dim2.MinecraftContext2d;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Inject(method = "<init>(Lnet/minecraft/client/MinecraftClient;Lorg/joml/Matrix3x2fStack;Lnet/minecraft/client/gui/render/state/GuiRenderState;)V", at = @At("TAIL"))
    private static void onCtxCreate(MinecraftClient client, Matrix3x2fStack matrices, GuiRenderState state, CallbackInfo ci) {
        ((MinecraftContext2d) API.context2d).setMatrices(matrices);
    }

}
