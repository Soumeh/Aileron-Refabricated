package com.sindercube.eleron.client.handler;

import com.sindercube.eleron.Eleron;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class SmokeStackGuiRenderer {

    public record SmokeStackIcon (
            Identifier containerTexture,
            Identifier fullTexture
    ) {

        public static final SmokeStackIcon DEFAULT = new SmokeStackIcon(
            Eleron.of("hud/smokestack/container"),
            Eleron.of("hud/smokestack/full")
        );

        public void drawContainer(DrawContext context, int x, int y) {
            context.drawGuiTexture(containerTexture, x, y, 9, 9);
        }

        public void drawFull(DrawContext context, int x, int y) {
            context.drawGuiTexture(fullTexture, x, y, 9, 9);
        }

    }

    public static void renderSmokeStackBar(DrawContext context, RenderTickCounter t) {
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

        for (int i = 0; i < player.getMaxSmokeStacks(); i++) {
            int spriteY = screenY - (i * 9);
            icon.drawContainer(context, screenX, spriteY);
        }

        for (int i = 0; i < player.getSmokeStacks(); i++) {
            int spriteY = screenY - (i * 9);
            icon.drawFull(context, screenX, spriteY);
        }
    }

    public static int moveAttackIndicator(int spriteX) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return spriteX;

        return switch (player.getMainArm()) {
            case LEFT -> spriteX - 9;
            case RIGHT -> spriteX + 5;
        };
    }

}
