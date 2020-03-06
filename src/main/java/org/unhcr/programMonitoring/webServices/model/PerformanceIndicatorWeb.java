package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;

public class PerformanceIndicatorWeb {

    public PerformanceIndicatorWeb() {
    }

    public PerformanceIndicatorWeb(Long id, String code, String description, State state, OutputWeb outputWeb) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.state = state;
        this.outputWeb = outputWeb;
    }

    private Long id;

    private String code;

    private String description;

    private State state;

    @JsonProperty("output")
    private OutputWeb outputWeb;

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

    public OutputWeb getOutputWeb() {
        return outputWeb;
    }

    public void setOutputWeb(OutputWeb outputWeb) {
        this.outputWeb = outputWeb;
    }
}
