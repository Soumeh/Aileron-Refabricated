package com.sindercube.aileron.mixin;

import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Stream;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

	@Redirect(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;spawnSmokeParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZZ)V"))
	private static void makeParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {

		long neighbors = Stream.of(pos.north(), pos.south(), pos.east(), pos.west())
				.filter(nPos -> world.getBlockState(nPos).getBlock() instanceof CampfireBlock)
				.count();

		if (neighbors == 0) {
			CampfireBlock.spawnSmokeParticle(world, pos, isSignal, lotsOfSmoke);
			return;
		}

		Random random = world.getRandom();
		SimpleParticleType particle = isSignal ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
		world.addImportantParticle(particle, true,
				(double) pos.getX() + 0.5 + random.nextDouble() / 3 * (double) (random.nextBoolean() ? 1 : -1),
				(double) pos.getY() + random.nextDouble() + random.nextDouble(),
				(double) pos.getZ() + 0.5 + random.nextDouble() / 3 * (double) (random.nextBoolean() ? 1 : -1),
				neighbors * 40 + (isSignal ? 280 : 80), 0.07, 0
		);
		if (lotsOfSmoke) world.addParticle(ParticleTypes.SMOKE,
				(double) pos.getX() + 0.5 + random.nextDouble() / 4 * (double) (random.nextBoolean() ? 1 : -1),
				(double) pos.getY() + 0.4,
				(double) pos.getZ() + 0.5 + random.nextDouble() / 4 * (double) (random.nextBoolean() ? 1 : -1),
				0, 0.005, 0
		);
	}

}
