package com.sindercube.aileron;

import net.minecraft.util.Identifier;

public class AileronNetworking {

	public static final Identifier LAUNCH_SMOKE_STACK_PACKET_ID = Aileron.of("launch_smoke_stack");
	public static final Identifier SMOKE_STACK_DASH_PACKET_ID = Aileron.of("dash_smoke_stack");

	public static void init() {
//		ServerPlayNetworking.registerGlobalReceiver(SMOKE_STACK_DASH_PACKET_ID, (server, player, handler, buf, responseSender) -> Aileron.playerDashedServer(player));
	}

//	public static void sendSmokeStackLaunch(ServerPlayerEntity player) {
//		ServerPlayNetworking.send(player, LAUNCH_SMOKE_STACK_PACKET_ID, PacketByteBufs.empty());
//	}

}
