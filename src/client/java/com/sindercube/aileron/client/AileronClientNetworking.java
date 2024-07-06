package com.sindercube.aileron.client;

import com.sindercube.aileron.content.packets.SmokeStackLaunchPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class AileronClientNetworking {

	public static void register() {
		PayloadTypeRegistry.playS2C().register(SmokeStackLaunchPacket.ID, SmokeStackLaunchPacket.PACKET_CODEC);
		ClientPlayNetworking.registerGlobalReceiver(SmokeStackLaunchPacket.ID, (payload, context) -> payload.launch(context.player()));
	}

}
