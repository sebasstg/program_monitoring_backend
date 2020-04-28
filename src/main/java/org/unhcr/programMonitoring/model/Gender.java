package org.unhcr.programMonitoring.model;

public enum Gender {
	MALE("MASCULINO",1),
	FEMALE("FEMENINO",2),
	OTHER("OTROS",3);

	private String label;

	private int order;


	Gender(String label, int order) {
		this.label = label;
		this.order = order;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}