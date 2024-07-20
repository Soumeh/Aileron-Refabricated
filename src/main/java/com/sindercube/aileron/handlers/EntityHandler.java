package com.sindercube.aileron.handlers;

import com.sindercube.aileron.registry.AileronAttributes;
import com.sindercube.aileron.registry.AileronGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHandler {

    public static final int MAX_DEPTH = 32;


    public static void tickSmokeTrial(World world, PlayerEntity player) {
        if (!player.isFallFlying()) player.setSmokeTrailTicks(0);

        if (player.getSmokeTrailTicks() <= 0) return;

        if (player.age % 3 == 0) {
            final ServerWorld serverWorld = (ServerWorld) world;
            Vec3d pos = player.getPos().add(player.getRotationVector().multiply(-1.0));
            serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0.005);
        }
    }

    public static void tickFlyingTimer(World world, PlayerEntity player) {
        if (player.getFlyingTimer() == 0 && !world.isClient) {
            player.setFlyingTimer(-1);
            player.startFallFlying();
        }
    }

    public static void tickFlightBoost(World world, PlayerEntity player) {
        if (player.getFlightBoostTicks() <= 0) return;
        if (!player.isFallFlying()) player.setFlightBoostTicks(0);

        Vec3d rotation = player.getRotationVector();
        Vec3d velocity = player.getVelocity();
        player.addVelocity(
                rotation.x * 0.1D + (rotation.x * 1.5D - velocity.x) * 0.5D,
                rotation.y * 0.1D + (rotation.y * 1.5D - velocity.y) * 0.5D,
                rotation.z * 0.1D + (rotation.z * 1.5D - velocity.z) * 0.5D
        );

        if (player.age % 3 == 0) {
            if (world.isClient) return;
            ServerWorld serverWorld = (ServerWorld) world;
            Vec3d pos = player.getPos();
            serverWorld.spawnParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 2, 0.2, 0.2, 0.2, 0.1);
            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 3, 0.2, 0.2, 0.2, 0.1);
            serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 1, 0.2, 0.2, 0.2, 0.0);
        }
    }

    public static void tickCampfireUpdrafts(World world, PlayerEntity player) {
        if (world.isClient) return;
        if (!player.isFallFlying() || !player.isMainPlayer()) return;

        BlockPos pos = player.getBlockPos();
        int depth = 0;
        while (depth < MAX_DEPTH && world.isAir(pos) && world.isInBuildLimit(pos)) {
            depth++;
            pos = pos.down();
        }

        BlockState state = world.getBlockState(pos);
        if (!state.isIn(BlockTags.CAMPFIRES) || !world.getGameRules().getBoolean(AileronGamerules.CAMPFIRES_UPDRAFTS)) return;
        if (!state.get(CampfireBlock.LIT)) return;

//		int neighbors = (int) Stream.of(pos.north(), pos.south(), pos.east(), pos.west())
//				.filter(nPos -> serverWorld.getBlockState(nPos).getBlock() instanceof CampfireBlock)
//				.count();

        boolean isSignal = state.get(CampfireBlock.SIGNAL_FIRE);
        int range = isSignal ? 24 : 10;
//		int range = isSignal ? 24 : 10 + (3 * neighbors);


        double distance = Math.abs(pos.getY() - player.getY());
        if (distance > range) return;

        double force = Math.min(range / distance / 7, 1.0);
        Vec3d velocity = player.getVelocity();
        velocity = new Vec3d(
                velocity.x,
                Math.min(velocity.y + force, 1.0),
                velocity.z
        );
        player.setVelocity(velocity);
    }


    public static void modifyVelocity(LivingEntity entity, Vec3d velocity) {
        World world = entity.getWorld();
        int dragReduction = (int)entity.getAttributeValue(AileronAttributes.ALTITUDE_DRAG_REDUCTION);

        double fac;

        double y = entity.getY();
        if (y < 100) fac = 0.0;
        else if (y < 230) fac = 0.00006 * Math.pow(y - 100, 2);
        else fac = 1;

        fac *= 0.6;
        fac *= dragReduction / 3.0;

        if (fac > 0.1 && !world.isClient && entity.age % ((int) (1.0 - fac) * 2 + 1) == 0) {
            ServerWorld serverWorld = (ServerWorld)world;
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                Vec3d pos = entity.getPos().add(entity.getRotationVector().multiply(-1.0));
                serverWorld.spawnParticles(player, ParticleTypes.POOF, false, pos.x, pos.y, pos.z, 1 + (int) (fac * 4.0), 0.1, 0.1, 0.1, 0.025);
            }
        }

        Vec3d negator = new Vec3d(1.0 / 0.9900000095367432D, 1.0, 1.0 / 0.9900000095367432D);
        negator = new Vec3d(negator.x, 1.0, negator.z);
        velocity = velocity.lerp(velocity.multiply(negator), fac);

        entity.setVelocity(velocity);
    }

}
