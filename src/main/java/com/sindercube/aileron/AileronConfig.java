package com.sindercube.aileron;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.DoubleField;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

// TODO implement using YALC
public class AileronConfig {

	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("aileron.json5");

	public static ConfigClassHandler<AileronConfig> HANDLER = ConfigClassHandler.createBuilder(AileronConfig.class)
			.id(Aileron.of("config"))
			.serializer(config -> GsonConfigSerializerBuilder.create(config)
					.setPath(CONFIG_PATH)
					.setJson5(true)
					.build())
			.build();


	public static AileronConfig getConfig() {
		return HANDLER.instance();
	}

	@SerialEntry
	private static boolean doCameraRoll = true;

	@SerialEntry
	@DoubleField(min = 0, max = 2)
	private static double cameraRollScale = 1.0;

	@SerialEntry
	@DoubleField(min = 0.05, max = 1)
	private static double cameraRollSpeed = 0.1;


	public static boolean doesCameraRoll() {
		return doCameraRoll;
	}

	public static double getCameraRollScale() {
		return cameraRollScale;
	}

	public static double getCameraRollSpeed() {
		return cameraRollSpeed;
	}

}
