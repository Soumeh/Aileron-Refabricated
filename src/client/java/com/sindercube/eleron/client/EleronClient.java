package com.sindercube.eleron.client;

import com.sindercube.eleron.client.handler.SmokeStackGuiRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class EleronClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EleronClientNetworking.register();
		HudRenderCallback.EVENT.register(SmokeStackGuiRenderer::renderSmokeStackBar);
		ClientTickEvents.END_CLIENT_TICK.register(EleronControls::handle);
	}

}
