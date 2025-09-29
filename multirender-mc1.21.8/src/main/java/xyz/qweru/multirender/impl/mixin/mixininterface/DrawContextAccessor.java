package xyz.qweru.multirender.impl.mixin.mixininterface;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {

    @Accessor("state")
    GuiRenderState mr$getState();

}
