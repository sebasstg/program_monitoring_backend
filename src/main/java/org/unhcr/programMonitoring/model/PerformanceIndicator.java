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
}