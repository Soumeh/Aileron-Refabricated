package com.sindercube.aileron.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class AileronClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		AileronNetworking.register();
		HudRenderCallback.EVENT.register(SmokeStackRenderer::renderSmokeStackBar);
		ClientTickEvents.END_CLIENT_TICK.register(AileronControls::handle);
	}

}
