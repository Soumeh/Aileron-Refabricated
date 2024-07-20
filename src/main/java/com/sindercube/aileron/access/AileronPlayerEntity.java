package com.sindercube.aileron.access;

public interface AileronPlayerEntity {

	// TODO (maybe) replace with world.getGameRules().getInt(AileronGamerules.SMOKE_STACK_CHARGE_TICKS);
	int CHARGE_TICKS = 30;

	default int getMaxSmokeStacks() {
		return 0;
	}

	default int getSmokeStacks() {
		return 0;
	}
	default void setSmokeStacks(int stacks) {}
	default void addSmokeStack() {
		setSmokeStacks(getSmokeStacks() + 1);
	}
	default void removeSmokeStack() {
		setSmokeStacks(getSmokeStacks() - 1);
	}

	default int getSmokeStackDashCooldown() {
		return 0;
	}
	default void setSmokeStackDashCooldown(int cooldown) {}

	default int getSmokeTrailTicks() {
		return 0;
	}
	default void setSmokeTrailTicks(int boostTicks) {}

	default boolean isCampfireCharged() {
		return getCampfireChargeTime() >= CHARGE_TICKS;
	}
	default int getCampfireChargeTime() {
		return 0;
	}
	default void setCampfireChargeTime(int time) {}
	default void incrementCampfireChargeTime() {
		setCampfireChargeTime(getCampfireChargeTime() + 1);
	}

	default int getFlightBoostTicks() {
		return 0;
	}
	default void setFlightBoostTicks(int flightBoostTicks) {}

	default int getFlyingTimer() {
		return 0;
	}
	default void setFlyingTimer(int timer) {}

}
