package com.sindercube.aileron;

import com.sindercube.aileron.registry.AileronAttachments;
import com.sindercube.aileron.registry.AileronAttributes;
import com.sindercube.aileron.registry.AileronGamerules;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Aileron implements ModInitializer {

	public static final String MOD_ID = "aileron";
	public static final Logger LOGGER = LoggerFactory.getLogger("Manic");

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		AileronConfig.HANDLER.load();
		register();
		LOGGER.info("Initialized!");
	}

	public static void register() {
		AileronAttachments.init();
		AileronAttributes.init();
		AileronGamerules.init();
		AileronNetworking.init();
	}

//	public static boolean isModInstalled(String modId) {
//		return false;
//	}

//	public static boolean canChargeSmokeStack(@Nullable PlayerEntity player) {
//		return false;
//	}

//	public static boolean isElytra(ItemStack stack) {
//		return false;
//	}

//	public static void boostPlayer(PlayerEntity player) {
//		if (player instanceof AileronPlayer ap) {
//			ap.setBoostTicks(50);
//		}
//	}

//	public static void playerDashedServer(PlayerEntity player) {
//		World serverLevel = player.getWorld();
//		// TODO replace with data attachment
//		int stocks = player.getEntityData().get(AileronEntityData.SMOKE_STACK_CHARGES);
//
//		if (stocks > 0) {
//			player.getEntityData().set(AileronEntityData.SMOKE_STACK_CHARGES, stocks - 1);
//
//			sendBoostParticles(serverLevel, player.getX(), player.getY(), player.getZ());
//
//			boostPlayer(player);
//		}
//	}

//	public static void sendBoostParticles(ServerWorld world, double x, double y, double z) {
//		for (ServerPlayerEntity player : world.getPlayers()) {
//			world.spawnParticles(player, ParticleTypes.LARGE_SMOKE, false, x, y, z, 40, 0.5, 0.5, 0.5, 0.1);
//			world.spawnParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, false, x, y, z, 40, 0.5, 0.5, 0.5, 0.1);
//			world.spawnParticles(player, ParticleTypes.SMOKE, false, x, y, z, 120, 0.5, 0.5, 0.5, 0.4);
//		}
//	}

}
