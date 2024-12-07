package com.sindercube.eleron.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sindercube.eleron.handlers.FireworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
	private <T extends ProjectileEntity> T handleBoosting(T projectile, ServerWorld world, ItemStack stack, @Local(argsOnly = true) PlayerEntity player) {
		FireworkHandler.handleBoosting(world, player, projectile, stack);
		return projectile;
	}

	@Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
	private <T extends ProjectileEntity> T handleBoostingOnBlock(T projectile, ServerWorld world, ItemStack stack, @Local(argsOnly = true) ItemUsageContext context) {
		FireworkHandler.handleBoosting(world, context.getPlayer(), projectile, stack);
		return projectile;
	}

}
