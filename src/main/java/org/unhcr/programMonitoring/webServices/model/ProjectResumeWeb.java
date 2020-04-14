package org.unhcr.programMonitoring.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.math.BigDecimal;
import java.util.List;


public class ProjectResumeWeb {

    public ProjectResumeWeb() {
    }



    private Long id;


    private ProjectImplementerWeb projectImplementer;

    private String code;

    private String name;

    private BigDecimal progressPercentaje;

    private Integer reportedProgress;

    private List<SituationWeb> situations;

    private String lastReportedMonth;

    private Integer target;

    private State state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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



    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
