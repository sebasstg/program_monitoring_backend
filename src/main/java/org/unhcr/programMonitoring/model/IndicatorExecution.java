package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "indicator_execution")
public class IndicatorExecution extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attachment_description", nullable = false,columnDefinition = "text")
    private String attachmentDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",nullable = false,foreignKey = @ForeignKey(name = "fk_indicator_execution_project"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "performance_indicator_id",foreignKey = @ForeignKey(name = "fk_indicator_executions_performance_indicators"))
    private PerformanceIndicator performanceIndicator;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "output_id",foreignKey = @ForeignKey(name = "fk_indicator_execution_performance_indicator"))
    private Output output;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "general_indicator_id",foreignKey = @ForeignKey(name = "fk_indicator_execution_general_indicator"))
    private GeneralIndicator generalIndicator;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "disaggregation_type", nullable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private DisaggregationType disaggregationType;

    @Column(name = "target")
    private Integer target;

    @Column(name = "total_execution", nullable = false)
    private Integer totalExecution;

    @Column(name = "execution_percentage", nullable = false)
    private Integer executionPercentage;

    @Column(name = "indicator_type", nullable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;

    @OneToMany(mappedBy = "indicatorExecution")
    private Set<IndicatorExecutionLocationAssigment> performanceIndicatorExecutionLocationAssigments = new HashSet<>();

    @OneToMany(mappedBy = "indicatorExecution")
    private Set<IndicatorValue> indicatorValues= new HashSet<>();

    @ManyToOne(optional = true)
    @JoinColumn(name = "situation_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_situation"))
    private Situation situation;

    @Column(name = "measure_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public PerformanceIndicator getPerformanceIndicator() {
        return performanceIndicator;
    }

    public void setPerformanceIndicator(PerformanceIndicator performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public DisaggregationType getDisaggregationType() {
        return disaggregationType;
    }

    public void setDisaggregationType(DisaggregationType disaggregationType) {
        this.disaggregationType = disaggregationType;
    }

    public Set<IndicatorExecutionLocationAssigment> getPerformanceIndicatorExecutionLocationAssigments() {
        return performanceIndicatorExecutionLocationAssigments;
    }

    public void setPerformanceIndicatorExecutionLocationAssigments(Set<IndicatorExecutionLocationAssigment> performanceIndicatorExecutionLocationAssigments) {
        this.performanceIndicatorExecutionLocationAssigments = performanceIndicatorExecutionLocationAssigments;
    }

    public Set<IndicatorValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(Set<IndicatorValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public GeneralIndicator getGeneralIndicator() {
        return generalIndicator;
    }

    public void setGeneralIndicator(GeneralIndicator generalIndicator) {
        this.generalIndicator = generalIndicator;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
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

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }
}