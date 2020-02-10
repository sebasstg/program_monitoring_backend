package org.unhcr.programMonitoring.model;

public enum IndicatorType {
	FOCUS("FOCUS"),
	PROXY("PROXY"),
	GENERAL("GENERAL"),
	LOCATION("LOCALIZACIÓN") ;

	private String label;

	private IndicatorType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}