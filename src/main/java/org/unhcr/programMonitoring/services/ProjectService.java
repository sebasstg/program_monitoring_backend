package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ProjectDao;
import org.unhcr.programMonitoring.daos.SituationAssigmentDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Stateless
public class ProjectService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(ProjectService.class);

    @Inject
    ProjectDao projectDao;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    SituationAssigmentDao situationAssigmentDao;

    @Inject
    ProjectImplementerService projectImplementerService;

    @Inject
    PeriodService periodService;

    @Inject
    SituationService situationService;

    @Inject
    ProjectLocationAssigmentService projectLocationAssigmentService;


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

            ProjectWeb projectWeb = new ProjectWeb(project.getId(), project.getCode(), project.getName(), project.getState(), periodWeb, projectImplentorWeb, project.getTarget());
            List<SituationWeb> situacionsWeb = this.situationAssigmentsToSituationsWeb(new ArrayList<>(situacions));
            projectWeb.setSituationWeb(situacionsWeb);

            projectWeb.setProjectLocations(this.projectLocationAssigmentService.getWebByProjectId(project.getId()));
            projectWeb.setTarget(project.getTarget());
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

        if (CollectionUtils.isNotEmpty(projectWeb.getProjectLocations())) {
            this.projectLocationAssigmentService.saveOrUpdate(projectWeb.getProjectLocations(), project);
        }

// creo o actualizo los general indicators

        this.indicatorExecutionService.createGeneralIndicatorsForProject(project);


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
        project.setCode(projectWeb.getCode());
        project.setTarget(projectWeb.getTarget());


        this.projectDao.update(project);


        if (CollectionUtils.isNotEmpty(projectWeb.getProjectLocations())) {
            this.projectLocationAssigmentService.saveOrUpdate(projectWeb.getProjectLocations(), project);
        }
        // creo o actualizo los general indicators

        this.indicatorExecutionService.createGeneralIndicatorsForProject(project);
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
                throw new GeneralAppException("El periodo es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }
            if (projectWeb.getProjectImplementerWeb() == null) {
                throw new GeneralAppException("El socio implementador es un dato obligatorio en el proyecto", Response.Status.BAD_REQUEST.getStatusCode());
            }


            // compruebo q no haya 2 para el mismo año para el mismo implementador y con el mismo nombre

            List<Project> result = this.projectDao.getByNamePeriodAndImplementorId(projectWeb.getName(), projectWeb.getPeriodWeb().getId(), projectWeb.getProjectImplementerWeb().getId());

            if (projectWeb.getId() == null && CollectionUtils.isNotEmpty(result)) {
                throw new GeneralAppException("Ya existe un proyecto implementado por " + result.get(0).getProjectImplementer().getDescription() + " para el periodo " + result.get(0).getPeriod().getYear() + ".", Response.Status.CONFLICT.getStatusCode());
            }
            if (projectWeb.getId() != null && CollectionUtils.isNotEmpty(result)) {
                for (Project project : result) {
                    if (!projectWeb.getId().equals(project.getId())) {
                        throw new GeneralAppException("Ya existe un proyecto implementado por " + project.getProjectImplementer().getDescription() + " para el periodo " + project.getPeriod().getYear() + ".", Response.Status.CONFLICT.getStatusCode());
                    }

                }


            }
        }
    }


    public List<ProjectResumeWeb> getResumeWebByPeriodId(Long periodId) {
        return this.projectsToProjectResumeWebs(this.getByPeriodId(periodId));
    }

    private List<ProjectResumeWeb> projectsToProjectResumeWebs(List<Project> projects) {
        List<ProjectResumeWeb> projectResumens = new ArrayList<>();
        for (Project project : projects) {
            projectResumens.add(this.projectToProjectResumeWeb(project));
        }
        return projectResumens;
    }

    private ProjectResumeWeb projectToProjectResumeWeb(Project project) {
        if (project == null) return null;

        ProjectResumeWeb projectResumeWeb = new ProjectResumeWeb();
        projectResumeWeb.setId(project.getId());
        projectResumeWeb.setCode(project.getCode());
        projectResumeWeb.setName(project.getName());
        projectResumeWeb.setProgressPercentaje(null); //todo calcular
        projectResumeWeb.setReportedProgress(null); //todo calcular
        projectResumeWeb.setLastReportedMonth(null); //todo calcular
        projectResumeWeb.setProjectImplementer(this.projectImplementerService.projectImplementerToProjectImplementerWeb(project.getProjectImplementer()));

        List<Situation> situations = new ArrayList<>();
        for (SituationAssigment situationAssigment : project.getSituationAssigments()) {
            situations.add(situationAssigment.getSituation());
        }
        projectResumeWeb.setSituations(this.situationService.situationsToSituationsWeb(situations));


        projectResumeWeb.setTarget(project.getTarget());

        projectResumeWeb.setState(project.getState());

        return projectResumeWeb;
    }

    private List<Project> getByPeriodId(Long periodId) {
        return this.projectDao.getByPeriodId(periodId);
    }

    private List<ProjectWeb> getWebByPeriodId(Long periodId) {
        return this.projectsToProjectWebs(this.getByPeriodId(periodId));
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
        ProjectImplementer projectImplementer = null;
        if (projectWeb.getProjectImplementerWeb() != null && projectWeb.getProjectImplementerWeb().getId() != null) {
            projectImplementer = this.projectImplementerService.find(projectWeb.getProjectImplementerWeb().getId());
        }
        Period period = null;
        if (projectWeb.getPeriodWeb() != null && projectWeb.getPeriodWeb().getId() != null) {
            period = this.periodService.find(projectWeb.getPeriodWeb().getId());
        }
        project.setPeriod(period);
        project.setProjectImplementer(projectImplementer);
        project.setCode(projectWeb.getCode());
        project.setTarget(projectWeb.getTarget());
        return project;
    }

    public List<ProjectWeb> getAllWeb() {
        List<Project> projects = this.projectDao.findAll();
        return this.projectsToProjectWebs(projects);
    }

    public List<ProjectWeb> getWebByState(State active) {

        return this.projectsToProjectWebs(this.projectDao.getByState(State.ACTIVE));
    }

    public Project saveOrUpdate(Project project) {
        if (project.getId() == null) {
            return this.projectDao.save(project);
        } else {
            return this.projectDao.update(project);
        }
    }

    public List<ProjectResumeWeb> getByPeriodIdAndImplementerId(Long periodId, Long implementerId) {
        return this.projectsToProjectResumeWebs(this.projectDao.getByPeriodIdAndImplementerId(periodId,implementerId));

    }
}
