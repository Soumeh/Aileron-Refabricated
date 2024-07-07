package com.sindercube.aileron.mixin;

import com.sindercube.aileron.access.AileronPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements AileronPlayerEntity {

}
