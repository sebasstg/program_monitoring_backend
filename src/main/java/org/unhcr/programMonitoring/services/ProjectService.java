package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ProjectDao;
import org.unhcr.programMonitoring.daos.SituationAssigmentDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.PeriodWeb;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;
import org.unhcr.programMonitoring.webServices.model.ProjectWeb;
import org.unhcr.programMonitoring.webServices.model.SituationWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Stateless
public class ProjectService {

    private static final Logger LOGGER = Logger.getLogger(ProjectService.class);

    @Inject
    ProjectDao projectDao;

    @Inject
    SituationAssigmentDao situationAssigmentDao;

    @Inject
    ProjectImplementerService projectImplementerService;

    @Inject
    PeriodService periodService;

    @Inject
    SituationService situationService;


    public ProjectWeb getWebById(Long id) {
        Project project = this.projectDao.find(id);

        return this.projectToProjectWeb(project);

    }

    public Project getById(Long id) {
        return this.projectDao.find(id);
    }

    public ProjectWeb projectToProjectWeb(Project project) {
        if (project == null) {
            return null;
        } else {
            ProjectImplementerWeb projectImplentorWeb = null;
            PeriodWeb periodWeb = null;
            if (project.getPeriod() != null) {
                periodWeb = this.periodService.getPeriodWebById(project.getPeriod().getId());
            }
            if (project.getProjectImplementer() != null) {
                projectImplentorWeb = this.projectImplementerService.getProjectImplementerWebById(project.getProjectImplementer().getId());
            }

            // las situaciones
            Set<SituationAssigment> situacions = project.getSituationAssigments();

            ProjectWeb projectWeb = new ProjectWeb(project.getId(), project.getName(), project.getReportingStartingDate(), project.getReportingFinishingDate(), project.getState(), periodWeb, projectImplentorWeb);
            List<SituationWeb> situacionsWeb = this.situationAssigmentsToSituationsWeb(new ArrayList<>(situacions));
            projectWeb.setSituationWeb(situacionsWeb);
            return projectWeb;
        }
    }

    public Long save(ProjectWeb projectWeb) throws GeneralAppException {

        if (projectWeb.getId() != null) {
            throw new GeneralAppException("El id del proyecto debe ser null si se va a crear un nuevo proyecto.", Response.Status.BAD_REQUEST.getStatusCode());
        }
        this.validate(projectWeb);
        Project project = this.projectWebToProject(projectWeb);


        List<SituationAssigment> situationAssigments = this.situationsToSituationAssigments(projectWeb.getSituationWeb());

        for (SituationAssigment situationAssigment : situationAssigments) {
            project.addSituationAssigment(situationAssigment);
        }


        this.projectDao.save(project);

        for (SituationAssigment situationAssigment : project.getSituationAssigments()) {
            this.situationAssigmentDao.save(situationAssigment);
        }
        return project.getId();
    }

    private List<SituationAssigment> situationsToSituationAssigments(List<SituationWeb> situationsWeb) {
        List<SituationAssigment> result = new ArrayList<>();
        for (SituationWeb situationWeb : situationsWeb) {
            if (situationWeb != null && situationWeb.getId() != null) {
                Situation situation = this.situationService.find(situationWeb.getId());
                if (situation != null) {
                    SituationAssigment situationAssigment = new SituationAssigment();
                    situationAssigment.setSituation(situation);
                    situationAssigment.setState(State.ACTIVE);
                    result.add(situationAssigment);
                }
            }
        }
        return result;
    }

    private List<SituationWeb> situationAssigmentsToSituationsWeb(List<SituationAssigment> situationAssigments) {
        List<SituationWeb> r = new ArrayList<>();
        for (SituationAssigment situationAssigment : situationAssigments) {
            r.add(this.situationAssigmentToSituationWeb(situationAssigment));
        }

        return r;

    }

    private SituationWeb situationAssigmentToSituationWeb(SituationAssigment situationAssigment) {

        SituationWeb situationWeb = new SituationWeb();
        situationWeb.setId(situationAssigment.getSituation().getId());
        situationWeb.setCode(situationAssigment.getSituation().getCode());
        situationWeb.setDescription(situationAssigment.getSituation().getDescription());
        boolean active = false;
        if (situationAssigment.getState().equals(State.ACTIVE) && situationAssigment.getSituation().getState().equals(State.ACTIVE)) {
            active = true;
        }
        situationWeb.setState(active ? State.ACTIVE : State.INACTIVE);
        return situationWeb;
    }

