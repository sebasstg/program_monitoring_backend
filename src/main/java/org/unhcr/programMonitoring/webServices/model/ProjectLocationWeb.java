package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;


public class ProjectLocationWeb {

    public ProjectLocationWeb() {
    }

    public ProjectLocationWeb(Long id, CantonWeb canton, State state) {
        this.id = id;
        this.canton = canton;
        this.state = state;
    }

    private Long id;

    private CantonWeb canton;

    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CantonWeb getCanton() {
        return canton;
    }

    public void setCanton(CantonWeb canton) {
        this.canton = canton;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ProjectLocationWeb)) return false;

        ProjectLocationWeb that = (ProjectLocationWeb) o;

        return new EqualsBuilder()
                .append(canton.getId(), that.canton.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(canton.getId())
                .toHashCode();
    }
}
