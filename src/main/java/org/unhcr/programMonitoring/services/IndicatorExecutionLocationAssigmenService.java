package org.unhcr.programMonitoring.services;

import org.unhcr.programMonitoring.daos.IndicatorExecutionLocationAssigmentDao;
import org.unhcr.programMonitoring.model.IndicatorExecutionLocationAssigment;
import org.unhcr.programMonitoring.webServices.model.IndicatorExecutionLocationAssigmentWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

    protected IndicatorExecutionLocationAssigment saveOrUpdate(IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment){
        if(indicatorExecutionLocationAssigment.getId()==null){
            return this.indicatorExecutionLocationAssigmentDao.save(indicatorExecutionLocationAssigment);
        }else{
            return this.indicatorExecutionLocationAssigmentDao.update(indicatorExecutionLocationAssigment);
        }

    }
}
