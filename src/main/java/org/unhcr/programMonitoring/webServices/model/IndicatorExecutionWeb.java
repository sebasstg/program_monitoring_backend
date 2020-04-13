package org.unhcr.programMonitoring.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.DisaggregationType;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.MeasureType;

import java.util.ArrayList;
import java.util.List;

public class IndicatorExecutionWeb {

    public IndicatorExecutionWeb() {
    }

    public IndicatorExecutionWeb(Long id, String attachmentDescription,
                                 ProjectWeb project, PerformanceIndicatorWeb performanceIndicator,
                                 OutputWeb output, GeneralIndicatorWeb generalIndicator,
                                 State state, DisaggregationType disaggregationType,
                                 Integer target, Integer totalExecution,
                                 Integer executionPercentage, IndicatorType indicatorType,
                                 List<IndicatorExecutionLocationAssigmentWeb> indicatorExecutionLocationAssigment, SituationWeb situation, MeasureType measureType, List<QuarterWeb> quarterWebs) {
        this.id = id;
        this.attachmentDescription = attachmentDescription;
        this.project = project;
        this.performanceIndicator = performanceIndicator;
        this.output = output;
        this.generalIndicator = generalIndicator;
        this.state = state;
        this.disaggregationType = disaggregationType;
        this.target = target;
        this.totalExecution = totalExecution;
        this.executionPercentage = executionPercentage;
        this.indicatorType = indicatorType;
        this.indicatorExecutionLocationAssigment = indicatorExecutionLocationAssigment;
        this.situation = situation;
        this.measureType = measureType;
        this.quarters=quarterWebs;
    }

    private Long id;
    private String attachmentDescription;
    private ProjectWeb project;
    private PerformanceIndicatorWeb performanceIndicator;
    private OutputWeb output;
    private GeneralIndicatorWeb generalIndicator;
    private State state;
    private DisaggregationType disaggregationType;
    private Integer target;
    private Integer totalExecution;
    private Integer executionPercentage;
    private IndicatorType indicatorType;
    private List<IndicatorExecutionLocationAssigmentWeb> indicatorExecutionLocationAssigment;
    private SituationWeb situation;
    private MeasureType measureType;
    private List<QuarterWeb> quarters = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public ProjectWeb getProject() {
        return project;
    }

    public void setProject(ProjectWeb project) {
        this.project = project;
    }

    public PerformanceIndicatorWeb getPerformanceIndicator() {
        return performanceIndicator;
    }

    public void setPerformanceIndicator(PerformanceIndicatorWeb performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }

    public OutputWeb getOutput() {
        return output;
    }

    public void setOutput(OutputWeb output) {
        this.output = output;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public List<IndicatorExecutionLocationAssigmentWeb> getIndicatorExecutionLocationAssigment() {
        return indicatorExecutionLocationAssigment;
    }

    public void setIndicatorExecutionLocationAssigment(List<IndicatorExecutionLocationAssigmentWeb> indicatorExecutionLocationAssigment) {
        this.indicatorExecutionLocationAssigment = indicatorExecutionLocationAssigment;
    }

    public SituationWeb getSituation() {
        return situation;
    }

    public void setSituation(SituationWeb situation) {
        this.situation = situation;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public GeneralIndicatorWeb getGeneralIndicator() {
        return generalIndicator;
    }

    public void setGeneralIndicator(GeneralIndicatorWeb generalIndicator) {
        this.generalIndicator = generalIndicator;
    }

    public List<QuarterWeb> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<QuarterWeb> quarters) {
        this.quarters = quarters;
    }
}
