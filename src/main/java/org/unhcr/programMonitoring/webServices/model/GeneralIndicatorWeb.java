package org.unhcr.programMonitoring.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.DisaggregationType;

public class GeneralIndicatorWeb {

    public GeneralIndicatorWeb() {
    }

    public GeneralIndicatorWeb(Long id, Boolean parent, String description, DisaggregationType disaggregationType, Integer target,
                               Integer totalExecution, Integer executionPercentage,
                               State state, PeriodWeb period) {
        this.id = id;
        this.parent = parent;
        this.description = description;
        this.disaggregationType = disaggregationType;
        this.target = target;
        this.totalExecution = totalExecution;
        this.executionPercentage = executionPercentage;
        this.state = state;
        this.period = period;
    }

    private Long id;

    private Boolean parent;

    private String description;

    private DisaggregationType disaggregationType;
    private Integer target;
    private Integer totalExecution;
    private Integer executionPercentage;

    private State state;
    private PeriodWeb period;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getParent() {
        return parent;
    }

    public void setParent(Boolean parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DisaggregationType getDisaggregationType() {
        return disaggregationType;
    }

    public void setDisaggregationType(DisaggregationType disaggregationType) {
        this.disaggregationType = disaggregationType;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(Integer totalExecution) {
        this.totalExecution = totalExecution;
    }

    public Integer getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(Integer executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }
}
