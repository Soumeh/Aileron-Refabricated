package com.sindercube.aileron.client.mixin;

import com.sindercube.aileron.AileronConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow @Final public GameRenderer gameRenderer;

	@Unique float previousEMA = 0.0f;
	@Unique float EMA = 0.0f;

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		Camera camera = gameRenderer.getCamera();

		previousEMA = EMA;
		EMA = (float) (EMA + (camera.getYaw() - EMA) * AileronConfig.getCameraRollSpeed());

		camera.setPreviousEMAValue(previousEMA);
		camera.setEMAValue(EMA);
	}

}
