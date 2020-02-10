package org.unhcr.programMonitoring.model;

public enum DisaggregationType {
	NONE("SIN DESAGREGACIÓN"),
	GENDER("GÉNERO"),
	AGE("EDAD"),
	LOCATION("LOCALIZACIÓN"),
	GENDER_AGE("GÉNERO Y EDAD"),
	GENDER_LOCATION("GÉNERO Y LOCALIZACIÓN"),
	AGE_LOCATION("EDAD Y LOCALIZACIÓN"),
	GENDER_AGE_LOCATION("GÉNERO, EDAD Y LOCALIZACIÓN");

	private String label;

	private DisaggregationType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}