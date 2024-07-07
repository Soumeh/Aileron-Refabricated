package com.sindercube.aileron.client;

import com.sindercube.aileron.content.packets.SmokeStackDashPacket;
import com.sindercube.aileron.content.packets.SmokeStackLaunchPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class AileronControls {

    public static KeyBinding JUMP_KEY = null;
    public static KeyBinding SNEAK_KEY = null;

    private static boolean WAS_JUMP_PRESSED = false;
    private static boolean WAS_SNEAK_PRESSED = false;

    public static void handle(MinecraftClient client) {
        if (!WAS_JUMP_PRESSED && JUMP_KEY.isPressed()) ClientPlayNetworking.send(SmokeStackDashPacket.INSTANCE);
        if (WAS_SNEAK_PRESSED && !SNEAK_KEY.isPressed()) ClientPlayNetworking.send(SmokeStackLaunchPacket.INSTANCE);

        WAS_JUMP_PRESSED = JUMP_KEY.isPressed();
        WAS_SNEAK_PRESSED = SNEAK_KEY.isPressed();
    }

}
