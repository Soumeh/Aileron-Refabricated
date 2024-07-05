package com.sindercube.aileron.client.mixin;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.sindercube.aileron.AileronConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModAbsent("cameraoverhaul")
@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Shadow @Final private Camera camera;

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"))
	public void renderLevel(RenderTickCounter tickCounter, CallbackInfo ci) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;
		if (!player.isFallFlying()) return;
		float roll = camera.getSmoothedEMADifference() * 0.225f;

		float deltaMovementSpeed = (float) player.getVelocity().length();

		// TODO i really dont know whats going on here
//		if (AileronConfig.doCameraRoll())
//			poseStack.mulPose(Axis.ZP.rotationDegrees((float) (roll * deltaMovementSpeed * AileronConfig.cameraRollScale())));
	}

}
