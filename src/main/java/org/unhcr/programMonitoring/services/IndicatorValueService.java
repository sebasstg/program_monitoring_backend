package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.daos.IndicatorValueDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.CantonWeb;
import org.unhcr.programMonitoring.webServices.model.IndicatorValueWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Stateless
public class IndicatorValueService {


    @Inject
    IndicatorValueDao indicatorValueDao;

    @Inject
    CantonService cantonService;

    public IndicatorValue saveOrUpdate(IndicatorValue indicatorValue){
        if(indicatorValue.getId()==null){
            return this.indicatorValueDao.save(indicatorValue);
        }else{
            return this.indicatorValueDao.update(indicatorValue);
        }
    }

    public List<IndicatorValue> createValuesForNoDisaggregation() {
        List<IndicatorValue> result = new ArrayList<>();

        for (Month month : getSortedMonths()) {
            IndicatorValue indicatorValue = new IndicatorValue();
            indicatorValue.setMonth(month);
            result.add(indicatorValue);
        }
        return result;
    }

    public List<IndicatorValue> createValuesForGenderDisaggregation() {
        List<IndicatorValue> result = new ArrayList<>();


        for (Month month : getSortedMonths()) {
            // por cada mes agrego generos

            for (Gender gender : getSortedGenders()) {
                IndicatorValue indicatorValue = new IndicatorValue();
                indicatorValue.setMonth(month);
                indicatorValue.setGender(gender);
                result.add(indicatorValue);
            }
            // si quiero total mensual
            /*
            IndicatorValue indicatorValue = new IndicatorValue();
            indicatorValue.setMonth(month);
            result.add(indicatorValue);
*/
        }
        return result;
    }

    public List<IndicatorValue> createValuesForAgeDisaggregation() {
        List<IndicatorValue> result = new ArrayList<>();

        for (AgeGroup ageGroup : getSortedAgeGroups()) {
            for (Month month : getSortedMonths()) {
                IndicatorValue indicatorValue = new IndicatorValue();
                indicatorValue.setMonth(month);
                indicatorValue.setAgeGroup(ageGroup);
                result.add(indicatorValue);
            }
        }
        return result;
    }

    public List<IndicatorValue> createValuesForLocationDisaggregation(List<Canton> cantones) {
        List<IndicatorValue> result = new ArrayList<>();

        for (Canton canton : getSortedCantones(cantones)) {
            for (Month month : getSortedMonths()) {
                IndicatorValue indicatorValue = new IndicatorValue();
                indicatorValue.setMonth(month);
                indicatorValue.setLocation(canton);
                result.add(indicatorValue);
            }
        }
        return result;
    }

    public List<IndicatorValue> createValuesForGenderAgeDisaggregation() {
        List<IndicatorValue> result = new ArrayList<>();

        for (AgeGroup ageGroup : getSortedAgeGroups()) {
            for (Month month : getSortedMonths()) {
                // por cada mes agrego generos

                for (Gender gender : getSortedGenders()) {
                    IndicatorValue indicatorValue = new IndicatorValue();
                    indicatorValue.setMonth(month);
                    indicatorValue.setGender(gender);
                    indicatorValue.setAgeGroup(ageGroup);
                    result.add(indicatorValue);
                }
            }
        }
        return result;
    }


    public List<IndicatorValue> createValuesForGenderLocationDisaggregation(List<Canton> cantones) {
        List<IndicatorValue> result = new ArrayList<>();
        for (Canton canton : getSortedCantones(cantones)) {
            for (Month month : getSortedMonths()) {
                // por cada mes agrego generos

                for (Gender gender : getSortedGenders()) {
                    IndicatorValue indicatorValue = new IndicatorValue();
                    indicatorValue.setMonth(month);
                    indicatorValue.setGender(gender);
                    indicatorValue.setLocation(canton);
                    result.add(indicatorValue);
                }
            }
        }
        return result;
    }


    public List<IndicatorValue> createValuesForAgeLocationDisaggregation(List<Canton> cantones) {
        List<IndicatorValue> result = new ArrayList<>();
        for (Canton canton : getSortedCantones(cantones)) {
            for (Month month : getSortedMonths()) {
                for (AgeGroup age : getSortedAgeGroups()) {
                    // por cada mes agrego generos
                    IndicatorValue indicatorValue = new IndicatorValue();
                    indicatorValue.setMonth(month);
                    indicatorValue.setAgeGroup(age);
                    indicatorValue.setLocation(canton);
                    result.add(indicatorValue);
                }
            }
        }
        return result;
    }

