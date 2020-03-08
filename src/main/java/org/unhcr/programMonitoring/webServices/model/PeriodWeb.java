package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.PerformanceIndicator;

import java.util.ArrayList;
import java.util.List;

public class PeriodWeb {

    public PeriodWeb() {
    }

    public PeriodWeb(Long id, Integer year, State state) {
        this.id = id;
        this.year = year;
        this.state = state;
    }



    protected Long id;

    protected Integer year;

    protected State state;

    protected List<PerformanceIndicatorWeb> performanceIndicatorWebs= new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<PerformanceIndicatorWeb> getPerformanceIndicatorWebs() {
        return performanceIndicatorWebs;
    }

    public void setPerformanceIndicatorWebs(List<PerformanceIndicatorWeb> performanceIndicatorWebs) {
        this.performanceIndicatorWebs = performanceIndicatorWebs;
    }
}
