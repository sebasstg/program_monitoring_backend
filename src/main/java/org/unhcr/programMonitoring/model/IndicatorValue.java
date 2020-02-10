package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "program_monitoring", name = "indicator_values")
public class IndicatorValue extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age_group")
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id", foreignKey = @ForeignKey(name = "fk_indicator_values_cantones"))
    private Canton location;

    @Column(name = "value")
    private BigDecimal value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_execution_id", foreignKey = @ForeignKey(name = "fk_performance_indicator_execution_value"))
    private IndicatorExecution indicatorExecution;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
    }
}