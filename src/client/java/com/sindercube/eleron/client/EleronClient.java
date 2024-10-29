package com.sindercube.eleron.client;

import com.sindercube.eleron.content.packet.SmokeStackBoostPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class EleronClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerPackets();
		HudRenderCallback.EVENT.register(SmokeStackHudRenderer::renderSmokeStackBar);
		ClientTickEvents.END_CLIENT_TICK.register(EleronClient::handleControls);
	}

	public static void registerPackets() {
		PayloadTypeRegistry.playC2S().register(SmokeStackBoostPacket.ID, SmokeStackBoostPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SmokeStackBoostPacket.ID, SmokeStackBoostPacket::boost);
	}

	private static boolean WAS_JUMP_PRESSED = false;

	public static void handleControls(MinecraftClient client) {
		GameOptions options = client.options;
		if (!WAS_JUMP_PRESSED && options.jumpKey.isPressed()) ClientPlayNetworking.send(SmokeStackBoostPacket.INSTANCE);
		WAS_JUMP_PRESSED = options.jumpKey.isPressed();
	}

}
