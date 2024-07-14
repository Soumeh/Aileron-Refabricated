package com.sindercube.aileron.client;

import com.sindercube.aileron.content.packets.SmokeStackDashPacket;
import com.sindercube.aileron.content.packets.SmokeStackLaunchPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class AileronControls {

    private static boolean WAS_JUMP_PRESSED = false;
    private static boolean WAS_SNEAK_PRESSED = false;

    public static void handle(MinecraftClient client) {
        var options = client.options;
        if (!WAS_JUMP_PRESSED && options.jumpKey.isPressed()) ClientPlayNetworking.send(SmokeStackDashPacket.INSTANCE);
        if (WAS_SNEAK_PRESSED && options.sneakKey.isPressed()) ClientPlayNetworking.send(SmokeStackLaunchPacket.INSTANCE);

        WAS_JUMP_PRESSED = options.jumpKey.isPressed();
        WAS_SNEAK_PRESSED = options.sneakKey.isPressed();
    }

}
