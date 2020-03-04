package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;

public class OutputWeb {

    public OutputWeb() {
    }

    public OutputWeb(Long id, String code, String description, State state, ObjetiveWeb objetiveWeb) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.state = state;
        this.objetiveWeb = objetiveWeb;
    }

    private Long id;

    private String code;

    private String description;

    private State state;

    @JsonProperty("objetive")
    private ObjetiveWeb objetiveWeb;

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

    public ObjetiveWeb getObjetiveWeb() {
        return objetiveWeb;
    }

    public void setObjetiveWeb(ObjetiveWeb objetiveWeb) {
        this.objetiveWeb = objetiveWeb;
    }
}
