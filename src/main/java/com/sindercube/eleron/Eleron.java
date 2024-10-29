package com.sindercube.eleron;

import com.sindercube.eleron.registry.EleronAttributes;
import com.sindercube.eleron.registry.EleronGamerules;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
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
		LOGGER.info("Initialized!");
	}

}
