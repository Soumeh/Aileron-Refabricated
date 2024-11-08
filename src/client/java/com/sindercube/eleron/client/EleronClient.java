package com.sindercube.eleron.client;

import com.sindercube.eleron.client.registry.EleronKeybinds;
import com.sindercube.eleron.client.registry.EleronPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class EleronClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EleronKeybinds.init();
		EleronPackets.init();

		HudRenderCallback.EVENT.register(SmokeStackHudRenderer::renderSmokeStackBar);
	}

}
