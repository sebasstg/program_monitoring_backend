package org.unhcr.programMonitoring.model;

public enum Month {
    JANUARY("ENERO", 1),
    FEBRUARY("FEBRERO", 2),
    MARCH("MARZO", 3),
    APRIL("ABRIL", 4),
    MAY("MAYO", 5),
    JUNE("JUNIO", 6),
    JULY("JULIO", 7),
    AUGUST("AGOSTO", 8),
    SEPTEMBER("SEPTIEMBRE", 9),
    OCTOBER("OCTUBRE", 10),
    NOVEMBER("NOVIEMBRE", 11),
    DECEMBER("DICIEMBRE", 12);

    private String label;
    private int order;

    Month(String label, int order) {
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