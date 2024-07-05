package com.sindercube.aileron.client;

import com.sindercube.aileron.AileronNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class AileronClientNetworking {

	// TODO do

	public static void sendSmokeStackDash() {
//		ClientPlayNetworking.send(AileronNetworking.SMOKE_STACK_DASH_PACKET_ID, PacketByteBufs.empty());
	}

	public static void register() {
//		ClientPlayNetworking.registerGlobalReceiver(AileronNetworking.LAUNCH_SMOKE_STACK_PACKET_ID, (client, handler, buf, responseSender) -> AileronClient.launchPlayer());
	}

}
