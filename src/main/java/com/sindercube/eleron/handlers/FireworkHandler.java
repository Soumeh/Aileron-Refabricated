package com.sindercube.eleron.handlers;

import com.sindercube.eleron.registry.EleronGamerules;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class FireworkHandler {

    private static final FireworksComponent FALLBACK_COMPONENT = new FireworksComponent(1, List.of());
    private static final int DURATION_MULTIPLIER = 60;

    public static boolean handleBoosting(World world, PlayerEntity player, Entity firework, FireworkRocketItem item, ItemStack stack) {
        int cooldown = world.getGameRules().getInt(EleronGamerules.FIREWORK_BOOST_COOLDOWN);
        if (cooldown > 0) player.getItemCooldownManager().set(item, cooldown);

        if (world.getGameRules().getBoolean(EleronGamerules.FIREWORK_BOOSTS_FLIGHT)) return world.spawnEntity(firework);

        int duration = stack.getOrDefault(DataComponentTypes.FIREWORKS, FALLBACK_COMPONENT).flightDuration() * DURATION_MULTIPLIER;
        player.setSmokeTrailTicks(duration);
        return false;
    }

}
