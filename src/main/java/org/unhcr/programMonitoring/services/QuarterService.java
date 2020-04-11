package org.unhcr.programMonitoring.services;

import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.CantonDao;
import org.unhcr.programMonitoring.daos.QuarterDao;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.Quarter;
import org.unhcr.programMonitoring.webServices.model.CantonWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class QuarterService {

    private static final Logger LOGGER = Logger.getLogger(QuarterService.class);

    @Inject
    QuarterDao quarterDao;

    protected Quarter saveOrUpdate(Quarter quarter){
        if(quarter.getId()==null){
            return this.quarterDao.save(quarter);
        }else{
            return this.quarterDao.update(quarter);
        }
    }

}
