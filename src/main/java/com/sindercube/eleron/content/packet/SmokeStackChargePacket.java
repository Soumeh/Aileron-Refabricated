package com.sindercube.eleron.content.packet;

import com.sindercube.eleron.Eleron;
import com.sindercube.eleron.registry.EleronSoundEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;

public class SmokeStackChargePacket implements CustomPayload {

	public static final CustomPayload.Id<SmokeStackChargePacket> ID = new CustomPayload.Id<>(Eleron.of("smokestack_charge"));

	@Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final SmokeStackChargePacket INSTANCE = new SmokeStackChargePacket();
    public static final PacketCodec<RegistryByteBuf, SmokeStackChargePacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

	private static final int STARTUP_TIME = 10;
    private static final int BOOST_TICKS = 40;
    private static final int COOLDOWN = 60;

    public void boost(ServerPlayNetworking.Context context) {
        PlayerEntity player = context.player();
        if (player.getSmokestackChargeCooldown() > 0) return;
        if (player.getSmokestackCharges() <= 0) return;

		if (player.getFallFlyingTicks() <= STARTUP_TIME) return;

		player.useSmokestackCharge();
		player.playSoundToPlayer(
			EleronSoundEvents.SMOKESTACK_CHARGE_USED,
			SoundCategory.PLAYERS,
			1,
			0.8f + (player.getSmokestackCharges() * 0.2f)
		);
		player.getWorld().playSound(
			player,
			player.getBlockPos(),
			EleronSoundEvents.SMOKESTACK_CHARGE_USED,
			SoundCategory.PLAYERS,
			1,
			0.8f + (player.getSmokestackCharges() * 0.2f)
		);
        player.setFlightBoostTicks(BOOST_TICKS);
        player.setSmokestackChargeCooldown(COOLDOWN);
    }

}
