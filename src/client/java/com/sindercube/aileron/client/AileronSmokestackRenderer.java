package com.sindercube.aileron.client;

import com.sindercube.aileron.Aileron;
import com.sindercube.aileron.registry.AileronAttachments;
import com.sindercube.aileron.registry.AileronAttributes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class AileronSmokestackRenderer {

    public record SmokestackIcon (
            Identifier containerTexture,
            Identifier fullTexture
    ) {

        public static final SmokestackIcon DEFAULT = new SmokestackIcon(
            Aileron.of("hud/smokestack/container"),
            Aileron.of("hud/smokestack/full") // sprites/
        );

    }

    public static void renderSmokeStackBar(DrawContext context, RenderTickCounter t) {
        int screenHeight = context.getScaledWindowHeight();
        int screenWidth = context.getScaledWindowWidth();
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        int screenX = (screenWidth / 2);
        if (player.getMainArm() == Arm.LEFT) screenX -= 102;
        else screenX += 92;

        int screenY = screenHeight - 10;

        for (int i = 0; i < player.getMaxSmokestacks(); i++) {
            int spriteY = screenY - (i * 9);
            context.drawGuiTexture(SmokestackIcon.DEFAULT.containerTexture, screenX, spriteY, 9, 9);
        }

        for (int i = 0; i < player.getSmokeStacks(); i++) {
            int spriteY = screenY - (i * 9);
            context.drawGuiTexture(SmokestackIcon.DEFAULT.fullTexture, screenX, spriteY, 9, 9);
        }



    }

    public static int moveAttackIndicator(int spriteX) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return spriteX;
        if (!player.canChargeSmokeStack()) return spriteX;

        if (player.getMainArm() == Arm.LEFT) spriteX -= 9;
        else spriteX += 5;

        return spriteX;
    }

}
