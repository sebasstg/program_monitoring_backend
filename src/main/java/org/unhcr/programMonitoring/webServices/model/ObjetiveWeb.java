package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;

public class ObjetiveWeb {

    public ObjetiveWeb() {
    }

    public ObjetiveWeb(Long id, String code, String description, State state, RightGroupWeb rightGroupWeb) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.state = state;
        this.rightGroupWeb = rightGroupWeb;
    }

    private Long id;

    private String code;

    private String description;

    private State state;

    @JsonProperty("rightGroup")
    private RightGroupWeb rightGroupWeb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public RightGroupWeb getRightGroupWeb() {
        return rightGroupWeb;
    }

    public void setRightGroupWeb(RightGroupWeb rightGroupWeb) {
        this.rightGroupWeb = rightGroupWeb;
    }
}
