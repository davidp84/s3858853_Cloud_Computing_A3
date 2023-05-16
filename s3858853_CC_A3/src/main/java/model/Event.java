package model;

public enum Event {

	FLAT_TYRE, FLAT_BATTERY, NO_PETROL, MECHANICAL, UNKNOWN;

	@Override
	public String toString() {
		return String.format("%C%s", name().toString().charAt(0), name().toString().toLowerCase().substring(1));
	}
}
