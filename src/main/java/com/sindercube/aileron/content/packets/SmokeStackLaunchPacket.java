package com.sindercube.aileron.content.packets;

import com.sindercube.aileron.Aileron;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundEvents;

public class SmokeStackLaunchPacket implements CustomPayload {

    public static final SmokeStackLaunchPacket INSTANCE = new SmokeStackLaunchPacket();
    public static final CustomPayload.Id<SmokeStackLaunchPacket> ID = new CustomPayload.Id<>(Aileron.of("smokestack_launch"));
    public static final PacketCodec<RegistryByteBuf, SmokeStackLaunchPacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }


    private static final int BOOST_TICKS = 50;

    public void launch(PlayerEntity player) {
        System.out.println("e");
        if (player.getSmokeStacks() <= 0) return;

        player.setVelocity(player.getVelocity().add(player.getRotationVector().multiply(1.5)));
        player.modifySmokeStacks(stacks -> stacks - 1);
        player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8F, 0.4F);
//        player.setBoostTicks(BOOST_TICKS);
    }

}
