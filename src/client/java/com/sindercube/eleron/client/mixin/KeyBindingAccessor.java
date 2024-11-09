package com.sindercube.eleron.client.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

	@Accessor
	InputUtil.Key getBoundKey();

	@Accessor
	int getTimesPressed();

	@Accessor("timesPressed")
	void setTimesPressed(int value);

	@Accessor("KEY_TO_BINDINGS")
	static Map<InputUtil.Key, KeyBinding> getKeyToBindings() {
		throw new AssertionError();
	}

	@Accessor("KEYS_BY_ID")
	static Map<String, KeyBinding> getKeysById() {
		throw new AssertionError();
	}

}
