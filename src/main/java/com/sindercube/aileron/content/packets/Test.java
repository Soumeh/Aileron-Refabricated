package com.sindercube.aileron.content.packets;

import net.minecraft.network.packet.CustomPayload;

public class Test implements CustomPayload {

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }

}
