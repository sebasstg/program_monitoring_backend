package org.unhcr.programMonitoring.model;

public enum AgeGroup {
	CHILDREN("NIÑAS Y NIÑOS",1),
	TEENS("ADOLECENTES",2),
	YOUNG_ADULTS("JÓVENES ADULTOS",3),
	ADULTS("ADULTOS",4),
	ELDERLY("ADULTOS MAYORES",5),;

	private String label;
	private int order;

	AgeGroup(String label, int order) {
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