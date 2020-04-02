package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateDeserializer;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class ProjectResumeWeb {

    public ProjectResumeWeb() {
    }



    private Long id;


    private ProjectImplementerWeb projectImplementer;

    private String name;

    private BigDecimal progressPercentaje;

    private Integer reportedProgress;

    private List<SituationWeb> situations;

    private String lastReportedMonth;

    private Integer target;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate reportingStartingDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate reportingFinishingDate;

    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectImplementerWeb getProjectImplementer() {
        return projectImplementer;
    }

    public void setProjectImplementer(ProjectImplementerWeb projectImplementer) {
        this.projectImplementer = projectImplementer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProgressPercentaje() {
        return progressPercentaje;
    }

    public void setProgressPercentaje(BigDecimal progressPercentaje) {
        this.progressPercentaje = progressPercentaje;
    }

    public Integer getReportedProgress() {
        return reportedProgress;
    }

    public void setReportedProgress(Integer reportedProgress) {
        this.reportedProgress = reportedProgress;
    }

    public List<SituationWeb> getSituations() {
        return situations;
    }

    public void setSituations(List<SituationWeb> situations) {
        this.situations = situations;
    }

    public String getLastReportedMonth() {
        return lastReportedMonth;
    }

    public void setLastReportedMonth(String lastReportedMonth) {
        this.lastReportedMonth = lastReportedMonth;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public LocalDate getReportingStartingDate() {
        return reportingStartingDate;
    }

    public void setReportingStartingDate(LocalDate reportingStartingDate) {
        this.reportingStartingDate = reportingStartingDate;
    }

    public LocalDate getReportingFinishingDate() {
        return reportingFinishingDate;
    }

    public void setReportingFinishingDate(LocalDate reportingFinishingDate) {
        this.reportingFinishingDate = reportingFinishingDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
