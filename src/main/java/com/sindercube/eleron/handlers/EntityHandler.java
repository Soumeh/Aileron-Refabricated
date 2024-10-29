package com.sindercube.eleron.handlers;

import com.sindercube.eleron.registry.EleronAttributes;
import com.sindercube.eleron.registry.EleronGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHandler {

    public static final int MAX_DEPTH = 32;


	public static void tickCampfireCharging(World world, PlayerEntity player) {
		if (world.isClient) return;
		if (player.getMaxSmokestackCharges() <= 0) return;
		if (!player.getSteppingBlockState().isIn(BlockTags.CAMPFIRES)) {
			player.setCampfireChargeTime(0);
			return;
		}

		player.tickCampfireChargeTime();
		int chargeTime = player.getCampfireChargeTime();
		if (chargeTime % world.getGameRules().getInt(EleronGamerules.SMOKESTACK_CHARGE_TICKS) != 0) return;

		int stacks = player.getSmokestackCharges();
		int maxStacks = player.getMaxSmokestackCharges();
		if (stacks >= maxStacks) return;

		player.addSmokestackCharge();

		final ServerWorld serverWorld = (ServerWorld) world;
		Vec3d pos = player.getPos();
		serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.1);
		serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 100, 0.5, 0.5, 0.5, 0.4);
		player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, 0.8f + stacks * 0.2f);
	}

    public static void tickSmokeTrial(World world, PlayerEntity player) {
        if (!player.isFallFlying()) player.setSmokeTrailTicks(0);

        if (player.getSmokeTrailTicks() <= 0) return;

        if (player.age % 3 == 0 && !world.isClient) {
            final ServerWorld serverWorld = (ServerWorld) world;
            Vec3d pos = player.getPos().add(player.getRotationVector().multiply(-1.0));
            serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0.005);
        }
    }

	public static void tickSmokestackChargeDecay(World world, PlayerEntity player) {
		if (world.isClient) return;

		if (player.isOnGround() && player.getCampfireChargeTime() == 0) {
			player.setSmokestackCharges(0);
			return;
		}

		if (player.getSmokestackCharges() >= player.getMaxSmokestackCharges())
			player.setSmokestackCharges(player.getMaxSmokestackCharges());


//		System.out.println(player.getCampfireChargeTime());
//		if (player.getCampfireChargeTime() > 0) return;
//		if (player.isFallFlying()) return;
//		player.setSmokestackCharges(0);
	}

    public static void tickFlightBoost(World world, PlayerEntity player) {
        if (player.getFlightBoostTicks() <= 0) return;
        if (!player.isFallFlying()) {
			player.setFlightBoostTicks(0);
            return;
        }

        Vec3d rotation = player.getRotationVector();
        Vec3d velocity = player.getVelocity();
        double finalMul = 0.1;
        double baseMul = 1.5;
        player.addVelocity(
                rotation.x * finalMul + (rotation.x * baseMul - velocity.x) * 0.5,
                rotation.y * finalMul + (rotation.y * baseMul - velocity.y) * 0.5,
                rotation.z * finalMul + (rotation.z * baseMul - velocity.z) * 0.5
        );
        player.velocityModified = true;

        if (!world.isClient && player.age % 3 == 0) {
            ServerWorld serverWorld = (ServerWorld) world;
            Vec3d pos = player.getPos();
            serverWorld.spawnParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 2, 0.2, 0.2, 0.2, 0.1);
            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 3, 0.2, 0.2, 0.2, 0.1);
            serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 1, 0.2, 0.2, 0.2, 0.0);
        }
    }

    public static void tickCampfireUpdrafts(World world, PlayerEntity player) {
        if (world.isClient) return;
        if (!player.isFallFlying() || player.isMainPlayer()) return;
//        if (!player.isFallFlying()) return;

        BlockPos pos = player.getBlockPos();
        int depth = 0;
        while (depth < MAX_DEPTH && world.isAir(pos) && world.isInBuildLimit(pos)) {
            depth++;
            pos = pos.down();
        }

        BlockState state = world.getBlockState(pos);
        if (!state.isIn(BlockTags.CAMPFIRES) || !world.getGameRules().getBoolean(EleronGamerules.CAMPFIRES_UPDRAFTS)) return;
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
        player.velocityModified = true;
    }


    public static void modifyVelocity(LivingEntity entity, Vec3d velocity) {
        World world = entity.getWorld();
        int dragReduction = (int)entity.getAttributeValue(EleronAttributes.ALTITUDE_DRAG_REDUCTION);

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
