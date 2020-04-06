package org.unhcr.programMonitoring.model;

public enum PercentageType {
	NUMERATOR("NUMERADOR"),
	DENOMINATOR("DENOMINADOR"),
	PERCENTAGE("PORCENTAJE");


	private String label;

	private PercentageType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}