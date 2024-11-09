package com.sindercube.eleron.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class EleronGamerules {

    public static void init() {}

    public static final GameRules.Key<GameRules.BooleanRule> FIREWORK_BOOSTS_FLIGHT = register("fireworkBoostsFlight",
		GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false)
	);
    public static final GameRules.Key<GameRules.IntRule> FIREWORK_BOOST_COOLDOWN = register("fireworkBoostCooldown",
		GameRules.Category.PLAYER, GameRuleFactory.createIntRule(0)
	);
    public static final GameRules.Key<GameRules.BooleanRule> CAMPFIRES_UPDRAFTS = register("campfireUpdrafts",
		GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true)
	);
    public static final GameRules.Key<GameRules.IntRule> SMOKESTACK_CHARGE_TICKS = register("smokestackChargeTicks",
		GameRules.Category.PLAYER, GameRuleFactory.createIntRule(30, 1)
	);
	public static final GameRules.Key<GameRules.BooleanRule> DO_ELYTRA_BOUNCE = register("doElytraBounce",
		GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false)
	);

    public static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }

}
