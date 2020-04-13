package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;

import java.util.List;


public class ProjectWeb {

    public ProjectWeb() {
    }

    public ProjectWeb(Long id, String code, String name,  State state, PeriodWeb periodWeb, ProjectImplementerWeb projectImplementerWeb, Integer target) {
        this.id = id;
        this.name = name;
         this.state = state;
        this.periodWeb = periodWeb;
        this.projectImplementerWeb = projectImplementerWeb;
        this.code=code;
        this.target=target;
    }

    private Long id;


    private String code;

    private String name;


    private State state;

    private Integer target;

    @JsonProperty("period")
    private PeriodWeb periodWeb;

    @JsonProperty("projectImplementer")
    private ProjectImplementerWeb projectImplementerWeb;

    @JsonProperty("situations")
    private List<SituationWeb> situationWeb;

    private List<ProjectLocationWeb> projectLocations;

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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProjectLocationWeb> getProjectLocations() {
        return projectLocations;
    }

    public void setProjectLocations(List<ProjectLocationWeb> projectLocations) {
        this.projectLocations = projectLocations;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
