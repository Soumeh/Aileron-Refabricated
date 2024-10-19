package com.sindercube.eleron.client;

//import com.sindercube.eleron.content.packet.SmokeStackLaunchPacket;
import com.sindercube.eleron.content.packet.SmokeStackDashPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class EleronClientNetworking {

	public static void register() {
		PayloadTypeRegistry.playC2S().register(SmokeStackDashPacket.ID, SmokeStackDashPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SmokeStackDashPacket.ID, SmokeStackDashPacket::dash);
//		PayloadTypeRegistry.playC2S().register(SmokeStackLaunchPacket.ID, SmokeStackLaunchPacket.PACKET_CODEC);
//		ServerPlayNetworking.registerGlobalReceiver(SmokeStackLaunchPacket.ID, SmokeStackLaunchPacket::launch);
	}

}
