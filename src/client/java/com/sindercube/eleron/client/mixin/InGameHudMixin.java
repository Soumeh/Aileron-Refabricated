package com.sindercube.eleron.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sindercube.eleron.client.SmokeStackHudRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@ModifyArg(
			method = "renderHotbar",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V",
				ordinal = 4
			),
			index = 2
	)
	private int moveAttackIndicatorBackground(int spriteX, @Local Arm arm) {
		return SmokeStackHudRenderer.moveAttackIndicator(spriteX, arm.getOpposite());
	}

	@ModifyArg(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIIIIIII)V"
			),
			index = 6
	)
	private int moveAttackIndicatorProgress(int spriteX, @Local Arm arm) {
		return SmokeStackHudRenderer.moveAttackIndicator(spriteX, arm.getOpposite());
	}

}
