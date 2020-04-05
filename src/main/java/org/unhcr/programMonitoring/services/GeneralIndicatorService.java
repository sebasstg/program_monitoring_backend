package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.GeneralIndicatorDao;
import org.unhcr.programMonitoring.model.GeneralIndicator;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.webServices.model.GeneralIndicatorWeb;
import org.unhcr.programMonitoring.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Stateless
public class GeneralIndicatorService {

    private static final Logger LOGGER = Logger.getLogger(GeneralIndicatorService.class);

    @Inject
    GeneralIndicatorDao generalIndicatorDao;

    @Inject
    PeriodService periodService;


    public List<GeneralIndicatorWeb> getWebByPeriodId(Long periodId) {
        return this.generalIndicatorsToGeneralIndicatorWebs(this.generalIndicatorDao.getByPeriodId(periodId));
    }


    public Long save(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        this.validate(generalIndicatorWeb);
        this.generalIndicatorDao.save(this.generalIndicatorWebToGeneralIndicator(generalIndicatorWeb));
        return generalIndicatorWeb.getId();
    }

    public Long update(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        this.validate(generalIndicatorWeb);
        //this.generalIndicatorDao.update(this.generalIndicatorWebToGeneralIndicator(generalIndicatorWeb));
        if (generalIndicatorWeb.getId() == null) {
            throw new GeneralAppException("El id es un dato obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }
        GeneralIndicator org = this.generalIndicatorDao.find(generalIndicatorWeb.getId());
        if (org == null) {
            throw new GeneralAppException("El no se pudo encontrar el indicador", Response.Status.NOT_FOUND.getStatusCode());
        }

        Set<GeneralIndicator> subs = null;
        // solo puedo actualizar estado y descripción
        org.setDescription(generalIndicatorWeb.getDescription());

        // veo si es padre
        // veo si cambia el estado
        if (!generalIndicatorWeb.getState().equals(org.getState())) {
            if (generalIndicatorWeb.getState().equals(State.INACTIVE) && generalIndicatorWeb.getParent()) {
                // desactivo todos los sub indicadores
                subs=org.getSubGeneralIndicators();
                for(GeneralIndicator sgi:subs){
                    sgi.setState(State.INACTIVE);
                    this.generalIndicatorDao.update(sgi);
                }
            }
        }
        org.setState(generalIndicatorWeb.getState());
        this.generalIndicatorDao.update(org);

        return generalIndicatorWeb.getId();
    }

    private void validate(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (StringUtils.isBlank(generalIndicatorWeb.getDescription())) {
            throw new GeneralAppException("El nombre del indicador es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (generalIndicatorWeb.getParent() == null) {
            throw new GeneralAppException("El tipo de indicador es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (generalIndicatorWeb.getDisaggregationType() == null) {
            throw new GeneralAppException("El tipo de desagregación es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }
       /* if (generalIndicatorWeb.getTarget() == null) {
            throw new GeneralAppException("La meta del indicador es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }

       */

        if (generalIndicatorWeb.getState() == null) {
            throw new GeneralAppException("El estado del indicador es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (generalIndicatorWeb.getPeriod() == null || generalIndicatorWeb.getPeriod().getId() == null) {
            throw new GeneralAppException("El periodo del indicador es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }


        /// para parent
        if (generalIndicatorWeb.getParent()) {
            // veo q no haya otro
            if (generalIndicatorWeb.getId() != null) {
                List<GeneralIndicator> parents = this.generalIndicatorDao.getByPeriodIdAndParent(generalIndicatorWeb.getPeriod().getId(), Boolean.TRUE);
                for (GeneralIndicator generalIndicator : parents) {
                    if (generalIndicator.getId() != generalIndicatorWeb.getId()) {
                        throw new GeneralAppException("Ya existe un indicador general principal.");
                    }
                }
            }

        } else {
            // si es hijo ya debe haber un parent
            List<GeneralIndicator> parents = this.generalIndicatorDao.getByPeriodIdAndParent(generalIndicatorWeb.getPeriod().getId(), Boolean.TRUE);
            if (CollectionUtils.isEmpty(parents)) {
                throw new GeneralAppException("Por favor cree un indicador general principal antes de crear un subindicador", Response.Status.BAD_REQUEST.getStatusCode());
            }

            // n debe haber 2 con la misma descripción
            List<GeneralIndicator> sons = this.generalIndicatorDao.getByPeriodIdAndParent(generalIndicatorWeb.getPeriod().getId(), Boolean.FALSE);
            for (GeneralIndicator generalIndicator : sons) {
                if (generalIndicatorWeb.getId() == null && CollectionUtils.isNotEmpty(sons)) {
                    throw new GeneralAppException("Ya existe un indicador con esta descripción.", Response.Status.BAD_REQUEST.getStatusCode());
                } else if (generalIndicator.getDescription().equals(generalIndicatorWeb.getDescription()) && generalIndicator.getId() != generalIndicatorWeb.getId()) {
                    throw new GeneralAppException("Ya existe un indicador con esta descripción.", Response.Status.BAD_REQUEST.getStatusCode());
                }
            }
        }


    }

    private GeneralIndicator generalIndicatorWebToGeneralIndicator(GeneralIndicatorWeb generalIndicatorWeb) {
        GeneralIndicator generalIndicator = new GeneralIndicator();
        generalIndicator.setId(generalIndicatorWeb.getId());
        generalIndicator.setParent(generalIndicatorWeb.getParent());
        generalIndicator.setDescription(generalIndicatorWeb.getDescription());
        generalIndicator.setDisaggregationType(generalIndicatorWeb.getDisaggregationType());
        generalIndicator.setTarget(generalIndicatorWeb.getTarget());
        generalIndicator.setTotalExecution(generalIndicatorWeb.getTotalExecution());
        generalIndicator.setExecutionPercentage(generalIndicatorWeb.getExecutionPercentage());
        generalIndicator.setState(generalIndicatorWeb.getState());
        Period period = null;
        if (generalIndicatorWeb.getPeriod() != null) {
            period = this.periodService.getById(generalIndicatorWeb.getPeriod().getId());
        }
        generalIndicator.setPeriod(period);
        // busco el parent
        if (!generalIndicator.getParent()) {
            generalIndicator.setMainIndicator(this.generalIndicatorDao.getMainByPeriodId(period.getId()));
        }


        return generalIndicator;
    }

    private List<GeneralIndicator> getByPeriodId(Long periodId) {
        return this.generalIndicatorDao.getByPeriodId(periodId);
    }


    private GeneralIndicatorWeb generalIndicatorToGeneralIndicatorWeb(GeneralIndicator generalIndicator) {
        if (generalIndicator == null) return null;

        PeriodWeb periodWeb = this.periodService.periodToPeriodWeb(generalIndicator.getPeriod());

        return new GeneralIndicatorWeb(
                generalIndicator.getId(), generalIndicator.getParent(), generalIndicator.getDescription(), generalIndicator.getDisaggregationType(),
                generalIndicator.getTarget(), generalIndicator.getTotalExecution(), generalIndicator.getExecutionPercentage(),
                generalIndicator.getState(), periodWeb);
    }


    private List<GeneralIndicatorWeb> generalIndicatorsToGeneralIndicatorWebs(List<GeneralIndicator> generalIndicators) {
        if (generalIndicators == null) return null;
        List<GeneralIndicatorWeb> r = new ArrayList<>();
        for (GeneralIndicator generalIndicator : generalIndicators) {
            r.add(this.generalIndicatorToGeneralIndicatorWeb(generalIndicator));
        }
        return r;
    }
}
