package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.PerformanceIndicator;

public class PerformanceIndicatorWeb {

    public PerformanceIndicatorWeb() {
    }

    public PerformanceIndicatorWeb(Long id,  String description, State state, IndicatorType indicatorType, OutputWeb outputWeb) {
        this.id = id;
        this.indicatorType = indicatorType;
        this.description = description;
        this.state = state;
        this.outputWeb = outputWeb;
    }

    private Long id;


    private String description;

    private State state;

    private IndicatorType indicatorType;

    @JsonProperty("output")
    private OutputWeb outputWeb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }
}
