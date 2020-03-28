package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.PeriodPerformanceIndicatorAssigmentDao;
import org.unhcr.programMonitoring.model.PerformanceIndicator;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.PeriodPerformanceIndicatorAssigment;
import org.unhcr.programMonitoring.webServices.model.PeriodPerformanceIndicatorAssigmentWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PeriodPerformanceIndicatorAssigmentService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(PeriodPerformanceIndicatorAssigmentService.class);

    @Inject
    PeriodPerformanceIndicatorAssigmentDao periodPerformanceIndicatorAssigmentDao;

    @Inject
    PerformanceIndicatorService performanceIndicatorService;

    @Inject
    PeriodService periodService;


    @SuppressWarnings("unused")
    public List<PeriodPerformanceIndicatorAssigmentWeb> getByPeriodoId(Long periodId) {

        List<PeriodPerformanceIndicatorAssigment> ass = this.periodPerformanceIndicatorAssigmentDao.getByPeriodId(periodId);

        return this.periodPerformanceIndicatorAssigmentsToPeriodPerformanceIndicatorAssigmentWebs(ass);

    }

    public Long save(PeriodPerformanceIndicatorAssigmentWeb periodPerformanceIndicatorAssigmentWeb) throws GeneralAppException {

        if (periodPerformanceIndicatorAssigmentWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear una asignación de indicador a periodo si este ya tiene un id", Response.Status.BAD_REQUEST.getStatusCode());
        }

        PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigmentT = this.periodPerformanceIndicatorAssigmentDao.getByPeriodIdAndPerformanceIndicatorId(periodPerformanceIndicatorAssigmentWeb.getPeriodWeb().getId(), periodPerformanceIndicatorAssigmentWeb.getPerformanceIndicatorWeb().getId());
        if (periodPerformanceIndicatorAssigmentWeb.getPerformanceIndicatorWeb() != null && periodPerformanceIndicatorAssigmentT != null) {
            throw new GeneralAppException("El indicador " + periodPerformanceIndicatorAssigmentT.getPerformanceIndicator().getDescription() + " ya se encuentra adignado al periodo " + periodPerformanceIndicatorAssigmentT.getPeriod().getYear() + ".", Response.Status.CONFLICT.getStatusCode());
        }

        PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment = this.periodPerformanceIndicatorAssigmentWebToPeriodPerformanceIndicatorAssigment(periodPerformanceIndicatorAssigmentWeb);

        this.validate(periodPerformanceIndicatorAssigment);

        this.periodPerformanceIndicatorAssigmentDao.save(periodPerformanceIndicatorAssigment);
        return periodPerformanceIndicatorAssigment.getId();
    }

    public Long update(PeriodPerformanceIndicatorAssigmentWeb periodPerformanceIndicatorAssigmentWeb) throws GeneralAppException {

        if (periodPerformanceIndicatorAssigmentWeb.getId() == null) {
            throw new GeneralAppException("No se puede actualizar una asignación de indicador a periodo si este no tiene un id", Response.Status.BAD_REQUEST.getStatusCode());
        }

        PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigmentOrg = this.periodPerformanceIndicatorAssigmentDao.getByPeriodIdAndPerformanceIndicatorId(periodPerformanceIndicatorAssigmentWeb.getPeriodWeb().getId(), periodPerformanceIndicatorAssigmentWeb.getPerformanceIndicatorWeb().getId());

        if (!periodPerformanceIndicatorAssigmentOrg.getId().equals(periodPerformanceIndicatorAssigmentWeb.getId())) {
            throw new GeneralAppException("El indicador " + periodPerformanceIndicatorAssigmentOrg.getPerformanceIndicator().getDescription() + " ya se encuentra adignado al periodo " + periodPerformanceIndicatorAssigmentOrg.getPeriod().getYear() + ".", Response.Status.CONFLICT.getStatusCode());
        }

        PeriodPerformanceIndicatorAssigment pN = this.periodPerformanceIndicatorAssigmentWebToPeriodPerformanceIndicatorAssigment(periodPerformanceIndicatorAssigmentWeb);

        periodPerformanceIndicatorAssigmentOrg.setState(pN.getState());
        periodPerformanceIndicatorAssigmentOrg.setMeasureType(pN.getMeasureType());
        periodPerformanceIndicatorAssigmentOrg.setDisaggregationType(pN.getDisaggregationType());
        periodPerformanceIndicatorAssigmentOrg.setPerformanceIndicator(pN.getPerformanceIndicator());
        periodPerformanceIndicatorAssigmentOrg.setPeriod(pN.getPeriod());


        this.validate(periodPerformanceIndicatorAssigmentOrg);

        this.periodPerformanceIndicatorAssigmentDao.update(periodPerformanceIndicatorAssigmentOrg);
        return periodPerformanceIndicatorAssigmentOrg.getId();
    }

    private void validate(PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment) throws GeneralAppException {
        if (periodPerformanceIndicatorAssigment.getDisaggregationType() == null) {
            throw new GeneralAppException("El tipo de desagregación es requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (periodPerformanceIndicatorAssigment.getMeasureType() == null) {
            throw new GeneralAppException("El tipo de medida es requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (periodPerformanceIndicatorAssigment.getPerformanceIndicator() == null) {
            throw new GeneralAppException("El indicador es requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (periodPerformanceIndicatorAssigment.getPeriod() == null) {
            throw new GeneralAppException("El periodo es requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        if (periodPerformanceIndicatorAssigment.getState() == null) {
            throw new GeneralAppException("El estado es requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }


    }

    private PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigmentWebToPeriodPerformanceIndicatorAssigment(PeriodPerformanceIndicatorAssigmentWeb periodPerformanceIndicatorAssigmentWeb) {
        PeriodPerformanceIndicatorAssigment p = new PeriodPerformanceIndicatorAssigment();
        p.setId(periodPerformanceIndicatorAssigmentWeb.getId());
        p.setDisaggregationType(periodPerformanceIndicatorAssigmentWeb.getDisaggregationType());
        p.setMeasureType(periodPerformanceIndicatorAssigmentWeb.getMeasureType());
        p.setState(periodPerformanceIndicatorAssigmentWeb.getState());
        // periodo
        Period period = null;
        if (periodPerformanceIndicatorAssigmentWeb.getPeriodWeb() != null && periodPerformanceIndicatorAssigmentWeb.getPeriodWeb().getId() != null) {
            period = this.periodService.getById(periodPerformanceIndicatorAssigmentWeb.getPeriodWeb().getId());
        }

        p.setPeriod(period);
        //Indicador de performance
        PerformanceIndicator performanceIndicator = null;
        if (periodPerformanceIndicatorAssigmentWeb.getPerformanceIndicatorWeb() != null && periodPerformanceIndicatorAssigmentWeb.getPerformanceIndicatorWeb().getId() != null) {
            performanceIndicator = performanceIndicatorService.find(periodPerformanceIndicatorAssigmentWeb.getPerformanceIndicatorWeb().getId());
        }
        p.setPerformanceIndicator(performanceIndicator);
        return p;

    }

    private List<PeriodPerformanceIndicatorAssigmentWeb> periodPerformanceIndicatorAssigmentsToPeriodPerformanceIndicatorAssigmentWebs(List<PeriodPerformanceIndicatorAssigment> periodPerformanceIndicatorAssigments) {
        List<PeriodPerformanceIndicatorAssigmentWeb> result = new ArrayList<>();
        for (PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment : periodPerformanceIndicatorAssigments) {
            result.add(this.periodPerformanceIndicatorAssigmentToPeriodPerformanceIndicatorAssigmentWeb(periodPerformanceIndicatorAssigment));
        }

        return result;
    }

    private PeriodPerformanceIndicatorAssigmentWeb periodPerformanceIndicatorAssigmentToPeriodPerformanceIndicatorAssigmentWeb(PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment) {
        if (periodPerformanceIndicatorAssigment == null) return null;
        return
                new PeriodPerformanceIndicatorAssigmentWeb(periodPerformanceIndicatorAssigment.getId(), periodPerformanceIndicatorAssigment.getDisaggregationType(),
                        this.performanceIndicatorService.performanceIndicatorToPerformanceIndicatorWeb(periodPerformanceIndicatorAssigment.getPerformanceIndicator()),
                        periodPerformanceIndicatorAssigment.getState(), periodPerformanceIndicatorAssigment.getMeasureType(),
                        this.periodService.periodToPeriodWeb(periodPerformanceIndicatorAssigment.getPeriod())
                );

    }

    public List<PeriodPerformanceIndicatorAssigmentWeb> getWebByPeriodId(Long periodId) {
        return this.periodPerformanceIndicatorAssigmentsToPeriodPerformanceIndicatorAssigmentWebs(this.periodPerformanceIndicatorAssigmentDao.getByPeriodId(periodId));
    }
    public PeriodPerformanceIndicatorAssigmentWeb getWebByPeriodIdAndPerformanceIndicatorId(Long periodId, Long performanceIndicatorId) {
        return this.periodPerformanceIndicatorAssigmentToPeriodPerformanceIndicatorAssigmentWeb(this.getByPeriodIdAndPerformanceIndicatorId(periodId, performanceIndicatorId));
    }

    private PeriodPerformanceIndicatorAssigment getByPeriodIdAndPerformanceIndicatorId(Long periodId, Long performanceIndicatorId) {
        return this.periodPerformanceIndicatorAssigmentDao.getByPeriodIdAndPerformanceIndicatorId(periodId, performanceIndicatorId);
    }
}
