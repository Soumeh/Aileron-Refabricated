package com.sindercube.eleron.client;

import com.sindercube.eleron.content.packet.SmokeStackDashPacket;
//import com.sindercube.eleron.content.packet.SmokeStackLaunchPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class EleronControls {

    private static boolean WAS_JUMP_PRESSED = false;
//    private static boolean WAS_SNEAK_PRESSED = false;

    public static void handle(MinecraftClient client) {
        GameOptions options = client.options;
        if (!WAS_JUMP_PRESSED && options.jumpKey.isPressed()) ClientPlayNetworking.send(SmokeStackDashPacket.INSTANCE);
//        if (WAS_SNEAK_PRESSED && !options.sneakKey.isPressed()) ClientPlayNetworking.send(SmokeStackLaunchPacket.INSTANCE);

        WAS_JUMP_PRESSED = options.jumpKey.isPressed();
//        WAS_SNEAK_PRESSED = options.sneakKey.isPressed();
    }

}
