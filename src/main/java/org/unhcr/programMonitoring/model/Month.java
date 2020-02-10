package org.unhcr.programMonitoring.model;

public enum Month {
	JANUARY("ENERO"),
	FEBRUARY("FEBRERO"),
	MARCH("MARZO"),
	APRIL("ABRIL"),
	MAY("MAYO"),
	JUNE("JUNIO"),
	JULY("JULIO"),
	AUGUST("AGOSTO"),
	SEPTEMBER("SEPTIEMBRE"),
	OCTOBER("OCTUBRE"),
	NOVEMBER("NOVIEMBRE"),
	DECEMBER("DICIEMBRE");

	private String label;

	Month(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}