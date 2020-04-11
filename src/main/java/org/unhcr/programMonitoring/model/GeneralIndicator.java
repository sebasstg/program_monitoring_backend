package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "general_indicators")
public class GeneralIndicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent",nullable = false)
    private Boolean parent;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;



    @Column(name = "disaggregation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DisaggregationType disaggregationType;

    @Column(name = "measure_type", nullable = true)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Column(name = "target")
    private Integer target;

    @Column(name = "total_execution")
    private Integer totalExecution;

    @Column(name = "execution_percentage")
    private Integer executionPercentage;

    @ManyToOne(optional = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "main_general_indicator_id",nullable = true,foreignKey = @ForeignKey(name = "fk_general_indicator_parent"))
    private GeneralIndicator mainIndicator;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "mainIndicator")
    private Set<GeneralIndicator> subGeneralIndicators = new HashSet<>();


    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "periodo_id",foreignKey = @ForeignKey(name = "fk_general_indicator_period"))
    private Period period;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getParent() {
        return parent;
    }

    public void setParent(Boolean parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public DisaggregationType getDisaggregationType() {
        return disaggregationType;
    }

    public void setDisaggregationType(DisaggregationType disaggregationType) {
        this.disaggregationType = disaggregationType;
    }

    public Integer getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(Integer totalExecution) {
        this.totalExecution = totalExecution;
    }

    public Integer getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(Integer executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    public GeneralIndicator getMainIndicator() {
        return mainIndicator;
    }

    public void setMainIndicator(GeneralIndicator mainIndicator) {
        this.mainIndicator = mainIndicator;
    }

    public Set<GeneralIndicator> getSubGeneralIndicators() {
        return subGeneralIndicators;
    }

    public void setSubGeneralIndicators(Set<GeneralIndicator> subGeneralIndicators) {
        this.subGeneralIndicators = subGeneralIndicators;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }
}
