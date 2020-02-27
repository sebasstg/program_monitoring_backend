package org.unhcr.programMonitoring.services;

import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ProjectImplementerDao;
import org.unhcr.programMonitoring.daos.RightGroupDao;
import org.unhcr.programMonitoring.model.ProjectImplementer;
import org.unhcr.programMonitoring.model.RightGroup;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class RightGroupService {

    private static final Logger LOGGER = Logger.getLogger(RightGroupService.class);

    @Inject
    RightGroupDao rightGroupDao;

    public List<RightGroup> getAllOrderedByCode() {
       return this.rightGroupDao.getAllOrderedByCode();

    }

}
