package com.sindercube.aileron.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sindercube.aileron.handlers.FireworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private boolean handleBoosting(World world, Entity entity, @Local(argsOnly = true) PlayerEntity player, @Local ItemStack stack) {
		return FireworkHandler.handleBoosting(world, player, entity, (FireworkRocketItem)(Object)this, stack);
	}

}
