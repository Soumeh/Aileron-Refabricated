package com.sindercube.eleron.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.sindercube.eleron.client.util.InclusiveKeyBinding;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class KeyBindingEntryMixin {

	@Shadow @Final private KeyBinding binding;

	@ModifyExpressionValue(
		method = "update",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/option/KeyBinding;equals(Lnet/minecraft/client/option/KeyBinding;)Z"
		)
	)
	private boolean ignoreKeybindConflicts(boolean result, @Local KeyBinding otherKeybind) {
		if (otherKeybind instanceof InclusiveKeyBinding) return false;
		if (binding instanceof InclusiveKeyBinding) return false;
		return result;
	}

}
