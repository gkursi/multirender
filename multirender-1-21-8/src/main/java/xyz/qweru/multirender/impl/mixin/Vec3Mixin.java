package xyz.qweru.multirender.impl.mixin;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.qweru.multirender.api.util.math.Vec3d;

@Mixin(Vec3.class)
public class Vec3Mixin implements Vec3d {
    @Shadow
    @Final
    public double x;

    @Shadow
    @Final
    public double y;

    @Shadow
    @Final
    public double z;

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }
}
