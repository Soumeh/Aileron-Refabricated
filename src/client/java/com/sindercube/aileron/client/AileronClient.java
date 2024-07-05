package com.sindercube.aileron.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class AileronClient implements ClientModInitializer {

	public static int cooldown = 0;
	public static boolean wasJumping = false;

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(AileronSmokestackRenderer::renderSmokeStackBar);
		AileronClientNetworking.register();
	}

	public static void launchPlayer() {
		// TODO do
//		Aileron.boostPlayer(Minecraft.getInstance().player);
	}


//	public static void localPlayerTick(Player self) {
//		if (self instanceof LocalPlayer localPlayer) {
//			boolean jumping = localPlayer.input.jumping;
//			if (jumping && !wasJumping && cooldown <= 0 && self.isFallFlying() && localPlayer.getFallFlyingTicks() > 0) {
//				int stocks = self.getEntityData().get(AileronEntityData.SMOKE_STACK_CHARGES);
//
//				if (stocks > 0) {
//					self.setDeltaMovement(self.getDeltaMovement().add(self.getLookAngle().scale(1.5)));
//					self.getEntityData().set(AileronEntityData.SMOKE_STACK_CHARGES, stocks - 1);
//					self.level().playSound(null, self.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.8f, 0.4f);
//					AileronClientNetworking.sendSmokeStackDash();
//				}
//
//				cooldown = 50;
//			}
//
//			wasJumping = jumping;
//		}
//
//		if (cooldown > 0) cooldown--;
//	}

}
