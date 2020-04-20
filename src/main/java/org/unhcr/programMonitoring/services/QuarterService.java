package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.IndicatorExecutionDao;
import org.unhcr.programMonitoring.daos.IndicatorValueDao;
import org.unhcr.programMonitoring.daos.QuarterDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.IndicatorValueWeb;
import org.unhcr.programMonitoring.webServices.model.QuarterValuesWeb;
import org.unhcr.programMonitoring.webServices.model.QuarterWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class QuarterService {

    private static final Logger LOGGER = Logger.getLogger(QuarterService.class);

    @Inject
    QuarterDao quarterDao;

    @Inject
    IndicatorValueService indicatorValueService;

    @Inject
    IndicatorExecutionDao indicatorExecutionDao;

    public Quarter saveOrUpdate(Quarter quarter) {
        if (quarter.getId() == null) {
            return this.quarterDao.save(quarter);
        } else {
            return this.quarterDao.update(quarter);
        }
    }

    public List<QuarterWeb> quartersToQuarterWebs(List<Quarter> quarters) {
        List<QuarterWeb> quarterWebs = new ArrayList<>();
        for (Quarter quarter : quarters) {
            quarterWebs.add(this.quarterToQuarterWeb(quarter));
        }
        return quarterWebs;
    }

    public QuarterValuesWeb quarterValueToQuarterValueWeb(Quarter quarter) {
        if (quarter == null) return null;
        List<IndicatorValue> indicatorValues = new ArrayList<>(quarter.getIndicatorValues());
        List<IndicatorValueWeb> indicatorValuesWeb = this.indicatorValueService.indicatorValueToIndicatorValuesWebs(indicatorValues);
        QuarterValuesWeb quarterValuesWeb =
                new QuarterValuesWeb(quarter.getId(), quarter.getQuarterNumber(), quarter.getCommentary(), indicatorValuesWeb);
        return quarterValuesWeb;
    }


    public QuarterWeb quarterToQuarterWeb(Quarter quarter) {
        if (quarter == null) return null;
        QuarterWeb quarterWeb = new QuarterWeb(quarter.getId(), quarter.getQuarterNumber(), quarter.getCommentary());


        if (!quarter.getIndicatorExecution().getMeasureType().equals(MeasureType.PERCENTAGE)) {
            BigDecimal t = quarter.getIndicatorValues().stream().map(indicatorValue -> indicatorValue.getValue() != null ? indicatorValue.getValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            quarterWeb.setTotalExecution(t);
        } else {
            BigDecimal num = quarter.getIndicatorValues().stream().map(indicatorValue -> indicatorValue.getNumeratorValue() != null ? indicatorValue.getNumeratorValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal den = quarter.getIndicatorValues().stream().map(indicatorValue -> indicatorValue.getDenominatorValue() != null ? indicatorValue.getDenominatorValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal total = BigDecimal.ZERO;

            if (den != null && !den.equals(BigDecimal.ZERO)) {
                total = num.divide(den, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
            }
            quarterWeb.setTotalExecution(total);


        }
        return quarterWeb;
    }

    public QuarterValuesWeb getWebValuesById(Long id) {
        return this.quarterValueToQuarterValueWeb(this.quarterDao.find(id));
    }

    public Long update(QuarterValuesWeb quarterValuesWeb) {

        Quarter quarter = this.quarterDao.find(quarterValuesWeb.getId());
        quarter.setCommentary(quarterValuesWeb.getCommentary());
        this.quarterDao.update(quarter);
        // es general!!??
        switch (quarter.getIndicatorExecution().getIndicatorType()) {
            case GENERAL:
                return updateGeneral(quarterValuesWeb, quarter);
            case FOCUS:
            case ESPECIFICO:
                updateIndicator(quarterValuesWeb, quarter);
            default:
                return null;
        }


    }

    private Long updateIndicator(QuarterValuesWeb quarterValuesWeb, Quarter quarter) {
        for (IndicatorValueWeb indicatorValueWeb : quarterValuesWeb.getIndicatorValues()) {
            IndicatorValue indicatorValue = this.indicatorValueService.find(indicatorValueWeb.getId());

            if (quarter.getIndicatorExecution().getMeasureType().equals(MeasureType.PERCENTAGE)) {
                indicatorValue.setNumeratorValue(indicatorValueWeb.getNumeratorValue());
                indicatorValue.setDenominatorValue(indicatorValueWeb.getDenominatorValue());
                if (indicatorValue.getDenominatorValue() != null && !indicatorValue.getDenominatorValue().equals(BigDecimal.ZERO)) {
                    indicatorValue.setValue(indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)));
                }
            } else {
                indicatorValue.setValue(indicatorValueWeb.getValue());
            }

            this.indicatorValueService.saveOrUpdate(indicatorValue);
            //updatedValues.add(indicatorValue);
        }
        IndicatorExecution indicatorExecution = quarter.getIndicatorExecution();

        this.updateTotalValuesIndicatorExecution(indicatorExecution);

        return quarter.getId();
    }

    private IndicatorExecution updateTotalValuesIndicatorExecution(IndicatorExecution indicatorExecution) {
        List<IndicatorValue> totalValues = this.indicatorValueService.getByIndicatorExecutionId(indicatorExecution.getId());

        if (!indicatorExecution.getMeasureType().equals(MeasureType.PERCENTAGE)) {
            BigDecimal t = totalValues.stream().map(indicatorValue -> indicatorValue.getValue() != null ? indicatorValue.getValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);

            indicatorExecution.setTotalExecution(t.intValue());

            Double perc = 0d;
            if (indicatorExecution.getTarget() != null && indicatorExecution.getTarget() > 0) {
                perc = ((double) indicatorExecution.getTotalExecution() / (double) indicatorExecution.getTarget()) * 100;
            }
            indicatorExecution.setExecutionPercentage((int) Math.round(perc));
        } else {
            if (indicatorExecution.getTarget() != null && indicatorExecution.getTarget() > 0) {
                BigDecimal num = totalValues.stream().map(indicatorValue -> indicatorValue.getNumeratorValue() != null ? indicatorValue.getNumeratorValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal den = totalValues.stream().map(indicatorValue -> indicatorValue.getDenominatorValue() != null ? indicatorValue.getDenominatorValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal total = null;
                Integer per=null;
                if (den != null && !den.equals(BigDecimal.ZERO)) {
                    total = num.divide(den, 0, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                }
                if(total!=null){
                 //   per=total.intValue()/indicatorExecution.getTarget();
                    per=total.intValue();
                    indicatorExecution.setTotalExecution(per);
                    indicatorExecution.setExecutionPercentage(per);
                }


            }

        }
        this.indicatorExecutionDao.update(indicatorExecution);
        return indicatorExecution;

    }

    private Long updateGeneral(QuarterValuesWeb quarterValuesWeb, Quarter quarter) {
        //solo actualiza subindicadores
        List<IndicatorValue> updatedValues = new ArrayList<>();
        for (IndicatorValueWeb indicatorValueWeb : quarterValuesWeb.getIndicatorValues()) {
            IndicatorValue indicatorValue = this.indicatorValueService.find(indicatorValueWeb.getId());
            indicatorValue.setValue(indicatorValueWeb.getValue());
            indicatorValue.setNumeratorValue(indicatorValueWeb.getValue());
            indicatorValue.setDenominatorValue(indicatorValueWeb.getValue());
            this.indicatorValueService.saveOrUpdate(indicatorValue);
            updatedValues.add(indicatorValue);
        }

        GeneralIndicator generalIndicatorMain = quarter.getIndicatorExecution().getGeneralIndicator().getMainIndicator();
        IndicatorExecution generalMainExecution = this.indicatorExecutionDao.getByGeneralIndicatorIdAndProjectId(generalIndicatorMain.getId(), quarter.getIndicatorExecution().getProject().getId());


        Set<IndicatorValue> mainValues = generalMainExecution.getIndicatorValues();

        for (IndicatorValue indicatorMainValue : mainValues) {
            Predicate<IndicatorValue> byParameters = indicatorValue -> {
                boolean r = indicatorMainValue.getMonth().equals(indicatorValue.getMonth());
                if (indicatorMainValue.getAgeGroup() != null && indicatorValue.getAgeGroup() != null) {
                    r = r && indicatorMainValue.getAgeGroup().equals(indicatorValue.getAgeGroup());
                }
                if (indicatorMainValue.getGender() != null && indicatorValue.getGender() != null) {
                    r = r && indicatorMainValue.getGender().equals(indicatorValue.getGender());
                }
                if (indicatorMainValue.getLocation() != null && indicatorValue.getLocation() != null) {
                    r = r && indicatorMainValue.getLocation().getId().equals(indicatorValue.getLocation().getId());
                }
                if (indicatorMainValue.getGender() != null && indicatorValue.getGender() != null) {
                    r = r && indicatorMainValue.getGender().equals(indicatorValue.getGender());
                }
                return r;
            };

            //BigDecimal totalV = BigDecimal.ZERO;
            //obtengo todo los values de los subindicadorss
            List<IndicatorValue> subValues = this.indicatorValueService.getSubIndicatorValuesByGeneralIndicatorIdAndProjectId(generalIndicatorMain.getId(), quarter.getIndicatorExecution().getProject().getId());
            List<IndicatorValue> filterresult = subValues.stream().filter(byParameters)
                    .collect(Collectors.toList());

            BigDecimal t = filterresult.stream().map(indicatorValue -> indicatorValue.getValue() != null ? indicatorValue.getValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);

            indicatorMainValue.setValue(t);

            this.indicatorValueService.saveOrUpdate(indicatorMainValue);
        }

        List<IndicatorExecution> generalIndicators = this.indicatorExecutionDao.getGeneralIndicators(generalMainExecution.getProject().getId());
        for (IndicatorExecution generalIndicatorExecution : generalIndicators) {

            this.updateTotalValuesIndicatorExecution(generalIndicatorExecution);
        }


        return quarter.getId();
    }
}
