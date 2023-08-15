package com.ownwn.mixin;

import com.ownwn.item.DrawHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(at = @At("HEAD"), method = "renderHotbar")
	private void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		DrawHelper.doDraw(matrices);
	}
}