package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateDeserializer;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;


public class ProjectWeb {

    public ProjectWeb() {
    }

    public ProjectWeb(Long id, String periodImplementerCode, String name, LocalDate reportingStartingDate, LocalDate reportingFinishingDate, State state, PeriodWeb periodWeb, ProjectImplementerWeb projectImplementerWeb) {
        this.id = id;
        this.name = name;
        this.reportingStartingDate = reportingStartingDate;
        this.reportingFinishingDate = reportingFinishingDate;
        this.state = state;
        this.periodWeb = periodWeb;
        this.projectImplementerWeb = projectImplementerWeb;
        this.periodImplementerCode=periodImplementerCode;
    }

    private Long id;

    private String name;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate reportingStartingDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate reportingFinishingDate;

    private State state;

    @JsonProperty("period")
    private PeriodWeb periodWeb;

    @JsonProperty("projectImplementer")
    private ProjectImplementerWeb projectImplementerWeb;

    @JsonProperty("situations")
    private List<SituationWeb> situationWeb;

    private String periodImplementerCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public PeriodWeb getPeriodWeb() {
        return periodWeb;
    }

    public void setPeriodWeb(PeriodWeb periodWeb) {
        this.periodWeb = periodWeb;
    }

    public ProjectImplementerWeb getProjectImplementerWeb() {
        return projectImplementerWeb;
    }

    public void setProjectImplementerWeb(ProjectImplementerWeb projectImplementerWeb) {
        this.projectImplementerWeb = projectImplementerWeb;
    }

    public List<SituationWeb> getSituationWeb() {
        return situationWeb;
    }

    public void setSituationWeb(List<SituationWeb> situationWeb) {
        this.situationWeb = situationWeb;
    }

    public String getPeriodImplementerCode() {
        return periodImplementerCode;
    }

    public void setPeriodImplementerCode(String periodImplementerCode) {
        this.periodImplementerCode = periodImplementerCode;
    }
}
