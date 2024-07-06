package com.sindercube.aileron.access;

import com.sindercube.aileron.content.packets.SmokeStackDashPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.function.UnaryOperator;

public interface AileronPlayerEntity {

	default void smokeStackDash(SmokeStackDashPacket packet, ServerPlayNetworking.Context context) {}


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


	default boolean isWearingElytra() {
		return false;
	}


	default boolean charged() {
		return false;
	}

	default int getBoostTicks() {
		return 0;
	}

	default void setBoostTicks(int boostTicks) {}

	default void setSmokeTrailTicks(int boostTicks) {}

	default int getCampfireDamageInvulnerability() {
		return 0;
	}

	default void setCampfireDamageInvulnerability(int campfireDamageIFrames) {}

	default boolean canChargeSmokeStack() {
		return false;
	}

}
