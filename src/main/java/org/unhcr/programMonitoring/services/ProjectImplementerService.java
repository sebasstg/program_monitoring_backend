package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.model.ProjectImplementer;
import org.unhcr.programMonitoring.daos.ProjectImplementerDao;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectImplementerService {

    private static final Logger LOGGER = Logger.getLogger(ProjectImplementerService.class);

    @Inject
    ProjectImplementerDao projectImplementerDao;

    public ProjectImplementer getByUserId(Long userId) {
        return this.projectImplementerDao.getByUserId(userId);
    }

    public ProjectImplementerWeb getProjectImplementerWebByUserId(Long userId) {
        ProjectImplementerWeb projectImplementerWeb = null;
        ProjectImplementer implementer = this.getByUserId(userId);
        if (implementer != null) {
            projectImplementerWeb = this.projectImplementerToProjectImplementerWeb(implementer);
        }
        return projectImplementerWeb;
    }


    public List<ProjectImplementerWeb> getAll() {
        return this.projectImplementersToProjectImplementersWeb(this.projectImplementerDao.findAll());
    }


    public Long save(ProjectImplementerWeb projectImplementerWeb) throws GeneralAppException {
        if (projectImplementerWeb.getId() != null) {
            throw new GeneralAppException("El id es un dato conflictivo para crear una situación", Response.Status.BAD_REQUEST.getStatusCode());
        }
        this.validate(projectImplementerWeb);
        ProjectImplementer projectImplementer = this.projectImplementerWebToProjectImplementer(projectImplementerWeb);
        this.projectImplementerDao.save(projectImplementer);
        return projectImplementer.getId();
    }

    public Long update(ProjectImplementerWeb projectImplementerWeb) throws GeneralAppException {
        if (projectImplementerWeb.getId() == null) {
            throw new GeneralAppException("El id es un dato necesario para actualizar una situación", Response.Status.BAD_REQUEST.getStatusCode());
        }
        this.validate(projectImplementerWeb);

        ProjectImplementer projectImplementer = this.projectImplementerDao.find(projectImplementerWeb.getId());
        if (projectImplementer == null) {
            throw new GeneralAppException("El no se pudo encontrar un implementador de proyectos con id " + projectImplementerWeb.getId(), Response.Status.BAD_REQUEST.getStatusCode());
        }
        projectImplementer.setId(projectImplementerWeb.getId());
        projectImplementer.setCode(projectImplementerWeb.getCode());
        projectImplementer.setDescription(projectImplementerWeb.getDescription());
        projectImplementer.setState(projectImplementerWeb.getState());

        this.projectImplementerDao.update(projectImplementer);
        return projectImplementer.getId();
    }

    private void validate(ProjectImplementerWeb projectImplementerWeb) throws GeneralAppException {
        if (StringUtils.isBlank(projectImplementerWeb.getDescription())) {
            throw new GeneralAppException("La descripción es un dato requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (StringUtils.isBlank(projectImplementerWeb.getCode())) {
            throw new GeneralAppException("El código es un dato requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (projectImplementerWeb.getState() == null) {
            throw new GeneralAppException("Estado es un dato requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        // reviso si hay repetidos
        //code
        List<ProjectImplementer> byCodes = this.projectImplementerDao.getByCode(projectImplementerWeb.getCode());
        if (projectImplementerWeb.getId() == null && CollectionUtils.isNotEmpty(byCodes)) {
            throw new GeneralAppException("Ya existe una situación con el código: " + projectImplementerWeb.getCode(), Response.Status.CONFLICT.getStatusCode());
        }
        if (projectImplementerWeb.getId() != null && CollectionUtils.isNotEmpty(byCodes)) {
            for (ProjectImplementer projectImplementer : byCodes) {
                if (!projectImplementerWeb.getId().equals(projectImplementer.getId())) {
                    new GeneralAppException("Ya existe una situación con el código: " + projectImplementerWeb.getCode(), Response.Status.CONFLICT.getStatusCode());
                }
            }

        }

        //code
        List<ProjectImplementer> byDescription = this.projectImplementerDao.getByDescription(projectImplementerWeb.getDescription());
        if (projectImplementerWeb.getId() == null && CollectionUtils.isNotEmpty(byDescription)) {
            throw new GeneralAppException("Ya existe una situación con el el nombre: " + projectImplementerWeb.getDescription(), Response.Status.CONFLICT.getStatusCode());
        }
        if (projectImplementerWeb.getId() != null && CollectionUtils.isNotEmpty(byDescription)) {
            for (ProjectImplementer projectImplementer : byCodes) {
                if (!projectImplementerWeb.getId().equals(projectImplementer.getId())) {
                    throw new GeneralAppException("Ya existe una situación con el el nombre: " + projectImplementerWeb.getDescription(), Response.Status.CONFLICT.getStatusCode());
                }
            }
        }

    }

    public List<ProjectImplementerWeb> getByState(State state) {
        return this.projectImplementersToProjectImplementersWeb(this.projectImplementerDao.getByState(state));
    }


    public List<ProjectImplementerWeb> projectImplementersToProjectImplementersWeb(List<ProjectImplementer> projectImplementers) {
        List<ProjectImplementerWeb> result = new ArrayList<>();
        for (ProjectImplementer projectImplementer : projectImplementers) {
            result.add(this.projectImplementerToProjectImplementerWeb(projectImplementer));
        }
        return result;
    }

    private ProjectImplementerWeb projectImplementerToProjectImplementerWeb(ProjectImplementer projectImplementer) {

        if (projectImplementer == null) return null;
        return new ProjectImplementerWeb(projectImplementer.getId(), projectImplementer.getCode(), projectImplementer.getDescription(), projectImplementer.getState());
    }


    private ProjectImplementer projectImplementerWebToProjectImplementer(ProjectImplementerWeb projectImplementerWeb) {
        if (projectImplementerWeb == null) return null;
        ProjectImplementer projectImplementer = new ProjectImplementer();
        projectImplementer.setId(projectImplementerWeb.getId());
        projectImplementer.setCode(projectImplementerWeb.getCode());
        projectImplementer.setDescription(projectImplementerWeb.getDescription());
        projectImplementer.setState(projectImplementerWeb.getState());
        return projectImplementer;
    }

    public ProjectImplementerWeb getProjectImplementerWebById(Long id) {
        return this.projectImplementerToProjectImplementerWeb(this.projectImplementerDao.find(id));
    }

}
