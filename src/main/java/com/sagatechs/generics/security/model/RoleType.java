package com.sagatechs.generics.security.model;

public enum RoleType {

    SUPER_ADMINISTRADOR("Super Administrador"),
	ADMINISTRADOR("Administrador"),
	MONITOR_DE_PROGRAMAS("Monitor de Programas"),
	EJECUTOR_PROYECTOS("Ejecutaro de Proyectos");


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
