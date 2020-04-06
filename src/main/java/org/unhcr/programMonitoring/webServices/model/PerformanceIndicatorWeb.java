package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.MeasureType;
import org.unhcr.programMonitoring.model.PercentageType;

public class PerformanceIndicatorWeb {

    public PerformanceIndicatorWeb() {
    }

    public PerformanceIndicatorWeb(Long id,  String description, State state,
                                   IndicatorType indicatorType, OutputWeb outputWeb, MeasureType measureType,
                                   PercentageType percentageType,
                                   PerformanceIndicatorWeb numerator, PerformanceIndicatorWeb denominator) {
        this.id = id;
        this.indicatorType = indicatorType;
        this.description = description;
        this.state = state;
        this.outputWeb = outputWeb;
        this.measureType=measureType;
        this.percentageType=percentageType;
        this.numerator=numerator;
        this.denominator=denominator;
    }

    private Long id;


    private String description;

    private State state;

    private IndicatorType indicatorType;

    @JsonProperty("output")
    private OutputWeb outputWeb;

    private MeasureType measureType;

    private PercentageType percentageType;

    private PerformanceIndicatorWeb numerator;
    private PerformanceIndicatorWeb denominator;



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

    public PercentageType getPercentageType() {
        return percentageType;
    }

    public void setPercentageType(PercentageType percentageType) {
        this.percentageType = percentageType;
    }


    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public PerformanceIndicatorWeb getNumerator() {
        return numerator;
    }

    public void setNumerator(PerformanceIndicatorWeb numerator) {
        this.numerator = numerator;
    }

    public PerformanceIndicatorWeb getDenominator() {
        return denominator;
    }

    public void setDenominator(PerformanceIndicatorWeb denominator) {
        this.denominator = denominator;
    }
}
