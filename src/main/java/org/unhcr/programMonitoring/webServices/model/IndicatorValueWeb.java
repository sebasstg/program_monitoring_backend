package org.unhcr.programMonitoring.webServices.model;

import org.unhcr.programMonitoring.model.AgeGroup;
import org.unhcr.programMonitoring.model.Gender;
import org.unhcr.programMonitoring.model.Month;

import java.math.BigDecimal;

public class IndicatorValueWeb {

    public IndicatorValueWeb() {
    }

    public IndicatorValueWeb(Long id, Month month, Gender gender, AgeGroup ageGroup, CantonWeb location, BigDecimal value, BigDecimal numeratorValue, BigDecimal denominatorValue) {
        this.id = id;
        this.month = month;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.location = location;
        this.value = value;
        this.numeratorValue=numeratorValue;
        this.denominatorValue=denominatorValue;
    }

    private Long id;

    private Month month;

    private Gender gender;

    private AgeGroup ageGroup;

    private CantonWeb location;

    private BigDecimal value;

    private BigDecimal denominatorValue;

    private BigDecimal numeratorValue;


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

    public CantonWeb getLocation() {
        return location;
    }

    public void setLocation(CantonWeb location) {
        this.location = location;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
}
