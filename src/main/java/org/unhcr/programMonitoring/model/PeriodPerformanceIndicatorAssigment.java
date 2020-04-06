package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;

@Entity
@Table(schema = "program_monitoring", name = "period_performance_indicator_assigments", uniqueConstraints = @UniqueConstraint(columnNames = {"period_id","performance_indicator_id"}))
public class PeriodPerformanceIndicatorAssigment extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "disaggregation_type",  nullable = false)
    @Enumerated(EnumType.STRING)
    private DisaggregationType disaggregationType;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "period_id",foreignKey = @ForeignKey(name = "fk_period_performance_indicator_assigment_period"))
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "performance_indicator_id",foreignKey = @ForeignKey(name = "fk_period_performance_indicator_assigment_perf_indicator"))
    private PerformanceIndicator performanceIndicator;

    @Column(name = "state",  nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;


    @Column(name = "measure_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Column(name = "percentage_type",nullable = true)
    @Enumerated(EnumType.STRING)
    private PercentageType percentageType;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "period_performance_indicator_assigment_id",foreignKey = @ForeignKey(name = "fk_period_performance_indicator_assigment_indicator"))
    private PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
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

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public PercentageType getPercentageType() {
        return percentageType;
    }

    public void setPercentageType(PercentageType percentageType) {
        this.percentageType = percentageType;
    }

    public PeriodPerformanceIndicatorAssigment getPeriodPerformanceIndicatorAssigment() {
        return periodPerformanceIndicatorAssigment;
    }

    public void setPeriodPerformanceIndicatorAssigment(PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment) {
        this.periodPerformanceIndicatorAssigment = periodPerformanceIndicatorAssigment;
    }
}