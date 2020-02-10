package org.unhcr.programMonitoring.model;

public enum Gender {
	MALE("MASCULINO"),
	FEMALE("FEMENINO");

	private String label;

	private Gender(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}