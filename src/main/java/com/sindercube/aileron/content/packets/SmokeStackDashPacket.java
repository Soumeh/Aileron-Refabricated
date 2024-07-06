package com.sindercube.aileron.content.packets;

import com.sindercube.aileron.Aileron;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class SmokeStackDashPacket implements CustomPayload {

    public static final SmokeStackDashPacket INSTANCE = new SmokeStackDashPacket();
    public static final Id<SmokeStackDashPacket> ID = new Id<>(Aileron.of("smokestack_dash"));
    public static final PacketCodec<RegistryByteBuf, SmokeStackDashPacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }


    private static final int BOOST_TICKS = 50;

    public void dash(ServerPlayNetworking.Context context) {
        ServerWorld world = context.player().getServerWorld();
        ServerPlayerEntity player = context.player();

        int smokeStacks = player.getSmokeStacks();
        if (smokeStacks == 0) return;

        player.modifySmokeStacks(stacks -> stacks - 1);
        player.setBoostTicks(BOOST_TICKS);
        spawnBoostParticles(world, player.getX(), player.getY(), player.getZ());
    }

    private static void spawnBoostParticles(ServerWorld world, double x, double y, double z) {
        world.spawnParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 40, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 40, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.SMOKE, x, y, z, 120, 0.5, 0.5, 0.5, 0.4);
    }

}
