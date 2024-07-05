package com.sindercube.aileron.client.integration;

import com.sindercube.aileron.AileronConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class AileronModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AileronConfig.HANDLER.generateGui().generateScreen(parent);
    }

}
