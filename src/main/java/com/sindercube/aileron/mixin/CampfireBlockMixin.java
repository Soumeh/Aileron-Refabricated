package com.sindercube.aileron.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

	// TODO replace (maybe) with: world.getGameRules().getInt(AileronGamerules.SMOKE_STACK_CHARGE_TICKS);
	@Unique private final int CHARGE_TICKS = 30;

	@Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
	private void protectUsingSmokeStack(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof PlayerEntity player)) return;
		if (player.getCampfireIFrames() > 0) return;
		if (!player.isSneaking()) return;
		if (player.getMaxSmokeStacks() <= 0) return;
		ci.cancel();
	}

	@Inject(method = "onEntityCollision", at = @At("HEAD"))
	private void handleSmokestackCharging(BlockState s, World world, BlockPos p, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof PlayerEntity player)) return;
		if (!player.isSneaking() || player.getMaxSmokeStacks() <= 0) {
			player.setCampfireChargeTime(0);
			return;
		}
		player.modifyCampfireChargeTime(time -> time + 1);
		int chargeTime = player.getCampfireChargeTime();

		if (world.isClient) {
			Vec3d offset = new Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			offset = offset.normalize().multiply(2.0);
			Vec3d motion = offset.multiply(-0.075);
			Vec3d pos = player.getPos().add(0.0, player.getStandingEyeHeight() / 2.0, 0.0).add(offset);
			world.addParticle(ParticleTypes.FLAME, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
			world.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
		} else if (chargeTime % CHARGE_TICKS == 0) {
			player.setCampfireCharged(true);
			int smokeStacks = player.getSmokeStacks();

			int maxSmokeStacks = player.getMaxSmokeStacks();

			if (smokeStacks < maxSmokeStacks) {
				player.setCampfireCharged(true);
				player.modifySmokeStacks(stacks -> stacks + 1);
				System.out.println(player.getSmokeStacks());

				final ServerWorld serverWorld = (ServerWorld) world;
				Vec3d pos = player.getPos();
				serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.1);
				serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 100, 0.5, 0.5, 0.5, 0.4);
				player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, 0.8f + smokeStacks * 0.2f);
			}
		}
	}

}
