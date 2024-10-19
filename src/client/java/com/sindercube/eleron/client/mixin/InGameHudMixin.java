package com.sindercube.eleron.client.mixin;

import com.sindercube.eleron.client.handler.SmokeStackGuiRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@ModifyArg(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"
			),
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
							ordinal = 1
					)
			),
			index = 1
	)
	private int moveAttackIndicatorBackground(int spriteX) {
		return SmokeStackGuiRenderer.moveAttackIndicator(spriteX);
	}

	@ModifyArg(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"
			),
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
							ordinal = 1
					)
			),
			index = 5
	)
	private int moveAttackIndicator(int spriteX) {
		return SmokeStackGuiRenderer.moveAttackIndicator(spriteX);
	}

}
