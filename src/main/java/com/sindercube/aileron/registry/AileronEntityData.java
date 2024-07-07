package com.sindercube.aileron.registry;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class AileronEntityData {

    public static final TrackedData<Integer> SMOKE_STACK_CHARGES = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

}
