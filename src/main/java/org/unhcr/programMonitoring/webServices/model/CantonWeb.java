package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;

public class CantonWeb {

    public CantonWeb() {
    }

    public CantonWeb(Long id, String code, String description, State state, ProvinciaWeb provinciaWeb) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.state = state;
        this.provinciaWeb = provinciaWeb;
    }

    private Long id;

    private String code;

    private String description;

    private State state;

    @JsonProperty("provincia")
    private ProvinciaWeb provinciaWeb;

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

    public ProvinciaWeb getProvinciaWeb() {
        return provinciaWeb;
    }

    public void setProvinciaWeb(ProvinciaWeb provinciaWeb) {
        this.provinciaWeb = provinciaWeb;
    }
}
