package com.sindercube.eleron.content.packet;

import com.sindercube.eleron.Eleron;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class SmokeStackBoostPacket implements CustomPayload {

	public static final CustomPayload.Id<SmokeStackBoostPacket> ID = new CustomPayload.Id<>(Eleron.of("smokestack_boost"));

	@Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final SmokeStackBoostPacket INSTANCE = new SmokeStackBoostPacket();
    public static final PacketCodec<RegistryByteBuf, SmokeStackBoostPacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

	private static final int STARTUP_TIME = 10;
    private static final int BOOST_TICKS = 40;
    private static final int COOLDOWN = 60;

    public void boost(ServerPlayNetworking.Context context) {
        PlayerEntity player = context.player();
        if (player.getSmokestackChargeCooldown() > 0) return;
        if (player.getSmokestackCharges() <= 0) return;

		if (player.getFallFlyingTicks() <= STARTUP_TIME) return;

		player.useSmokestackCharge();
        player.setFlightBoostTicks(BOOST_TICKS);
        player.setSmokestackChargeCooldown(COOLDOWN);
    }

}
