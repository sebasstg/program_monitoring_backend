package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "program_monitoring", name = "indicator_values",
        uniqueConstraints = @UniqueConstraint(columnNames = {"month", "gender","age_group","canton_id", "indicator_execution_id"})
)
public class IndicatorValue extends BaseEntity<Long> {
    public IndicatorValue() {
    }

    public IndicatorValue(Month month, Gender gender, AgeGroup ageGroup, Canton location, BigDecimal value, Quarter quarter) {
        this.month = month;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.location = location;
        this.value = value;
        this.quarter = quarter;
    }

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

    @Column(name = "denominator_value")
    private BigDecimal denominatorValue;

    @Column(name = "numerator_value")
    private BigDecimal numeratorValue;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_execution_id", foreignKey = @ForeignKey(name = "fk_performance_indicator_execution_value"))
    private IndicatorExecution indicatorExecution;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "quarter_id",foreignKey = @ForeignKey(name = "fk_indicator_value_quarter"))
    private Quarter quarter;

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

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getDenominatorValue() {
        return denominatorValue;
    }

    public void setDenominatorValue(BigDecimal denominatorValue) {
        this.denominatorValue = denominatorValue;
    }

    public BigDecimal getNumeratorValue() {
        return numeratorValue;
    }

    public void setNumeratorValue(BigDecimal numeratorValue) {
        this.numeratorValue = numeratorValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IndicatorValue that = (IndicatorValue) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, that.id)
                .append(month, that.month)
                .append(gender, that.gender)
                .append(ageGroup, that.ageGroup)
                .append(location, that.location)
                .append(value, that.value)
                .append(indicatorExecution, that.indicatorExecution)
                .append(quarter, that.quarter)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(month)
                .append(gender)
                .append(ageGroup)
                .append(location)
                .append(value)
                .append(indicatorExecution)
                .append(quarter)
                .toHashCode();
    }
}