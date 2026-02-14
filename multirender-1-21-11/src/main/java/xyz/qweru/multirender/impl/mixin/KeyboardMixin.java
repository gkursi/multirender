package xyz.qweru.multirender.impl.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qweru.multirender.api.API;
import xyz.qweru.multirender.impl.input.MinecraftKeyboard;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onKey(long handle, int action, KeyEvent keyEvent, CallbackInfo ci) {
        if (action == 0) {
            ((MinecraftKeyboard) API.keyboardHandler).onKey(handle, keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers());
        }
    }
}
