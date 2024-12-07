package com.sindercube.eleron.client;

import com.sindercube.eleron.Eleron;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class SmokeStackHudRenderer {

    public static void renderSmokeStackBar(DrawContext context, RenderTickCounter ignoredT) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (player.isCreative()) return;

        int screenHeight = context.getScaledWindowHeight();
        int screenWidth = context.getScaledWindowWidth();

        int screenX = (screenWidth / 2);
        if (player.getMainArm() == Arm.LEFT) screenX -= 102;
        else screenX += 92;

        int screenY = screenHeight - 10;
        SmokeStackIcon icon = SmokeStackIcon.DEFAULT;

        for (int i = 0; i < player.getMaxSmokestackCharges(); i++) {
            int spriteY = screenY - (i * 9);
			context.drawGuiTexture(RenderLayer::getGuiTextured, icon.containerTexture, screenX, spriteY, 9, 9);
        }

        for (int i = 0; i < player.getSmokestackCharges(); i++) {
            int spriteY = screenY - (i * 9);
			context.drawGuiTexture(RenderLayer::getGuiTextured, icon.fullTexture, screenX, spriteY, 9, 9);
        }
    }

    public static int moveAttackIndicator(int spriteX, Arm arm) {
        return switch (arm) {
            case LEFT -> spriteX - 9;
            case RIGHT -> spriteX + 5;
        };
    }


	public record SmokeStackIcon (
		Identifier containerTexture,
		Identifier fullTexture
	) {

		public static final SmokeStackIcon DEFAULT = new SmokeStackIcon(
			Eleron.of("hud/smokestack/default/container"),
			Eleron.of("hud/smokestack/default/full")
		);

	}

}
