package org.unhcr.programMonitoring.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;

public class PeriodResumeWeb extends PeriodWeb{

    public PeriodResumeWeb() {
    }

    public PeriodResumeWeb(Long id, String state, Integer year, Integer numberOfProjects, Integer numberOfAsignedIndicators) {
        State stateE=null;
        if(StringUtils.isNotBlank(state)){
            stateE=State.valueOf(state);
        }

        this.id=id;
        this.year=year;
        this.state=stateE;
        this.numberOfProjects = numberOfProjects;
        this.numberOfAsignedIndicators = numberOfAsignedIndicators;
    }

    private Integer numberOfProjects;

    private Integer numberOfAsignedIndicators;

    public Integer getNumberOfProjects() {
        return numberOfProjects;
    }

    public void setNumberOfProjects(Integer numberOfProjects) {
        this.numberOfProjects = numberOfProjects;
    }

    public Integer getNumberOfAsignedIndicators() {
        return numberOfAsignedIndicators;
    }

    public void setNumberOfAsignedIndicators(Integer numberOfAsignedIndicators) {
        this.numberOfAsignedIndicators = numberOfAsignedIndicators;
    }
}
