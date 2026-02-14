package xyz.qweru.multirender.impl.mixin.mixininterface;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MouseHandler.class)
public interface MouseInvoker {
    @Invoker("onMove")
    void invokeOnCursorPos(long window, double x, double y);
    @Accessor("accumulatedDX")
    void setDeltaX(double deltaX);
    @Accessor("accumulatedDY")
    void setDeltaY(double deltaY);
    @Accessor("xpos")
    double getXPos();
    @Accessor("ypos")
    double getYPos();

    @Invoker("onButton")
    void invokeOnButton(long l, MouseButtonInfo mouseButtonInfo, int i);
}
