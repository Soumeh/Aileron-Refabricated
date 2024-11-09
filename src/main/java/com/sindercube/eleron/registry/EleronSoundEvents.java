package com.sindercube.eleron.registry;

import com.sindercube.eleron.Eleron;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class EleronSoundEvents {

	public static void init() {}

	public static final SoundEvent SMOKESTACK_CHARGE_GAINED = register("entity.player.smokestack_charge.gained");
	public static final SoundEvent SMOKESTACK_CHARGE_USED = register("entity.player.smokestack_charge.used");

	private static SoundEvent register(String path) {
		Identifier id = Eleron.of(path);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

}
