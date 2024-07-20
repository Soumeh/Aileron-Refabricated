package com.sindercube.aileron.client.handler;

import com.sindercube.aileron.Aileron;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class SmokeStackRenderer {

    public record SmokeStackIcon (
            Identifier containerTexture,
            Identifier fullTexture
    ) {

        public static final SmokeStackIcon DEFAULT = new SmokeStackIcon(
            Aileron.of("hud/smokestack/container"),
            Aileron.of("hud/smokestack/full")
        );

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

        for (int i = 0; i < player.getMaxSmokeStacks(); i++) {
            int spriteY = screenY - (i * 9);
            context.drawGuiTexture(SmokeStackIcon.DEFAULT.containerTexture, screenX, spriteY, 9, 9);
        }

        for (int i = 0; i < player.getSmokeStacks(); i++) {
            int spriteY = screenY - (i * 9);
            context.drawGuiTexture(SmokeStackIcon.DEFAULT.fullTexture, screenX, spriteY, 9, 9);
        }

    }

    public static int moveAttackIndicator(int spriteX) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return spriteX;

        if (player.getMainArm() == Arm.LEFT) spriteX -= 9;
        else spriteX += 5;

        return spriteX;
    }

}
