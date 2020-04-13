package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.PerformanceIndicatorDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.PerformanceIndicatorWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PerformanceIndicatorService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(PerformanceIndicatorService.class);

    @Inject
    PerformanceIndicatorDao performanceIndicatorDao;

    @Inject
    OutputService outputService;

    public List<PerformanceIndicatorWeb> getAllPerformanceIndicatorWebOrderedByCode() {


        List<PerformanceIndicator> performanceIndicators = this.getAllOrderedByCode();
        return this.performanceIndicatorsToPerformanceIndicatorWebs(performanceIndicators);
    }

    public List<PerformanceIndicatorWeb> getMainPerformanceIndicatorWebOrderedByCode() {


        List<PerformanceIndicator> performanceIndicators = this.getMainOrderedByCode();
        return this.performanceIndicatorsToPerformanceIndicatorWebs(performanceIndicators);
    }

    private List<PerformanceIndicator> getAllOrderedByCode() {
        return this.performanceIndicatorDao.getAllOrderedByCode();
    }
    private List<PerformanceIndicator> getMainOrderedByCode() {
        return this.performanceIndicatorDao.getMainOrderedByCode();
    }

    public PerformanceIndicator find(Long id) {
        return this.performanceIndicatorDao.find(id);
    }

    public Long save(PerformanceIndicator performanceIndicator) throws GeneralAppException {
        this.validate(performanceIndicator);

        // veo si es porcentaje

        PerformanceIndicator numerator = null;
        PerformanceIndicator denominator = null;
        if(performanceIndicator.getPercentageType()!=null && performanceIndicator.getPercentageType().equals(PercentageType.PERCENTAGE)){
        // el numerador
             numerator = performanceIndicator.getNumerator();
             denominator = performanceIndicator.getDenominator();

            numerator.setMeasureType(MeasureType.INTEGER);
            denominator.setMeasureType(MeasureType.INTEGER);
            numerator.setNumerator(null);
            numerator.setDenominator(null);
            numerator.setPercentageType(PercentageType.NUMERATOR);
            denominator.setPercentageType(PercentageType.DENOMINATOR);
            numerator.setIndicatorType(performanceIndicator.getIndicatorType());
            denominator.setIndicatorType(performanceIndicator.getIndicatorType());

            numerator.setState(performanceIndicator.getState());
            denominator.setState(performanceIndicator.getState());

            numerator.setOutput(performanceIndicator.getOutput());
            denominator.setOutput(performanceIndicator.getOutput());

            performanceIndicator.setNumerator(numerator);
            performanceIndicator.setDenominator(denominator);

        }

        if(numerator!=null) {
            this.performanceIndicatorDao.save(numerator);
            this.performanceIndicatorDao.save(denominator);
        }
        this.performanceIndicatorDao.save(performanceIndicator);
        return  performanceIndicator.getId();

    }

    public Long save(PerformanceIndicatorWeb performanceIndicatorWeb) throws GeneralAppException {

        PerformanceIndicator performanceIndicator = this.performanceIndicatorWebToPerformanceIndicator(performanceIndicatorWeb);
        this.save(performanceIndicator);
        return performanceIndicator.getId();

    }


    public PerformanceIndicator update(PerformanceIndicator performanceIndicator) throws GeneralAppException {

        return this.performanceIndicatorDao.update(performanceIndicator);
    }

    public Long update(PerformanceIndicatorWeb oWeb) throws GeneralAppException {
        PerformanceIndicator oOrg = this.find(oWeb.getId());
        if (oOrg == null) {
            throw new GeneralAppException("No se puedo encontrar el performanceIndicator con id :" + oWeb.getId(), Response.Status.NOT_FOUND.getStatusCode());
        }

        PerformanceIndicator oNew = this.performanceIndicatorWebToPerformanceIndicator(oWeb);
        oOrg.setOutput(oNew.getOutput());
        oOrg.setDescription(oNew.getDescription());
        oOrg.setState(oNew.getState());
        oOrg.setIndicatorType(oNew.getIndicatorType());



        if(oNew.getPercentageType()!=null && oNew.getPercentageType().equals(PercentageType.PERCENTAGE)){
            PerformanceIndicator numerator = null;
            PerformanceIndicator denominator = null;
            numerator = oOrg.getNumerator();
            denominator = oOrg.getDenominator();

            numerator.setMeasureType(MeasureType.INTEGER);
            denominator.setMeasureType(MeasureType.INTEGER);
            numerator.setNumerator(null);
            numerator.setDenominator(null);
            numerator.setPercentageType(PercentageType.NUMERATOR);
            denominator.setPercentageType(PercentageType.DENOMINATOR);
            numerator.setIndicatorType(oNew.getIndicatorType());
            denominator.setIndicatorType(oNew.getIndicatorType());

            numerator.setState(oNew.getState());
            denominator.setState(oNew.getState());

            numerator.setOutput(oNew.getOutput());
            denominator.setOutput(oNew.getOutput());

            numerator.setDescription(oNew.getNumerator().getDescription());
            denominator.setDescription(oNew.getDenominator().getDescription());

            oOrg.setNumerator(numerator);
            oOrg.setDenominator(denominator);
            LOGGER.info(numerator.getDescription());
            LOGGER.info(denominator.getDescription());
            this.performanceIndicatorDao.update(numerator);
            this.performanceIndicatorDao.update(denominator);
        }


        this.validate(oOrg);
        this.update(oOrg);
        return oOrg.getId();

    }


    public void validate(PerformanceIndicator performanceIndicator) throws GeneralAppException {

        if (performanceIndicator.getOutput() == null) {
            throw new GeneralAppException("El output es un valor requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (StringUtils.isBlank(performanceIndicator.getDescription())) {
            throw new GeneralAppException("La descripción es un valor requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (performanceIndicator.getState() == null) {
            throw new GeneralAppException("El estado es un valor requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (performanceIndicator.getPercentageType() != null) {
            if (performanceIndicator.getPercentageType().equals(PercentageType.PERCENTAGE)) {
                if (performanceIndicator.getNumerator()==null || performanceIndicator.getDenominator()==null) {
                    throw new GeneralAppException("Los denominadores de porcentaje deben tener un numerador y un denominador", Response.Status.BAD_REQUEST.getStatusCode());
                }
                if (StringUtils.isBlank(performanceIndicator.getNumerator().getDescription()) || StringUtils.isBlank(performanceIndicator.getDenominator().getDescription())) {
                    throw new GeneralAppException("Los denominadores de porcentaje deben tener un numerador y un denominador", Response.Status.BAD_REQUEST.getStatusCode());
                }


            }
        }
        List<PerformanceIndicator> result = new ArrayList<>(this.performanceIndicatorDao.getByDescription(performanceIndicator.getDescription()));

        if (performanceIndicator.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un performanceIndicator con este nombre", Response.Status.CONFLICT.getStatusCode());
        } else if (performanceIndicator.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (PerformanceIndicator performanceIndicatorT : result) {
                if (!performanceIndicatorT.getId().equals(performanceIndicator.getId())) {
                    throw new GeneralAppException("Ya existe un performanceIndicator con este código", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
        result = new ArrayList<>(this.performanceIndicatorDao.getByDescription(performanceIndicator.getDescription()));

        if (performanceIndicator.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
        } else if (performanceIndicator.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (PerformanceIndicator performanceIndicatorT : result) {
                if (!performanceIndicatorT.getId().equals(performanceIndicator.getId())) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
    }


    public PerformanceIndicatorWeb performanceIndicatorToPerformanceIndicatorWeb(PerformanceIndicator performanceIndicator) {
        if (performanceIndicator == null) {
            return null;
        } else {

            PerformanceIndicatorWeb numeratorWeb = null;
            PerformanceIndicatorWeb denominatorWeb = null;
            if (performanceIndicator.getPercentageType() != null && performanceIndicator.getPercentageType().equals(PercentageType.PERCENTAGE)) {
                PerformanceIndicator enumerator = performanceIndicator.getNumerator();
                numeratorWeb = this.performanceIndicatorToPerformanceIndicatorWeb(enumerator);
                PerformanceIndicator denominator = performanceIndicator.getDenominator();
                denominatorWeb = this.performanceIndicatorToPerformanceIndicatorWeb(denominator);
            }

            PerformanceIndicatorWeb r = new PerformanceIndicatorWeb(performanceIndicator.getId(), performanceIndicator.getDescription(), performanceIndicator.getState(),
                    performanceIndicator.getIndicatorType(),
                    this.outputService.outputToOutputWeb(performanceIndicator.getOutput()),
                    performanceIndicator.getMeasureType(), performanceIndicator.getPercentageType(), numeratorWeb, denominatorWeb);
            return r;
        }
    }

    private List<PerformanceIndicatorWeb> performanceIndicatorsToPerformanceIndicatorWebs(List<PerformanceIndicator> performanceIndicators) {
        List<PerformanceIndicatorWeb> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(performanceIndicators)) {
            for (PerformanceIndicator performanceIndicator : performanceIndicators) {
                result.add(this.performanceIndicatorToPerformanceIndicatorWeb(performanceIndicator));
            }
        }
        return result;
    }

    private PerformanceIndicator performanceIndicatorWebToPerformanceIndicator(PerformanceIndicatorWeb performanceIndicatorWeb) {
        if(performanceIndicatorWeb==null){
            return null;
        }
        PerformanceIndicator o = new PerformanceIndicator();
        o.setId(performanceIndicatorWeb.getId());
        o.setIndicatorType(performanceIndicatorWeb.getIndicatorType());
        o.setState(performanceIndicatorWeb.getState());
        o.setDescription(performanceIndicatorWeb.getDescription());
        Output output = null;
        if (performanceIndicatorWeb.getOutputWeb() != null) {
            output = this.outputService.find(performanceIndicatorWeb.getOutputWeb().getId());
        }
        o.setOutput(output);
        o.setMeasureType(performanceIndicatorWeb.getMeasureType());
        o.setPercentageType(performanceIndicatorWeb.getPercentageType());
        if (o.getMeasureType().equals(MeasureType.PERCENTAGE)) {
            // debe haber un numerador y un denominador
            o.setNumerator(this.performanceIndicatorWebToPerformanceIndicator(performanceIndicatorWeb.getNumerator()));
            o.setDenominator(this.performanceIndicatorWebToPerformanceIndicator(performanceIndicatorWeb.getDenominator()));
        }

        return o;
    }


    public List<PerformanceIndicatorWeb> getWebByOutputId(Long id) {
        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByOutputId(id));
    }

    private List<PerformanceIndicator> getByOutputId(Long id) {
        return this.performanceIndicatorDao.getByOutputId(id);
    }

    public List<PerformanceIndicatorWeb> getWebsByStateAndPeriodIdandOutputId(Long periodId, State state, Long outputId) {

        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByStateAndPeriodIdandOutputId(periodId, state, outputId));
    }

    public List<PerformanceIndicatorWeb> getWebByStateAndPeriodIdandOutputIdAndIndicatorType(Long periodId, State state, Long outputId, IndicatorType indicatorType) {

        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByStateAndPeriodIdandOutputIdAndIndicatorType(periodId, state, outputId, indicatorType));
    }

    public List<PerformanceIndicatorWeb> getWebsByIndicatorType(IndicatorType indicatorType, State state) {

        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByIndicatorType(indicatorType, state));
    }

    private List<PerformanceIndicator> getByIndicatorType(IndicatorType indicatorType, State state) {
        return this.performanceIndicatorDao.getByIndicatorType(indicatorType, state);
    }

    private List<PerformanceIndicator> getByStateAndPeriodIdandOutputId(Long periodoId, State state, Long outputId) {
        return this.performanceIndicatorDao.getByStateAndPeriodIdandOutputId(periodoId, state, outputId);
    }

    private List<PerformanceIndicator> getByStateAndPeriodIdandOutputIdAndIndicatorType(Long periodoId, State state, Long outputId, IndicatorType indicatorType) {
        return this.performanceIndicatorDao.getByStateAndPeriodIdandOutputIdAndIndicatorType(periodoId, state, outputId, indicatorType);
    }

    public List<PerformanceIndicatorWeb> getWebByOutputIdAndType(Long id, IndicatorType type) {
        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByOutputIdAndType(id, type));
    }

    private List<PerformanceIndicator> getByOutputIdAndType(Long id, IndicatorType type) {
        return this.performanceIndicatorDao.getByOutputIdAndType(id, type);
    }

}
