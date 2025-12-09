package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.impl.input.MinecraftKeyboard;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == 0) ((MinecraftKeyboard) API.keyboardHandler).onKey(window, key, scancode, modifiers);
    }
}
