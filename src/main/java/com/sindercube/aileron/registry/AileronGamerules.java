package com.sindercube.aileron.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class AileronGamerules {

    public static void init() {}


    public static final GameRules.Key<GameRules.BooleanRule> FIREWORK_BOOSTS_FLIGHT =
            register("fireworkBoostsFlight", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.IntRule> FIREWORK_BOOST_COOLDOWN =
            register("fireworkBoostCooldown", GameRules.Category.MOBS, GameRuleFactory.createIntRule(200));

    public static final GameRules.Key<GameRules.BooleanRule> CAMPFIRES_UPDRAFTS =
            register("campfireUpdrafts", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.IntRule> SMOKE_STACK_CHARGE_TICKS =
            register("smokeStackChargeTicks", GameRules.Category.MOBS, GameRuleFactory.createIntRule(20));


    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }

}
