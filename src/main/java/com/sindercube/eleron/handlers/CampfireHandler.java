package com.sindercube.eleron.handlers;

public class CampfireHandler {

//    public static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
//		int neighbors = (int) Stream.of(pos.north(), pos.south(), pos.east(), pos.west())
//				.filter(nPos -> world.getBlockState(nPos).isIn(BlockTags.CAMPFIRES))
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
