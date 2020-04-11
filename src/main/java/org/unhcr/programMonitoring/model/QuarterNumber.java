package org.unhcr.programMonitoring.model;

public enum QuarterNumber {
	I("I"),
	II("II"),
	III("III"),
	IV("IV");

	private String label;

	private QuarterNumber(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}