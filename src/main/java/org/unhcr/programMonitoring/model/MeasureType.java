package org.unhcr.programMonitoring.model;

public enum MeasureType {
	INTEGER("NÚMEROS ENTEROS"),
	YES_NO("SI_NO");
	private String label;

	private MeasureType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}