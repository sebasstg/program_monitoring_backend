package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "projects")
public class Project extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "code")
    private String code;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<IndicatorExecution> indicatorExecutions = new HashSet<>();
    ;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<ProjectLocationAssigment> projectLocationAssigments = new HashSet<>();

    @Column(name = "target")
    private Integer target;


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

    public void addSituationAssigment(SituationAssigment situationAssigment) {

        situationAssigment.setProject(this);
        if (this.situationAssigments.add(situationAssigment)) {
            this.situationAssigments.remove(situationAssigment);
            this.situationAssigments.add(situationAssigment);
        }
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<ProjectLocationAssigment> getProjectLocationAssigments() {
        return projectLocationAssigments;
    }

    public void setProjectLocationAssigments(Set<ProjectLocationAssigment> projectLocationAssigments) {
        this.projectLocationAssigments = projectLocationAssigments;
    }

    public void addProjectLocationAssigment(ProjectLocationAssigment projectLocationAssigment) {
        projectLocationAssigment.setProject(this);
        if (!this.projectLocationAssigments.add(projectLocationAssigment)) {
            this.projectLocationAssigments.remove(projectLocationAssigment);
            this.projectLocationAssigments.add(projectLocationAssigment);
        }
    }

    public void addIndicatorExecution(IndicatorExecution indicatorExecution) {
        indicatorExecution.setProject(this);
        if (!this.indicatorExecutions.add(indicatorExecution)) {
            this.indicatorExecutions.remove(indicatorExecution);
            this.indicatorExecutions.add(indicatorExecution);
        }

    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}