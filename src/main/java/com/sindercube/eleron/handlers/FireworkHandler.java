package com.sindercube.eleron.handlers;

import com.sindercube.eleron.registry.EleronGamerules;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class FireworkHandler {

    private static final FireworksComponent FALLBACK_COMPONENT = new FireworksComponent(1, List.of());
    private static final int DURATION_MULTIPLIER = 60;

    public static void handleBoosting(ServerWorld world, PlayerEntity player, ProjectileEntity firework, ItemStack stack) {
        int cooldown = world.getGameRules().getInt(EleronGamerules.FIREWORK_BOOST_COOLDOWN);
        if (cooldown > 0) player.getItemCooldownManager().set(stack, cooldown);

        if (world.getGameRules().getBoolean(EleronGamerules.FIREWORK_BOOSTS_FLIGHT)) {
			world.spawnEntity(firework);
			return;
		}

        int duration = stack.getOrDefault(DataComponentTypes.FIREWORKS, FALLBACK_COMPONENT).flightDuration() * DURATION_MULTIPLIER;
        player.setSmokeTrailTicks(duration);
	}

}
