package org.unhcr.programMonitoring.services;

import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.model.ProjectImplementer;
import org.unhcr.programMonitoring.daos.ProjectImplementerDao;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ProjectImplementerService {

    private static final Logger LOGGER = Logger.getLogger(ProjectImplementerService.class);

    @Inject
    ProjectImplementerDao projectImplementerDao;

    public ProjectImplementer getByUserId(Long userId) {
        return this.projectImplementerDao.getByUserId(userId);
    }

    public ProjectImplementerWeb getProjectImplementerWebByUserId(Long userId) {
        ProjectImplementerWeb projectImplementerWeb=null;
        ProjectImplementer implementer = this.getByUserId(userId);
        if(implementer!=null){
            projectImplementerWeb=this.projectImplementerToProjectImplementWeb(implementer);
        }
        return projectImplementerWeb;
    }

    private ProjectImplementerWeb projectImplementerToProjectImplementWeb(ProjectImplementer projectImplementer){

        return new ProjectImplementerWeb(projectImplementer.getId(), projectImplementer.getCode(), projectImplementer.getDescription(), projectImplementer.getState());
    }

}
