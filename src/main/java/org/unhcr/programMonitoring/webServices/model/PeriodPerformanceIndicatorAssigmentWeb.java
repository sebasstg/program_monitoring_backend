package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.DisaggregationType;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.MeasureType;

public class PeriodPerformanceIndicatorAssigmentWeb {


    public PeriodPerformanceIndicatorAssigmentWeb() {
    }

    public PeriodPerformanceIndicatorAssigmentWeb(Long id, DisaggregationType disaggregationType, PerformanceIndicatorWeb performanceIndicatorWeb,
                                                  State state, MeasureType measureType, PeriodWeb periodWeb, IndicatorType indicatorType) {
        this.id = id;
        this.disaggregationType = disaggregationType;
        this.performanceIndicatorWeb = performanceIndicatorWeb;
        this.state = state;
        this.measureType = measureType;
        this.periodWeb=periodWeb;
        this.indicatorType=indicatorType;

    }


    private Long id;

    private DisaggregationType disaggregationType;

    @JsonProperty("performanceIndicator")
    private PerformanceIndicatorWeb performanceIndicatorWeb;

    private State state;


    private MeasureType measureType;

    @JsonProperty("period")
    private PeriodWeb periodWeb;

    private IndicatorType indicatorType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DisaggregationType getDisaggregationType() {
        return disaggregationType;
    }

    public void setDisaggregationType(DisaggregationType disaggregationType) {
        this.disaggregationType = disaggregationType;
    }

    public PerformanceIndicatorWeb getPerformanceIndicatorWeb() {
        return performanceIndicatorWeb;
    }

    public void setPerformanceIndicatorWeb(PerformanceIndicatorWeb performanceIndicatorWeb) {
        this.performanceIndicatorWeb = performanceIndicatorWeb;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public PeriodWeb getPeriodWeb() {
        return periodWeb;
    }

    public void setPeriodWeb(PeriodWeb periodWeb) {
        this.periodWeb = periodWeb;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }
}