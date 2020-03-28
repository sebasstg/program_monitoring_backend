package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.PerformanceIndicatorDao;
import org.unhcr.programMonitoring.model.Output;
import org.unhcr.programMonitoring.model.PerformanceIndicator;
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

    private List<PerformanceIndicator> getAllOrderedByCode() {
        return this.performanceIndicatorDao.getAllOrderedByCode();
    }

    public PerformanceIndicator find(Long id) {
        return this.performanceIndicatorDao.find(id);
    }

    public Long save(PerformanceIndicator performanceIndicator) throws GeneralAppException {
        this.validate(performanceIndicator);
        return this.performanceIndicatorDao.save(performanceIndicator).getId();

    }

    public Long save(PerformanceIndicatorWeb performanceIndicatorWeb) throws GeneralAppException {

        PerformanceIndicator performanceIndicator = this.performanceIndicatorWebToPerformanceIndicator(performanceIndicatorWeb);
        this.save(performanceIndicator);
        return performanceIndicator.getId();

    }


    public PerformanceIndicator update(PerformanceIndicator performanceIndicator) throws GeneralAppException {
        this.validate(performanceIndicator);
        return this.performanceIndicatorDao.update(performanceIndicator);
    }

    public Long update(PerformanceIndicatorWeb oWeb) throws GeneralAppException {
        PerformanceIndicator oOrg = this.find(oWeb.getId());
        if (oOrg == null) {
            throw new GeneralAppException("No se puedo encontrar el performanceIndicator con id :" + oWeb.getId(), Response.Status.NOT_FOUND.getStatusCode());
        }

        PerformanceIndicator oNew =this.performanceIndicatorWebToPerformanceIndicator(oWeb);
        oOrg.setOutput(oNew.getOutput());
        oOrg.setDescription(oNew.getDescription());
        oOrg.setState(oNew.getState());
        oOrg.setIndicatorType(oNew.getIndicatorType());

        this.validate(oOrg);
        this.performanceIndicatorDao.update(oOrg);
        return oOrg.getId();

    }


    public void validate(PerformanceIndicator performanceIndicator) throws GeneralAppException {

        if (performanceIndicator.getOutput() == null) {
            throw new GeneralAppException("El output es un valor requerido");
        }
        if (StringUtils.isBlank(performanceIndicator.getDescription())) {
            throw new GeneralAppException("La descripción es un valor requerido");
        }
        if (performanceIndicator.getState() == null) {
            throw new GeneralAppException("El estado es un valor requerido");
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


    PerformanceIndicatorWeb performanceIndicatorToPerformanceIndicatorWeb(PerformanceIndicator performanceIndicator) {
        if (performanceIndicator == null) {
            return null;
        } else {
            return new PerformanceIndicatorWeb(performanceIndicator.getId(), performanceIndicator.getDescription(), performanceIndicator.getState(),  performanceIndicator.getIndicatorType(),this.outputService.outputToOutputWeb(performanceIndicator.getOutput()));
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
        return o;
    }


    public List<PerformanceIndicatorWeb> getWebByOutputId(Long id) {
        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByOutputId(id));
    }

    private List<PerformanceIndicator> getByOutputId(Long id) {
        return this.performanceIndicatorDao.getByOutputId(id);
    }

    public List<PerformanceIndicatorWeb> getWebsByStateAndPeriodIdandOutputId(Long periodId, State state, Long outputId) {

        return this.performanceIndicatorsToPerformanceIndicatorWebs(this.getByStateAndPeriodIdandOutputId(periodId,state,outputId));
    }

    private List<PerformanceIndicator> getByStateAndPeriodIdandOutputId(Long periodoId, State state, Long outputId) {
        return this.performanceIndicatorDao.getByStateAndPeriodIdandOutputId(periodoId, state, outputId);
    }
}
