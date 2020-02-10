package com.sagatechs.generics.security.model;

public enum RoleType {

	ADMINISTRADOR("Administrador"), USUARIO("Usuario"),PASSENGER("Pasajero");



    RoleType(String label) {
		this.label = label;
	}

	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
