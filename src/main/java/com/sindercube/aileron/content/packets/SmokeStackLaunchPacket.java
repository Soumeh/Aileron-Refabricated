package com.sindercube.aileron.content.packets;

import com.sindercube.aileron.Aileron;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class SmokeStackLaunchPacket implements CustomPayload {

    public static final SmokeStackLaunchPacket INSTANCE = new SmokeStackLaunchPacket();
    public static final Id<SmokeStackLaunchPacket> ID = new Id<>(Aileron.of("smokestack_dash"));
    public static final PacketCodec<RegistryByteBuf, SmokeStackLaunchPacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }


    public void launch(ServerPlayNetworking.Context context) {
        PlayerEntity player = context.player();
        if (player.isSneaking()) return;
        if (!player.isCampfireCharged()) return;

        ServerWorld world = context.player().getServerWorld();
        player.setCampfireChargeTime(0);

        player.startFallFlying();
        player.setFlightBoostTicks(50);
        player.checkFallFlying();
        player.setFlyingTimer(2);

        Vec3d pos = player.getPos();
        world.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 40, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 40, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 120, 0.5, 0.5, 0.5, 0.4);
        player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, 0.8f);
    }

}