    public List<IndicatorValue> createValuesForGenderAgeLocationDisaggregation(List<Canton> cantones) {
        List<IndicatorValue> result = new ArrayList<>();
        for (Canton canton : getSortedCantones(cantones)) {
            for (Month month : getSortedMonths()) {
                // por cada mes agrego generos

                for (Gender gender : getSortedGenders()) {
                    for (AgeGroup age : getSortedAgeGroups()) {
                        IndicatorValue indicatorValue = new IndicatorValue();
                        indicatorValue.setMonth(month);
                        indicatorValue.setGender(gender);
                        indicatorValue.setAgeGroup(age);
                        indicatorValue.setLocation(canton);
                        result.add(indicatorValue);
                    }
                }
            }
        }
        return result;
    }


    private List<Month> getSortedMonths() {
        List<Month> months = Arrays.asList(Month.values());
        months.sort(Comparator.comparingInt(Month::getOrder));
        return months;
    }

    private List<Gender> getSortedGenders() {
        List<Gender> genders = Arrays.asList(Gender.values());
        genders.sort(Comparator.comparingInt(Gender::getOrder));
        return genders;
    }


    private List<AgeGroup> getSortedAgeGroups() {
        List<AgeGroup> items = Arrays.asList(AgeGroup.values());
        items.sort(Comparator.comparingInt(AgeGroup::getOrder));
        return items;
    }

    private List<Canton> getSortedCantones(List<Canton> cantones) {
        Comparator<Canton> comparator = Comparator.comparing(canton -> canton.getProvincia().getDescription());
        comparator = comparator.thenComparing(canton -> canton.getDescription());
        cantones.sort(comparator);
        return cantones;
    }

    public List<Quarter> createQuarters(List<IndicatorValue> values) throws GeneralAppException {
        Quarter q1 = new Quarter();
        q1.setQuarterNumber(QuarterNumber.I);

        Quarter q2 = new Quarter();
        q2.setQuarterNumber(QuarterNumber.II);

        Quarter q3 = new Quarter();
        q3.setQuarterNumber(QuarterNumber.III);


        Quarter q4 = new Quarter();
        q4.setQuarterNumber(QuarterNumber.IV);

        for (IndicatorValue value : values) {
            switch (value.getMonth()) {
                case JANUARY:
                case FEBRUARY:
                case MARCH:
                    q1.addIndicatorValue(value);
                    break;
                case APRIL:
                case MAY:
                case JUNE:
                    q2.addIndicatorValue(value);
                    break;
                case JULY:
                case AUGUST:
                case SEPTEMBER:
                    q3.addIndicatorValue(value);
                    break;

                case OCTOBER:
                case NOVEMBER:
                case DECEMBER:
                    q4.addIndicatorValue(value);
                    break;
                default:
                    throw new GeneralAppException("Error al crear el trimestre. Valor sin mes");
            }
        }

        List<Quarter> result = new ArrayList<>();
        result.add(q1);
        result.add(q2);
        result.add(q3);
        result.add(q4);
        return result;
    }

    public IndicatorValueWeb indicatorValueToIndicatorValueWeb(IndicatorValue indicatorValue){
        if(indicatorValue==null) return null;
        CantonWeb cantonWeb = this.cantonService.cantonToCantonWeb(indicatorValue.getLocation());
        IndicatorValueWeb i = new IndicatorValueWeb(indicatorValue.getId(),indicatorValue.getMonth(),indicatorValue.getGender(),indicatorValue.getAgeGroup(),cantonWeb,indicatorValue.getValue());
        return i;
    }

    public List<IndicatorValueWeb> indicatorValueToIndicatorValuesWebs(List<IndicatorValue> indicatorValues){
        List<IndicatorValueWeb> r=new ArrayList<>();

        for(IndicatorValue indicatorValue:indicatorValues){
            r.add(this.indicatorValueToIndicatorValueWeb(indicatorValue));
        }
        return r;
    }
    public IndicatorValue find(Long id){
        return this.indicatorValueDao.find(id);
    }

    public List<IndicatorValue> getSubIndicatorValuesByGeneralIndicatorIdAndProjectId(Long generalIndicatorId, Long projectId){
        return this.indicatorValueDao.getSubIndicatorValuesByGeneralIndicatorIdAndProjectId(generalIndicatorId,projectId);
    }

    public List<IndicatorValue> getByIndicatorExecutionId(Long indicatorExecutionId){
        return this.indicatorValueDao.getByIndicatorExecutionId(indicatorExecutionId);
    }

}
