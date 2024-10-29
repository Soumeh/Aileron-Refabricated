package com.sindercube.eleron.access;

public interface EleronPlayerEntity {

	default int getMaxSmokestackCharges() {
		return 0;
	}

	default int getSmokestackCharges() {
		return 0;
	}
	default void setSmokestackCharges(int stacks) {}
	default void addSmokestackCharge() {
		setSmokestackCharges(getSmokestackCharges() + 1);
	}
	default void useSmokestackCharge() {
		setSmokestackCharges(getSmokestackCharges() - 1);
	}

	default int getSmokestackChargeCooldown() {
		return 0;
	}
	default void setSmokestackChargeCooldown(int cooldown) {}

	default int getSmokeTrailTicks() {
		return 0;
	}
	default void setSmokeTrailTicks(int boostTicks) {}

	default int getCampfireChargeTime() {
		return 0;
	}
	default void setCampfireChargeTime(int time) {}
	default void tickCampfireChargeTime() {
		setCampfireChargeTime(getCampfireChargeTime() + 1);
	}

	default int getFlightBoostTicks() {
		return 0;
	}
	default void setFlightBoostTicks(int flightBoostTicks) {}

}
