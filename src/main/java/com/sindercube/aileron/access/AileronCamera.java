package com.sindercube.aileron.access;

public interface AileronCamera {

	default double getPreviousEMAValue() {
		return 0;
	}

	default void setPreviousEMAValue(float previousEMA) {}

	default double getEMAValue() {
		return 0;
	}

	default void setEMAValue(float EMA) {}

	default float getSmoothedEMADifference() {
		return 0;
	}

}
