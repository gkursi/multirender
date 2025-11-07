package xyz.qweru.multirender.impl.mixin.mixininterface;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MouseHandler.class)
public interface MouseInvoker {
    @Invoker("onPress")
    void invokeOnMouseButton(long window, int button, int action, int mods);
    @Invoker("onMove")
    void invokeOnCursorPos(long window, double x, double y);
    @Accessor("accumulatedDX")
    void setDeltaX(double deltaX);
    @Accessor("accumulatedDY")
    void setDeltaY(double deltaY);
}
