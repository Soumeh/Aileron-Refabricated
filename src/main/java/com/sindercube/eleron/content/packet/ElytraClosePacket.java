package com.sindercube.eleron.content.packet;

import com.sindercube.eleron.Eleron;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class ElytraClosePacket implements CustomPayload {

	public static final CustomPayload.Id<ElytraClosePacket> ID = new CustomPayload.Id<>(Eleron.of("elytra_close"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static final ElytraClosePacket INSTANCE = new ElytraClosePacket();
	public static final PacketCodec<RegistryByteBuf, ElytraClosePacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

	public void closeElytra(ServerPlayNetworking.Context context) {
		PlayerEntity player = context.player();
		player.stopGliding();
	}

}
