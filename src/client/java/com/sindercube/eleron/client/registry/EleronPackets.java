package com.sindercube.eleron.client.registry;

import com.sindercube.eleron.content.packet.ElytraClosePacket;
import com.sindercube.eleron.content.packet.SmokeStackChargePacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class EleronPackets {

	public static void init() {
		PayloadTypeRegistry.playC2S().register(SmokeStackChargePacket.ID, SmokeStackChargePacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SmokeStackChargePacket.ID, SmokeStackChargePacket::boost);

		PayloadTypeRegistry.playC2S().register(ElytraClosePacket.ID, ElytraClosePacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ElytraClosePacket.ID, ElytraClosePacket::closeElytra);
	}

}
