package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.impl.render.dim2.MinecraftContext2d;

@Mixin(GuiGraphics.class)
public class DrawContextMixin {

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lorg/joml/Matrix3x2fStack;Lnet/minecraft/client/gui/render/state/GuiRenderState;)V", at = @At("TAIL"))
    private static void onCtxCreate(Minecraft minecraft, Matrix3x2fStack matrices, GuiRenderState guiRenderState, CallbackInfo ci) {
        ((MinecraftContext2d) API.context2d).setMatrices(matrices);
    }

}
