package com.sindercube.aileron.access;

import java.util.function.UnaryOperator;

public interface AileronPlayerEntity {

	default int getMaxSmokeStacks() {
		return 0;
	}

	default int getSmokeStacks() {
		return 0;
	}
	default void setSmokeStacks(int stacks) {}
	default void modifySmokeStacks(UnaryOperator<Integer> function) {
		setSmokeStacks(function.apply(getSmokeStacks()));
	}

	default int getSmokeStackDashCooldown() {
		return 0;
	}
	default void setSmokeStackDashCooldown(int cooldown) {}

	default boolean isCampfireCharged() {
		return false;
	}
	default void setCampfireCharged(boolean charged) {}

	default int getCampfireChargeTime() {
		return 0;
	}
	default void setCampfireChargeTime(int time) {}
	default void modifyCampfireChargeTime(UnaryOperator<Integer> function) {
		setCampfireChargeTime(function.apply(getCampfireChargeTime()));
	}

	default void setFlightBoostTicks(int flightBoostTicks) {}
	default void setSmokeTrailTicks(int boostTicks) {}
	default void setFlyingTimer(int timer) {}

	default int getCampfireIFrames() {
		return 0;
	}
	default void setCampfireIFrames(int frames) {}

}
