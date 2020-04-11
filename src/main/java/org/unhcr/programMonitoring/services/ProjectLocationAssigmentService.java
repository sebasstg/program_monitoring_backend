package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ProjectLocationAssigmentDao;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.Project;
import org.unhcr.programMonitoring.model.ProjectLocationAssigment;
import org.unhcr.programMonitoring.webServices.model.ProjectLocationWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class ProjectLocationAssigmentService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(ProjectLocationAssigmentService.class);

    @Inject
    CantonService cantonService;

    @Inject
    ProjectLocationAssigmentDao projectLocationAssigmentDao;

    public List<ProjectLocationAssigment> getByProjectId(Long projectId) {
        return this.projectLocationAssigmentDao.getByProjectId(projectId);
    }

    public List<ProjectLocationWeb> getWebByProjectId(Long projectId) {
        return this.projectsToProjectWebs(this.getByProjectId(projectId));
    }

    private ProjectLocationWeb projectToProjectWeb(ProjectLocationAssigment projectLocationAssigment) {
        if (projectLocationAssigment == null) return null;
        return new ProjectLocationWeb(
                projectLocationAssigment.getId(),
                this.cantonService.cantonToCantonWeb(projectLocationAssigment.getLocation()),
                projectLocationAssigment.getState());
    }

    private List<ProjectLocationWeb> projectsToProjectWebs(List<ProjectLocationAssigment> projectLocationAssigments) {
        List<ProjectLocationWeb> projectsToProjectWebs = new ArrayList<>();
        if (projectLocationAssigments == null) return projectsToProjectWebs;
        for (ProjectLocationAssigment projectLocationAssigment : projectLocationAssigments) {
            projectsToProjectWebs.add(this.projectToProjectWeb(projectLocationAssigment));
        }
        return projectsToProjectWebs;
    }


    public List<ProjectLocationAssigment> saveOrUpdate(List<ProjectLocationWeb> projectLocations, Project project) throws GeneralAppException {
        List<ProjectLocationAssigment> result = new ArrayList<>();
        Set<ProjectLocationWeb> set = new HashSet<ProjectLocationWeb>(projectLocations);

        for (ProjectLocationWeb projectLocationWeb : set) {
            result.add(this.saveOrUpdate(projectLocationWeb, project));
        }
        return result;
    }

    public ProjectLocationAssigment saveOrUpdate(ProjectLocationWeb projectLocationWeb, Project project) throws GeneralAppException {
        if (projectLocationWeb.getId() != null) {
            ProjectLocationAssigment projectLocation = this.projectLocationAssigmentDao.find(projectLocationWeb.getId());
            if (projectLocation == null) {
                throw new GeneralAppException("No se pudo encontrar la localización con id " + projectLocation.getId() + " para el proyecto con id " + project.getId(), Response.Status.BAD_REQUEST.getStatusCode());
            }
            projectLocation.setState(projectLocationWeb.getState());
            return this.projectLocationAssigmentDao.update(projectLocation);

        } else {
            List<ProjectLocationAssigment> locationsAss = this.projectLocationAssigmentDao.getByProjectIdAndCantonId(projectLocationWeb.getId(), projectLocationWeb.getCanton().getId());
            if (CollectionUtils.isNotEmpty(locationsAss)) {
                if (locationsAss.size() > 1) {
                    // no debería pasar nunca
                    throw new GeneralAppException("Error de consistencia de datos, comuniquese con el administrador del sistema Error para el proyecto con id " + project.getId(), Response.Status.BAD_REQUEST.getStatusCode());
                }
                ProjectLocationAssigment projectLocation = locationsAss.get(0);
                projectLocation.setState(projectLocationWeb.getState());
                return this.projectLocationAssigmentDao.update(projectLocation);
            } else {

                if (projectLocationWeb.getCanton() == null) {
                    throw new GeneralAppException("El cantón es un dato obligatorio para la asiganción de localiziones al proyecto " + project.getId(), Response.Status.BAD_REQUEST.getStatusCode());
                }
                Canton canton = this.cantonService.find(projectLocationWeb.getCanton().getId());
                if (canton == null) {
                    throw new GeneralAppException("El cantón con id  " + projectLocationWeb.getCanton().getId() + " no se pudo encontrar.", Response.Status.BAD_REQUEST.getStatusCode());
                }
                ProjectLocationAssigment projectLocationAssigment = new ProjectLocationAssigment();
                ///projectLocationAssigment.setProject(project);
                projectLocationAssigment.setState(projectLocationWeb.getState());
                projectLocationAssigment.setLocation(canton);
                project.addProjectLocationAssigment(projectLocationAssigment);
                return this.projectLocationAssigmentDao.save(projectLocationAssigment);


            }

        }
    }
}
