package com.sindercube.aileron.access;

import java.util.function.UnaryOperator;

public interface AileronPlayerEntity {

	default int getMaxSmokestacks() {
		return 0;
	}

	default int getSmokeStacks() {
		return 0;
	}

	default void setSmokeStacks(int stacks) {}

	default void modifySmokeStacks(UnaryOperator<Integer> function) {
		setSmokeStacks(function.apply(getSmokeStacks()));
	}


	default boolean charged() {
		return false;
	}

	default int getBoostTicks() {
		return 0;
	}

	default void setBoostTicks(int boostTicks) {}

	default void setSmokeTrailTicks(int boostTicks) {}

	default int getCampfireDamageIFrames() {
		return 0;
	}

	default void setCampfireDamageIFrames(int campfireDamageIFrames) {}

	default boolean canChargeSmokeStack() {
		return false;
	}

}
