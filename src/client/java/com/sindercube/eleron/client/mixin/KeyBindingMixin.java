package com.sindercube.eleron.client.mixin;

import com.sindercube.eleron.client.util.InclusiveKeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static com.sindercube.eleron.client.util.InclusiveKeyBinding.KEY_TO_MULTIPLE_BINDINGS;
import static com.sindercube.eleron.client.util.InclusiveKeyBinding.UNIQUE_KEYS_BY_ID;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

	@Inject(method = "onKeyPressed", at = @At("TAIL"))
	private static void afterOnKeyPressed(InputUtil.Key key, CallbackInfo ci) {
		KEY_TO_MULTIPLE_BINDINGS.getOrDefault(key, new ArrayList<>()).forEach(binding -> ++binding.timesPressed);
	}

	@Inject(method = "setKeyPressed", at = @At("TAIL"))
	private static void afterSetKeyPressed(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
		KEY_TO_MULTIPLE_BINDINGS.getOrDefault(key, new ArrayList<>()).forEach(binding -> binding.setPressed(pressed));
	}

	@Inject(method = "updateKeysByCode", at = @At("TAIL"))
	private static void afterUpdateKeysByCode(CallbackInfo ci) {
		KEY_TO_MULTIPLE_BINDINGS.clear();

		for (InclusiveKeyBinding keybind : UNIQUE_KEYS_BY_ID.values()) {
			if (!KEY_TO_MULTIPLE_BINDINGS.containsKey(keybind.boundKey))
				KEY_TO_MULTIPLE_BINDINGS.put(keybind.boundKey, new ArrayList<>());
			KEY_TO_MULTIPLE_BINDINGS.get(keybind.boundKey).add(keybind);
		}
	}

}
