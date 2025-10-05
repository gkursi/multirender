package xyz.qweru.multirender.impl.mixin.mixininterface;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseInvoker {
    @Invoker("onMouseButton")
    void invokeOnMouseButton(long window, int button, int action, int mods);
    @Invoker("onCursorPos")
    void invokeOnCursorPos(long window, double x, double y);
    @Accessor("cursorDeltaX")
    void setDeltaX(double deltaX);
    @Accessor("cursorDeltaY")
    void setDeltaY(double deltaY);
}
