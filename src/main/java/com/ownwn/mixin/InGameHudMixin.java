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
		/* use a Mixin to inject in to the hotbar renderer, so that any text we want to draw can be drawn at the same time.
		This is good because when rendering the hotbar, the game will already have done the usual null checks, e.g. checking the player isn't null*/
		DrawHelper.doDraw(matrices);
	}
}