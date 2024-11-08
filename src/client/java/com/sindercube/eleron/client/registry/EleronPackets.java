package com.sindercube.eleron.client.registry;

import com.sindercube.eleron.content.packet.SmokeStackBoostPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class EleronPackets {

	public static void init() {
		PayloadTypeRegistry.playC2S().register(SmokeStackBoostPacket.ID, SmokeStackBoostPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SmokeStackBoostPacket.ID, SmokeStackBoostPacket::boost);
	}

}
