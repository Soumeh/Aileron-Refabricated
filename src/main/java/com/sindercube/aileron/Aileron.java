package com.sindercube.aileron;

import com.sindercube.aileron.registry.AileronAttributes;
import com.sindercube.aileron.registry.AileronGamerules;
import net.fabricmc.api.ModInitializer;
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
		AileronAttributes.init();
		AileronGamerules.init();
		LOGGER.info("Initialized!");
	}

}
