package org.unhcr.programMonitoring.webServices.model;

import com.sagatechs.generics.persistence.model.State;

public class IndicatorExecutionLocationAssigmentWeb {

    public IndicatorExecutionLocationAssigmentWeb(Long id,  CantonWeb location, State state) {
        this.id = id;

        this.location = location;
        this.state = state;
    }

    private Long id;

    private CantonWeb location;
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CantonWeb getLocation() {
        return location;
    }

    public void setLocation(CantonWeb location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
