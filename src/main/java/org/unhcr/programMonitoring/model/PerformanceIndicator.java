package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;

@Entity
@Table(schema = "program_monitoring", name = "performance_indicators")
public class PerformanceIndicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "state", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "indicator_type", nullable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "output_id",foreignKey = @ForeignKey(name = "fk_performance_indicators_outputs"))
    Output output;

    @Column(name = "measure_type",nullable = true)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Column(name = "percentage_type",nullable = true)
    @Enumerated(EnumType.STRING)
    private PercentageType percentageType;

    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "numerator_id",foreignKey = @ForeignKey(name = "fk_performance_indicator_indicator_numerator"))
    private PerformanceIndicator numerator;

    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "denominator_id",foreignKey = @ForeignKey(name = "fk_performance_indicator_indicator_denominator"))
    private PerformanceIndicator denominator;



    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public PercentageType getPercentageType() {
        return percentageType;
    }

    public void setPercentageType(PercentageType percentageType) {
        this.percentageType = percentageType;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public PerformanceIndicator getNumerator() {
        return numerator;
    }

    public void setNumerator(PerformanceIndicator numerator) {
        this.numerator = numerator;
    }

    public PerformanceIndicator getDenominator() {
        return denominator;
    }

    public void setDenominator(PerformanceIndicator denominator) {
        this.denominator = denominator;
    }
}