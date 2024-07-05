package com.sindercube.aileron.mixin;

import com.sindercube.aileron.registry.AileronGamerules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
public class MixinFireworkRocketItem {

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if (world.getGameRules().getBoolean(AileronGamerules.FIREWORK_BOOST)) return;
		cir.cancel();

//		ItemStack stack = player.getStackInHand(hand);
//		stack.decrementUnlessCreative(1, player);

		player.setSmokeTrailTicks(100);
		FireworkRocketItem item = (FireworkRocketItem)(Object)this;
		player.getItemCooldownManager().set(item, 100);
//		player.increaseStat();
//		player.awardStat(Stats.ITEM_USED.get(item));
//		cir.setReturnValue(InteractionResultHolder.pass(stack));
	}

}
