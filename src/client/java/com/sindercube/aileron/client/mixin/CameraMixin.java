package com.sindercube.aileron.client.mixin;

import com.sindercube.aileron.access.AileronCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin implements AileronCamera {

	@Unique double previousEMAValue = 0.0;
	@Unique double EMAValue = 0.0;
	@Unique float smoothedEMADifference = 0.0f;

	@Shadow public abstract float getYaw();


	@Inject(method = "update", at = @At("TAIL"))
	public void setup(BlockView area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		smoothedEMADifference = entity != null ? getYaw() - (float) (EMAValue + (EMAValue - previousEMAValue) * tickDelta) : 0.0f;
	}


	@Override
	public double getPreviousEMAValue() {
		return previousEMAValue;
	}

	@Override
	public void setPreviousEMAValue(float previousEMA) {
		previousEMAValue = previousEMA;
	}

	@Override
	public double getEMAValue() {
		return EMAValue;
	}

	@Override
	public void setEMAValue(float EMA) {
		EMAValue = EMA;
	}

	@Override
	public float getSmoothedEMADifference() {
		return smoothedEMADifference;
	}

}