    public List<ProjectWeb> getProjectWebByState(State state) {
        List<Project> result = this.getByState(state);
        return this.projectsToProjectWebs(result);

    }

    private List<ProjectWeb> projectsToProjectWebs(List<Project> projectWebs) {
        List<ProjectWeb> r = new ArrayList<>();
        for (Project project : projectWebs) {
            r.add(this.projectToProjectWeb(project));
        }
        return r;
    }

    private List<Project> getByState(State state) {
        return this.projectDao.getByState(state);
    }

    public Long update(ProjectWeb projectWeb) throws GeneralAppException {
        if (projectWeb.getId() == null) {
            throw new GeneralAppException("El id del proyecto no puede ser null si se va a actualizar el proyecto.", Response.Status.BAD_REQUEST.getStatusCode());
        }
        this.validate(projectWeb);

        Project project = this.projectDao.find(projectWeb.getId());//this.projectWebToProject(projectWeb);
        project.setState(projectWeb.getState());
        project.setName(projectWeb.getName());
        project.setReportingStartingDate(projectWeb.getReportingStartingDate());
        project.setReportingFinishingDate(projectWeb.getReportingFinishingDate());

        this.projectDao.update(project);
        return project.getId();
    }

    private void validate(ProjectWeb projectWeb) throws GeneralAppException {
        if (projectWeb == null) {
            throw new GeneralAppException("El proyecto es null", Response.Status.BAD_REQUEST.getStatusCode());
        } else {
            if (projectWeb.getName() == null) {
                throw new GeneralAppException("El año es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }
            if (projectWeb.getPeriodWeb() == null) {
                throw new GeneralAppException("El estado es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }
            if (projectWeb.getProjectImplementerWeb() == null) {
                throw new GeneralAppException("El socio implementador es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }
            if (projectWeb.getReportingStartingDate() == null) {
                throw new GeneralAppException("La fecha de inicio de reportes es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }

            if (projectWeb.getReportingFinishingDate() == null) {
                throw new GeneralAppException("La fecha de fin de reportes es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }

            // compruebo q no haya 2 para el mismo año para el mismo implementador y con el mismo nombre

            List<Project> result = this.projectDao.getByNamePeriodAndImplementorId(projectWeb.getName(), projectWeb.getPeriodWeb().getId(), projectWeb.getProjectImplementerWeb().getId());

            if (projectWeb.getId() == null && CollectionUtils.isNotEmpty(result)) {
                throw new GeneralAppException("Ya existe un proyecto implementado por " + result.get(0).getProjectImplementer().getDescription() + " para el periodo " + result.get(0).getPeriod().getYear() + ".", Response.Status.CONFLICT.getStatusCode());
            }
            if (projectWeb.getId() != null && CollectionUtils.isNotEmpty(result)) {
                for (Project project : result) {
                    if (projectWeb.getId().equals(project.getId())) {
                        throw new GeneralAppException("Ya existe un proyecto implementado por " + project.getProjectImplementer().getDescription() + " para el periodo " + project.getPeriod().getYear() + ".", Response.Status.CONFLICT.getStatusCode());
                    }

                }
            }
        }
    }

    private List<Project> getByPeriodId(Long periodId) {
        return this.projectDao.getByPeriodId(periodId);
    }

    private List<Project> getByImplementerId(Long idImplementer) {
        return this.projectDao.getByImplementerId(idImplementer);
    }

    private Project projectWebToProject(ProjectWeb projectWeb) {
        if (projectWeb == null) {
            return null;
        }
        Project project = new Project();
        project.setId(projectWeb.getId());
        project.setName(projectWeb.getName());
        project.setState(projectWeb.getState());
        project.setReportingFinishingDate(projectWeb.getReportingFinishingDate());
        project.setReportingStartingDate(projectWeb.getReportingFinishingDate());
        ProjectImplementer projectImplementer = null;
        if (projectWeb.getProjectImplementerWeb() != null && projectWeb.getProjectImplementerWeb().getId() != null) {
            projectImplementer = this.projectImplementerService.find(1l);
        }
        Period period = null;
        if (projectWeb.getPeriodWeb() != null && projectWeb.getPeriodWeb().getId() != null) {
            period = this.periodService.find(1l);
        }
        project.setPeriod(period);
        project.setProjectImplementer(projectImplementer);
        return project;
    }

    public List<ProjectWeb> getAllWeb() {
        List<Project> projects = this.projectDao.findAll();
        return this.projectsToProjectWebs(projects);
    }

    public List<ProjectWeb> getWebByState(State active) {

        return this.projectsToProjectWebs(this.projectDao.getByState(State.ACTIVE));
    }
}
