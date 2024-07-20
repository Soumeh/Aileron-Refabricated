package com.sindercube.aileron.handlers;

import com.sindercube.aileron.access.AileronPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class CampfireHandler {

    public static void protectWithSmokeStack(Entity entity, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (!player.isSneaking()) return;
        if (player.getSmokeStacks() <= 0) return;
        ci.cancel();
    }

    public static void handleSmokestackCharging(World world, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (!player.isSneaking() || player.getMaxSmokeStacks() <= 0) {
            player.setCampfireChargeTime(0);
            return;
        }
        player.incrementCampfireChargeTime();
        int chargeTime = player.getCampfireChargeTime();

//        if (world.isClient) {
//            Vec3d offset = new Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
//            offset = offset.normalize().multiply(2.0);
//            Vec3d motion = offset.multiply(-0.075);
//            Vec3d pos = player.getPos().add(0.0, player.getStandingEyeHeight() / 2.0, 0.0).add(offset);
//            world.addParticle(ParticleTypes.FLAME, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
//            world.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        if (chargeTime % AileronPlayerEntity.CHARGE_TICKS == 0) {
            int stacks = player.getSmokeStacks();

            int maxSmokeStacks = player.getMaxSmokeStacks();

            if (stacks < maxSmokeStacks) {
                player.addSmokeStack();

                final ServerWorld serverWorld = (ServerWorld) world;
                Vec3d pos = player.getPos();
                serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.1);
                serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 100, 0.5, 0.5, 0.5, 0.4);
                player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, 0.8f + stacks * 0.2f);
            }
        }
    }

//    public static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
//		int neighbors = (int) Stream.of(pos.north(), pos.south(), pos.east(), pos.west())
//				.filter(nPos -> world.getBlockState(nPos).getBlock() instanceof CampfireBlock)
//				.count();
//
//		Random random = world.getRandom();
//		SimpleParticleType particle = isSignal ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
//		int height = (isSignal ? 280 : 80) + neighbors * 40;
//
//		world.addImportantParticle(particle, true,
//				(double)pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1),
//				(double)pos.getY() + random.nextDouble() + random.nextDouble(),
//				(double)pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1),
//				0.0, 0.07, 0.0
//		);
//
//
//
//		if (lotsOfSmoke) world.addParticle(ParticleTypes.SMOKE,
//				(double) pos.getX() + 0.5 + random.nextDouble() / 4 * (double) (random.nextBoolean() ? 1 : -1),
//				(double) pos.getY() + 0.4,
//				(double) pos.getZ() + 0.5 + random.nextDouble() / 4 * (double) (random.nextBoolean() ? 1 : -1),
//				0, 0.005, 0
//		);
//	}

}
