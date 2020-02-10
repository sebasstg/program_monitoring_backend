package org.unhcr.programMonitoring.model;

public enum AgeGroup {
	CHILDREN("NIÑAS Y NIÑOS"),
	TEENS("ADOLECENTES"),
	YOUNG_ADULTS("JÓVENES ADULTOS"),
	ADULTS("ADULTOS"),
	ELDERLY("ADULTOS MAYORES");

	private String label;

	private AgeGroup(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}