package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.unhcr.programMonitoring.daos.IndicatorExecutionLocationAssigmentDao;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.IndicatorExecution;
import org.unhcr.programMonitoring.model.IndicatorExecutionLocationAssigment;
import org.unhcr.programMonitoring.webServices.model.IndicatorExecutionLocationAssigmentWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Stateless
public class IndicatorExecutionLocationAssigmenService {

    @Inject
    CantonService cantonService;

    @Inject
    IndicatorExecutionLocationAssigmentDao indicatorExecutionLocationAssigmentDao;


    public List<IndicatorExecutionLocationAssigmentWeb> indicatorExecutionLocationAssigmentsToIndicatorExecutionLocationAssigmentWebs(Set<IndicatorExecutionLocationAssigment> performanceIndicatorExecutionLocationAssigments) {
        List<IndicatorExecutionLocationAssigmentWeb> r = new ArrayList<>();
        for (IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment : performanceIndicatorExecutionLocationAssigments) {
            r.add(this.indicatorExecutionLocationAssigmentToIndicatorExecutionLocationAssigment(indicatorExecutionLocationAssigment));

        }
        return r;
    }

    private IndicatorExecutionLocationAssigmentWeb indicatorExecutionLocationAssigmentToIndicatorExecutionLocationAssigment(IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment) {

        IndicatorExecutionLocationAssigmentWeb r = new IndicatorExecutionLocationAssigmentWeb(indicatorExecutionLocationAssigment.getId(),
                this.cantonService.cantonToCantonWeb(indicatorExecutionLocationAssigment.getLocation()), indicatorExecutionLocationAssigment.getState());
        return r;
    }

    public IndicatorExecutionLocationAssigment saveOrUpdate(IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment) {
        if (indicatorExecutionLocationAssigment.getId() == null) {
            return this.indicatorExecutionLocationAssigmentDao.save(indicatorExecutionLocationAssigment);
        } else {
            return this.indicatorExecutionLocationAssigmentDao.update(indicatorExecutionLocationAssigment);
        }

    }


    public IndicatorExecutionLocationAssigment save(IndicatorExecutionLocationAssigmentWeb indicatorExecutionLocationAssigmentWeb, IndicatorExecution indicatorExecution) {
        // primero valido que este no vaya a estar ya
        IndicatorExecutionLocationAssigment org = this.indicatorExecutionLocationAssigmentDao.getByIndicatorExecutionIdAndLocationId(indicatorExecution.getId(), indicatorExecutionLocationAssigmentWeb.getLocation().getId());

        if (org == null) {
            IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment = new IndicatorExecutionLocationAssigment();
            //indicatorExecutionLocationAssigment.setIndicatorExecution(indicatorExecution);
            Canton canton = this.cantonService.find(indicatorExecutionLocationAssigmentWeb.getLocation().getId());
            indicatorExecutionLocationAssigment.setLocation(canton);
            indicatorExecutionLocationAssigment.setState(indicatorExecutionLocationAssigmentWeb.getState());
            indicatorExecution.addPerformanceIndicatorExecutionLocationAssigments(indicatorExecutionLocationAssigment);
            return this.indicatorExecutionLocationAssigmentDao.save(indicatorExecutionLocationAssigment);
        } else {
            // solo puedo actualizar el estado
            org.setState(indicatorExecutionLocationAssigmentWeb.getState());
            return this.indicatorExecutionLocationAssigmentDao.update(org);
        }

    }

    public IndicatorExecutionLocationAssigment update(IndicatorExecutionLocationAssigmentWeb indicatorExecutionLocationAssigmentWeb) throws GeneralAppException {
        IndicatorExecutionLocationAssigment org = this.indicatorExecutionLocationAssigmentDao.find(indicatorExecutionLocationAssigmentWeb.getId());

        if (org == null) {
            throw  new GeneralAppException("No se pudo encontrar la asignación de localización con id "+ indicatorExecutionLocationAssigmentWeb.getId(), Response.Status.BAD_REQUEST.getStatusCode());
        } else {
            // solo puedo actualizar el estado
            org.setState(indicatorExecutionLocationAssigmentWeb.getState());
            return this.indicatorExecutionLocationAssigmentDao.update(org);
        }
    }
}
