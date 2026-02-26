package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.api.util.color.Color;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        API.context2d.quad(10, 10, 100, 100, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, 0);

    }

}
