package com.sindercube.eleron.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

	@Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;serverDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"), cancellable = true)
	private void protectWithSmokeStack(BlockState s, World w, BlockPos p, Entity entity, CallbackInfo ci) {
		if (entity instanceof PlayerEntity player && player.isSneaking() && player.getMaxSmokestackCharges() >= 1) ci.cancel();
	}

//	TODO maybe reimplement this sometime idk
//	/**
//	 * @author Sindercube
//	 * @reason Stacking particle distance
//	 */
//	@Overwrite
//	public static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
//		CampfireHandler.spawnSmokeParticle(world, pos, isSignal, lotsOfSmoke);
//	}

}
