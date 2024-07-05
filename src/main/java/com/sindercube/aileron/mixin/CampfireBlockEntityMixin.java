package com.sindercube.aileron.mixin;

import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

	@Redirect(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;spawnSmokeParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZZ)V"))
	private static void makeParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
		BlockPos[] possibleNeighbors = new BlockPos[]{
				pos.north(),
				pos.south(),
				pos.east(),
				pos.west()
		};
		int neighbors = 0;

		for (BlockPos neighbor : possibleNeighbors) {
			if (world.getBlockState(neighbor).getBlock() instanceof CampfireBlock) {
				neighbors++;
			}
		}

		if (neighbors == 0) {
			CampfireBlock.spawnSmokeParticle(world, pos, isSignal, lotsOfSmoke);
			return;
		}

		Random random = world.getRandom();
		world.addImportantParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true,
				(double) pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1),
				(double) pos.getY() + random.nextDouble() + random.nextDouble(),
				(double) pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1),
				neighbors * 40 + (isSignal ? 280 : 80), 0.07D, 0.0D
		);
		if (lotsOfSmoke) world.addParticle(ParticleTypes.SMOKE,
				(double) pos.getX() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1),
				(double) pos.getY() + 0.4D,
				(double) pos.getZ() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1),
				0.0D, 0.005D, 0.0D
		);
	}

}
