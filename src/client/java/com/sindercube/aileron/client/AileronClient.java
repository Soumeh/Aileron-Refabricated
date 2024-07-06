package com.sindercube.aileron.client;

import com.sindercube.aileron.content.packets.SmokeStackLaunchPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;

public class AileronClient implements ClientModInitializer {

	public static KeyBinding JUMP_KEY = null;


	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(SmokestackRenderer::renderSmokeStackBar);

		AileronClientNetworking.register();





		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (JUMP_KEY.wasPressed()) {
				if (client.player == null) continue;
				if (client.player.isFallFlying()) ClientPlayNetworking.send(SmokeStackLaunchPacket.INSTANCE);
			}
		});
	}

}
