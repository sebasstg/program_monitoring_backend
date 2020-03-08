package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "periods")
@SqlResultSetMapping(
        name = "periodResumeWebMapping",
        classes = @ConstructorResult(
                targetClass = PeriodResumeWeb.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "state", type = String.class),
                        @ColumnResult(name = "year", type = Integer.class),
                        @ColumnResult(name = "numberOfProjects", type = Integer.class),
                        @ColumnResult(name = "numberOfAsignedIndicators", type = Integer.class)
                }
        )
)
public class Period extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", unique = true, nullable = false)
    private Integer year;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "period")
    private Set<Project> projects;

    @OneToMany(mappedBy = "period")
    private Set<PeriodPerformanceIndicatorAssigment> periodPerformanceIndicatorAssigments = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<PeriodPerformanceIndicatorAssigment> getPeriodPerformanceIndicatorAssigments() {
        return periodPerformanceIndicatorAssigments;
    }

    public void setPeriodPerformanceIndicatorAssigments(Set<PeriodPerformanceIndicatorAssigment> periodPerformanceIndicatorAssigments) {
        this.periodPerformanceIndicatorAssigments = periodPerformanceIndicatorAssigments;
    }
}