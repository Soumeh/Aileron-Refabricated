package com.sindercube.aileron.client.mixin;

import com.sindercube.aileron.client.AileronControls;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Shadow @Final public KeyBinding jumpKey;
    @Shadow @Final public KeyBinding sneakKey;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        AileronControls.JUMP_KEY = jumpKey;
        AileronControls.SNEAK_KEY = sneakKey;
    }

}
