package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "projects")
public class Project extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "reporting_starting_date", nullable = false)
    private LocalDateTime reportingStartingDate;

    @Column(name = "reporting_finishing_date", nullable = false)
    private LocalDateTime reportingFinishingDate;

    @Column(name = "state",  nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_projects_periods"))
    private Period period;

    @OneToMany(mappedBy = "project")
    private Set<SituationAssigment> situationAssigments = new HashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_implementor_id", foreignKey = @ForeignKey(name = "fk_projects_project_implementor"))
    private ProjectImplementer projectImplementer;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "project")
    private Set<IndicatorExecution> indicatorExecutions;

    @Override

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

    public LocalDateTime getReportingStartingDate() {
        return reportingStartingDate;
    }

    public void setReportingStartingDate(LocalDateTime reportingStartingDate) {
        this.reportingStartingDate = reportingStartingDate;
    }

    public LocalDateTime getReportingFinishingDate() {
        return reportingFinishingDate;
    }

    public void setReportingFinishingDate(LocalDateTime reportingFinishingDate) {
        this.reportingFinishingDate = reportingFinishingDate;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Set<SituationAssigment> getSituationAssigments() {
        return situationAssigments;
    }

    public void setSituationAssigments(Set<SituationAssigment> situationAssigments) {
        this.situationAssigments = situationAssigments;
    }

    public ProjectImplementer getProjectImplementer() {
        return projectImplementer;
    }

    public void setProjectImplementer(ProjectImplementer projectImplementer) {
        this.projectImplementer = projectImplementer;
    }

    public Set<IndicatorExecution> getIndicatorExecutions() {
        return indicatorExecutions;
    }

    public void setIndicatorExecutions(Set<IndicatorExecution> indicatorExecutions) {
        this.indicatorExecutions = indicatorExecutions;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}