package com.sindercube.eleron.mixin;

import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ElytraItem.class)
public class ElytraItemMixin extends Item {

	public ElytraItemMixin(Settings settings) {
		super(settings);
	}

	@Override
	public int getEnchantability() {
		return 15;
	}

}
