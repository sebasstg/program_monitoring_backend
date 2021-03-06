package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(schema = "program_monitoring", name = "indicator_execution_location_assigments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"indicator_execution_id", "canton_id"}))
public class IndicatorExecutionLocationAssigment extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_execution_id",nullable = false,foreignKey = @ForeignKey(name = "fk_project_per_ind_assig_proj_location"))
    private IndicatorExecution indicatorExecution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id",nullable = false,foreignKey = @ForeignKey(name = "fk_project_per_ind_assig_proj_location_canton"))
    private Canton location;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
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

        if (!(o instanceof IndicatorExecutionLocationAssigment)) return false;

        IndicatorExecutionLocationAssigment that = (IndicatorExecutionLocationAssigment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))

                .append(indicatorExecution, that.indicatorExecution)
                .append(location, that.location)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(indicatorExecution)
                .append(location)
                .toHashCode();
    }
}