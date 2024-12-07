package com.sindercube.eleron;

import com.sindercube.eleron.registry.EleronAttributes;
import com.sindercube.eleron.registry.EleronGamerules;
import com.sindercube.eleron.registry.EleronSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Eleron implements ModInitializer {

	public static final String MOD_ID = "eleron";
	public static final Logger LOGGER = LoggerFactory.getLogger("Eleron");

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		EleronAttributes.init();
		EleronGamerules.init();
		EleronSoundEvents.init();
		DefaultItemComponentEvents.MODIFY.register(context ->
			context.modify(Eleron::isElytra, Eleron::makeEnchantable)
		);
	}

	public static boolean isElytra(Item item) {
		return item.getDefaultStack().contains(DataComponentTypes.GLIDER);
	}

	public static void makeEnchantable(ComponentMap.Builder builder, Item item) {
		ItemStack stack = item.getDefaultStack();
		if (stack.contains(DataComponentTypes.ENCHANTABLE)) return;
		builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(15));
	}

}
