package com.sindercube.aileron.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sindercube.aileron.registry.AileronGamerules;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(FireworkRocketItem.class)
public class MixinFireworkRocketItem {

	@Unique private static final FireworksComponent FALLBACK_COMPONENT = new FireworksComponent(1, List.of());
	@Unique private static final int DURATION_MULTIPLIER = 64;

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private boolean handleBoosting(World world, Entity entity, @Local(argsOnly = true) PlayerEntity player, @Local ItemStack stack) {
		FireworkRocketItem item = (FireworkRocketItem)(Object)this;
		int cooldown = world.getGameRules().getInt(AileronGamerules.FIREWORK_BOOST_COOLDOWN);
		if (cooldown > 0) player.getItemCooldownManager().set(item, cooldown);

		if (world.getGameRules().getBoolean(AileronGamerules.FIREWORK_BOOSTS_FLIGHT)) return world.spawnEntity(entity);

		int duration = stack.getOrDefault(DataComponentTypes.FIREWORKS, FALLBACK_COMPONENT).flightDuration() * DURATION_MULTIPLIER;
		player.setSmokeTrailTicks(duration);
		return false;
	}

}
