package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.IndicatorExecutionDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;

@Stateless
public class IndicatorExecutionService {

    private static final Logger LOGGER = Logger.getLogger(IndicatorExecutionService.class);

    @Inject
    IndicatorExecutionDao indicatorExecutionDao;

    @Inject
    ProjectService projectService;

    @Inject
    PerformanceIndicatorService performanceIndicatorService;

    @Inject
    OutputService outputService;

    @Inject
    IndicatorExecutionLocationAssigmenService indicatorExecutionLocationAssigmenService;

    @Inject
    SituationService situationService;

    @Inject
    IndicatorValueService indicatorValueService;

    @Inject
    QuarterService quarterService;

    @Inject
    GeneralIndicatorService generalIndicatorService;

    @Inject
    PeriodPerformanceIndicatorAssigmentService periodPerformanceIndicatorAssigmentService;

    private List<IndicatorExecutionWeb> indicatorExecutionsToIndicatorExecutionWebs(List<IndicatorExecution> indicatorExecutions) {
        List<IndicatorExecutionWeb> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            r.add(this.indicatorExecutionToIndicatorExecutionWeb(indicatorExecution));
        }
        return r;
    }

    private IndicatorExecutionWeb indicatorExecutionToIndicatorExecutionWeb(IndicatorExecution indicatorExecution) {
        if (indicatorExecution == null) return null;

        ProjectWeb project = this.projectService.projectToProjectWeb(indicatorExecution.getProject());

        OutputWeb outputWeb = this.outputService.outputToOutputWeb(indicatorExecution.getOutput());
        List<IndicatorExecutionLocationAssigmentWeb> locations = this.indicatorExecutionLocationAssigmenService.indicatorExecutionLocationAssigmentsToIndicatorExecutionLocationAssigmentWebs(indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments());
        SituationWeb situation = this.situationService.situationToSituationWeb(indicatorExecution.getSituation());
        PerformanceIndicatorWeb performanceIndicator = null;
        GeneralIndicatorWeb generalIndicatorWeb = null;
        if (indicatorExecution.getPerformanceIndicator() != null) {
            performanceIndicator = this.performanceIndicatorService.performanceIndicatorToPerformanceIndicatorWeb(indicatorExecution.getPerformanceIndicator());
        } else {
            generalIndicatorWeb = this.generalIndicatorService.generalIndicatorToGeneralIndicatorWeb(indicatorExecution.getGeneralIndicator());
        }
        List<IndicatorExecutionLocationAssigmentWeb> indicatorLocation = this.indicatorExecutionLocationAssigmenService.indicatorExecutionLocationAssigmentsToIndicatorExecutionLocationAssigmentWebs(indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments());
        List<QuarterWeb> quarterWebs = new ArrayList<>();
        if (indicatorExecution.getQuarters() != null) {
            quarterWebs = this.quarterService.quartersToQuarterWebs(new ArrayList<>(indicatorExecution.getQuarters()));
        }
        return new IndicatorExecutionWeb(indicatorExecution.getId(), indicatorExecution.getAttachmentDescription(), project, performanceIndicator, outputWeb, generalIndicatorWeb, indicatorExecution.getState(), indicatorExecution.getDisaggregationType(), indicatorExecution.getTarget(), indicatorExecution.getTotalExecution(), indicatorExecution.getExecutionPercentage(), indicatorExecution.getIndicatorType(), indicatorLocation, situation, indicatorExecution.getMeasureType(), quarterWebs);
    }


    public Long createIndicatorExecution(IndicatorExecutionWeb indicatorExecutionWeb) throws GeneralAppException {
        Project project = this.projectService.getById(indicatorExecutionWeb.getProject().getId());
        PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment =
                this.periodPerformanceIndicatorAssigmentService.getByPeriodIdAndPerformanceIndicatorId(project.getPeriod().getId(), indicatorExecutionWeb.getPerformanceIndicator().getId());
        Situation situation = this.situationService.find(indicatorExecutionWeb.getSituation().getId());

        List<IndicatorExecution> r = this.indicatorExecutionDao.getPerformanceIndicatorByProjectIdAndIndicatorId(project.getId(), periodPerformanceIndicatorAssigment.getPerformanceIndicator().getId());
        if (CollectionUtils.isNotEmpty(r)) {
            throw new GeneralAppException("Este indicador ya está asignado al proyecto.", Response.Status.BAD_REQUEST.getStatusCode());
        }

        IndicatorExecution ind = this.createIndicatorExecution(project, periodPerformanceIndicatorAssigment, null,
                indicatorExecutionWeb.getTarget(),
                situation);

        return ind.getId();
    }

    public Long updateIndicatorExecution(IndicatorExecutionWeb indicatorExecutionWeb) throws GeneralAppException {
        // solo puedo actualizar localizaciones , meta y agregar desactivar localizaciones
        if (indicatorExecutionWeb.getId() == null) {
            throw new GeneralAppException("No se puede actualizar un indicador sin id", Response.Status.BAD_REQUEST.getStatusCode());
        }

        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.find(indicatorExecutionWeb.getId());

        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador con id " + indicatorExecutionWeb.getId(), Response.Status.BAD_REQUEST.getStatusCode());
        }

        indicatorExecution.setTarget(indicatorExecutionWeb.getTarget());

        Set<IndicatorExecutionLocationAssigment> locationAssigmentsOrg = indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments();


        List<IndicatorExecutionLocationAssigmentWeb> assigLoToAdd = new ArrayList<>();
        List<IndicatorExecutionLocationAssigmentWeb> assigLoToUpdate = new ArrayList<>();

        if (CollectionUtils.isEmpty(locationAssigmentsOrg) && CollectionUtils.isNotEmpty(indicatorExecutionWeb.getIndicatorExecutionLocationAssigment())) {
            // no habia y ahora hay todos nuevos
            assigLoToAdd = indicatorExecutionWeb.getIndicatorExecutionLocationAssigment();

        } else if (CollectionUtils.isNotEmpty(locationAssigmentsOrg) && CollectionUtils.isEmpty(indicatorExecutionWeb.getIndicatorExecutionLocationAssigment())) {
            // no se hace nada
        } else {
            if (CollectionUtils.isNotEmpty(locationAssigmentsOrg) && CollectionUtils.isNotEmpty(indicatorExecutionWeb.getIndicatorExecutionLocationAssigment())) {
                for (IndicatorExecutionLocationAssigmentWeb indicatorExecutionLocationAssigmentWeb : indicatorExecutionWeb.getIndicatorExecutionLocationAssigment()) {
                    if (indicatorExecutionLocationAssigmentWeb.getId() == null) {
                        assigLoToAdd.add(indicatorExecutionLocationAssigmentWeb);
                    } else {
                        assigLoToUpdate.add(indicatorExecutionLocationAssigmentWeb);
                    }
                }
            }
        }

        this.saveOrUpdate(indicatorExecution);
        for (IndicatorExecutionLocationAssigmentWeb indicatorExecutionLocationAssigmentWeb : assigLoToAdd) {
            this.indicatorExecutionLocationAssigmenService.save(indicatorExecutionLocationAssigmentWeb, indicatorExecution);
        }
        for (IndicatorExecutionLocationAssigmentWeb indicatorExecutionLocationAssigmentWeb : assigLoToUpdate) {
            this.indicatorExecutionLocationAssigmenService.save(indicatorExecutionLocationAssigmentWeb, indicatorExecution);
        }

        return indicatorExecution.getId();
    }

    private IndicatorExecution createIndicatorExecution(Project project,
                                                        PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment,
                                                        GeneralIndicator generalIndicator,
                                                        Integer target,
                                                        Situation situation) throws GeneralAppException {
        IndicatorExecution indicatorExecution = new IndicatorExecution();

        indicatorExecution.setProject(project);
        indicatorExecution.setState(State.ACTIVE);
        indicatorExecution.setTotalExecution(0);
        indicatorExecution.setExecutionPercentage(0);

        DisaggregationType disaggregationType = null;


        if (periodPerformanceIndicatorAssigment != null) {
            indicatorExecution.setAttachmentDescription(periodPerformanceIndicatorAssigment.getPerformanceIndicator().getDescription());
            indicatorExecution.setPerformanceIndicator(periodPerformanceIndicatorAssigment.getPerformanceIndicator());
            indicatorExecution.setOutput(periodPerformanceIndicatorAssigment.getPerformanceIndicator().getOutput());
            indicatorExecution.setDisaggregationType(periodPerformanceIndicatorAssigment.getDisaggregationType());
            indicatorExecution.setTarget(target);
            indicatorExecution.setIndicatorType(periodPerformanceIndicatorAssigment.getPerformanceIndicator().getIndicatorType());
            indicatorExecution.setSituation(situation);
            indicatorExecution.setMeasureType(periodPerformanceIndicatorAssigment.getMeasureType());
            disaggregationType = periodPerformanceIndicatorAssigment.getDisaggregationType();


        } else {
            indicatorExecution.setAttachmentDescription(generalIndicator.getDescription());
            indicatorExecution.setGeneralIndicator(generalIndicator);
            indicatorExecution.setDisaggregationType(generalIndicator.getDisaggregationType());
            indicatorExecution.setTarget(target);
            indicatorExecution.setIndicatorType(IndicatorType.GENERAL);
            indicatorExecution.setSituation(null);
            indicatorExecution.setMeasureType(generalIndicator.getMeasureType());
            disaggregationType = generalIndicator.getDisaggregationType();
        }

        Set<Canton> cantones = new HashSet<>();
        if (disaggregationType.equals(DisaggregationType.LOCATION)
                || disaggregationType.equals(DisaggregationType.AGE_LOCATION)
                || disaggregationType.equals(DisaggregationType.GENDER_LOCATION)
                || disaggregationType.equals(DisaggregationType.GENDER_AGE_LOCATION)
        ) {

            if (!indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
                for (ProjectLocationAssigment projectLocationAssigment : project.getProjectLocationAssigments()) {
                    // indicatorExecution.
                    if (projectLocationAssigment.getState().equals(State.ACTIVE)) {
                        cantones.add(projectLocationAssigment.getLocation());
                    }
                }
            } else {
                for (ProjectLocationAssigment projectLocationAssigment : project.getProjectLocationAssigments()) {
                    // general.
                    if (projectLocationAssigment.getState().equals(State.ACTIVE)) {
                        cantones.add(projectLocationAssigment.getLocation());
                    }
                }
            }
        }

        // genero los valores q se llenaran

        //this.indicatorValueService.

        List<IndicatorValue> values = this.createIndicatorValues(disaggregationType,cantones);

        //this.indicatorValueService.createQuarters(values);


        //guardar
        // //proyecto
        // indicator execution
        // indicator location
        //quarters
        // values

        project.addIndicatorExecution(indicatorExecution);

        // las asignaciones de localizaciones

        for (Canton canton : cantones) {
            IndicatorExecutionLocationAssigment i = new IndicatorExecutionLocationAssigment();
            i.setLocation(canton);
            i.setState(State.ACTIVE);
            indicatorExecution.addPerformanceIndicatorExecutionLocationAssigments(i);
        }


        for (IndicatorValue value : values) {
            indicatorExecution.addIndicatorValue(value);
        }

        List<Quarter> quarters = this.indicatorValueService.createQuarters(values);
        for (Quarter quarter : quarters) {
            indicatorExecution.addQuarter(quarter);
        }
//guarda

        this.projectService.saveOrUpdate(project);
        this.saveOrUpdate(indicatorExecution);
        for (IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment : indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments()) {
            this.indicatorExecutionLocationAssigmenService.saveOrUpdate(indicatorExecutionLocationAssigment);
        }

        for (Quarter quarter : indicatorExecution.getQuarters()) {
            this.quarterService.saveOrUpdate(quarter);
        }

        for (IndicatorValue value : values) {
            this.indicatorValueService.saveOrUpdate(value);
        }

        return indicatorExecution;


    }

    private IndicatorExecution createOnlyValuesForIndicatorExecution(IndicatorExecution indicatorExecution) throws GeneralAppException {
        Set<Canton> cantones= new HashSet<>();
        for(IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment:indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments()){
            cantones.add(indicatorExecutionLocationAssigment.getLocation());
        }

        List<IndicatorValue> values = this.createIndicatorValues(indicatorExecution.getDisaggregationType(), cantones);
        this.indicatorValueService.createQuarters(values);
        Project project= indicatorExecution.getProject();
        project.addIndicatorExecution(indicatorExecution);

        for (IndicatorValue value : values) {
            indicatorExecution.addIndicatorValue(value);
        }
        List<Quarter> quarters = this.indicatorValueService.createQuarters(values);
        for (Quarter quarter : quarters) {
            indicatorExecution.addQuarter(quarter);
            LOGGER.debug(indicatorExecution.getQuarters().size());

        }

        LOGGER.debug(indicatorExecution.getQuarters().size());
        //guarda

        for (Quarter quarter :quarters) {
            this.quarterService.saveOrUpdate(quarter);
        }

        for (IndicatorValue value : values) {
            this.indicatorValueService.saveOrUpdate(value);
        }

        this.projectService.saveOrUpdate(project);
        indicatorExecution.setTotalExecution(0);
        indicatorExecution.setExecutionPercentage(0);

        this.saveOrUpdate(indicatorExecution);


        return indicatorExecution;

    }

    private List<IndicatorValue> createIndicatorValues(DisaggregationType disaggregationType,Set<Canton> cantones  ){
        List<IndicatorValue> values = new ArrayList<>();
        switch (disaggregationType) {
            case NONE:
                values = this.indicatorValueService.createValuesForNoDisaggregation();
                break;
            case AGE:
                values = this.indicatorValueService.createValuesForAgeDisaggregation();
                break;
            case GENDER:
                values = this.indicatorValueService.createValuesForGenderDisaggregation();
                break;
            case LOCATION:
                values = this.indicatorValueService.createValuesForLocationDisaggregation(new ArrayList<>(cantones));
                break;
            case GENDER_AGE:
                values = this.indicatorValueService.createValuesForGenderAgeDisaggregation();
                break;
            case GENDER_LOCATION:
                values = this.indicatorValueService.createValuesForGenderLocationDisaggregation(new ArrayList<>(cantones));
                break;
            case AGE_LOCATION:
                values = this.indicatorValueService.createValuesForAgeLocationDisaggregation(new ArrayList<>(cantones));
                break;
            case GENDER_AGE_LOCATION:
                values = this.indicatorValueService.createValuesForGenderAgeLocationDisaggregation(new ArrayList<>(cantones));
                break;
        }
        return values;
    }


    public IndicatorExecution saveOrUpdate(IndicatorExecution indicatorExecution) {
        if (indicatorExecution.getId() == null) {
            return this.indicatorExecutionDao.save(indicatorExecution);
        } else {
            return this.indicatorExecutionDao.update(indicatorExecution);
        }
    }


    public List<IndicatorExecutionWeb> getPerformanceIndicatorByProjectId(Long projectId) {
        return this.indicatorExecutionsToIndicatorExecutionWebs(this.indicatorExecutionDao.getPerformanceIndicatorByProjectId(projectId));
    }
    public List<IndicatorExecutionWeb> getPerformanceIndicatorByProjectIdAndState(Long projectId, State state) throws GeneralAppException {

        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getPerformanceIndicatorByProjectIdAndState(projectId, state);
        //compruebo q tenga valores


        for(IndicatorExecution indicatorExecution:indicatorExecutions){
            if(CollectionUtils.isEmpty(indicatorExecution.getIndicatorValues())){
                // si no tiene valores
                this.createOnlyValuesForIndicatorExecution(indicatorExecution);

            }
        }


        return this.indicatorExecutionsToIndicatorExecutionWebs(indicatorExecutions);
    }



    public List<IndicatorExecutionWeb> getGeneralIndicatorByProjectId(Long projectId) {
        return this.indicatorExecutionsToIndicatorExecutionWebs(this.indicatorExecutionDao.getGeneralIndicators(projectId));
    }

    public void createGeneralIndicatorsForProject(Project project) throws GeneralAppException {
        List<IndicatorExecution> generalIndicatorOrg =
                this.indicatorExecutionDao.getGeneralIndicators(project.getId());

        if (CollectionUtils.isEmpty(generalIndicatorOrg)) {
            // hay q crear

            //recupero  lo q debo crear
            List<GeneralIndicator> generalIndicators = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVE);

            for (GeneralIndicator generalIndicator : generalIndicators) {
                this.createGeneralIndicatorExecutionForProject(project, generalIndicator);
            }

        } else {
            // si no esta vacio debe ver si hay nuevos que crear
            List<GeneralIndicator> generalIndicatorsToAdd = new ArrayList<>();
            List<IndicatorExecution> generalIndicatorsToUpdateState = new ArrayList<>();
            List<GeneralIndicator> generalIndicators = this.generalIndicatorService.getByPeriodId(project.getPeriod().getId());

            for (GeneralIndicator generalIndicatorP : generalIndicators) {
                boolean add = true;
                for (IndicatorExecution indicatorExecution : generalIndicatorOrg) {
                    if (generalIndicatorP.equals(indicatorExecution.getGeneralIndicator())) {
                        add = false;
                        if (!generalIndicatorP.getState().equals(indicatorExecution.getState())) {
                            generalIndicatorsToUpdateState.add(indicatorExecution);
                        }
                        break;
                    }
                }
                if (add) {
                    generalIndicatorsToAdd.add(generalIndicatorP);
                }
            }
            for (GeneralIndicator generalIndicator : generalIndicatorsToAdd) {
                this.createGeneralIndicatorExecutionForProject(project, generalIndicator);
            }

            for (IndicatorExecution indicatorExecution : generalIndicatorsToUpdateState) {
                if (indicatorExecution.getState().equals(State.ACTIVE)) {
                    indicatorExecution.setState(State.INACTIVE);
                } else {
                    indicatorExecution.setState(State.ACTIVE);
                }
                this.indicatorExecutionDao.update(indicatorExecution);
            }

        }

    }

    private IndicatorExecution createGeneralIndicatorExecutionForProject(Project project, GeneralIndicator generalIndicator) throws GeneralAppException {

        return this.createIndicatorExecution(project, null, generalIndicator, project.getTarget(), null);

    }

    public IndicatorExecution getByGeneralIndicatorIdAndProjectId(Long generalIndicatorId, Long projectId) {
        return this.indicatorExecutionDao.getByGeneralIndicatorIdAndProjectId(generalIndicatorId, projectId);
    }

}
