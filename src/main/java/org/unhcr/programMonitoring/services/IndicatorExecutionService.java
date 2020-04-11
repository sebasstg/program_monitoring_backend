package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.daos.IndicatorExecutionDao;
import org.unhcr.programMonitoring.model.*;
import org.unhcr.programMonitoring.webServices.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class IndicatorExecutionService {

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


    public List<IndicatorExecution> getByProjectId(Long projectId) {
        return this.indicatorExecutionDao.getByProjectId(projectId);
    }

    private IndicatorExecutionWeb indicatorExecutionToIndicatorExecutionWeb(IndicatorExecution indicatorExecution) {
        if (indicatorExecution == null) return null;

        ProjectWeb project = this.projectService.projectToProjectWeb(indicatorExecution.getProject());
        PerformanceIndicatorWeb performanceIndicator = this.performanceIndicatorService.performanceIndicatorToPerformanceIndicatorWeb(indicatorExecution.getPerformanceIndicator());
        OutputWeb outputWeb = this.outputService.outputToOutputWeb(indicatorExecution.getOutput());
        List<IndicatorExecutionLocationAssigmentWeb> locations = this.indicatorExecutionLocationAssigmenService.indicatorExecutionLocationAssigmentsToIndicatorExecutionLocationAssigmentWebs(indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments());
        SituationWeb situation = this.situationService.situationToSituationWeb(indicatorExecution.getSituation());
        IndicatorExecutionWeb i = new IndicatorExecutionWeb(indicatorExecution.getId(), indicatorExecution.getAttachmentDescription(), project,
                performanceIndicator, outputWeb, indicatorExecution.getState(), indicatorExecution.getDisaggregationType(), indicatorExecution.getTarget(),
                indicatorExecution.getTotalExecution(), indicatorExecution.getExecutionPercentage(), indicatorExecution.getIndicatorType(),
                locations, situation, indicatorExecution.getMeasureType());
        return i;
    }

    public IndicatorExecution createIndicatorExecution(Project project,
                                         PeriodPerformanceIndicatorAssigment periodPerformanceIndicatorAssigment,
                                         GeneralIndicator generalIndicator,
                                         Integer target,
                                         Situation situation) throws GeneralAppException {
        IndicatorExecution indicatorExecution = new IndicatorExecution();
        indicatorExecution.setAttachmentDescription(periodPerformanceIndicatorAssigment.getPerformanceIndicator().getDescription());
        indicatorExecution.setProject(project);
        indicatorExecution.setState(State.ACTIVE);
        indicatorExecution.setTotalExecution(0);
        indicatorExecution.setExecutionPercentage(0);

        DisaggregationType disaggregationType = null;

        if (periodPerformanceIndicatorAssigment != null) {
            indicatorExecution.setPerformanceIndicator(periodPerformanceIndicatorAssigment.getPerformanceIndicator());
            indicatorExecution.setDisaggregationType(periodPerformanceIndicatorAssigment.getDisaggregationType());
            indicatorExecution.setTarget(target);
            indicatorExecution.setIndicatorType(periodPerformanceIndicatorAssigment.getPerformanceIndicator().getIndicatorType());
            indicatorExecution.setSituation(situation);
            indicatorExecution.setMeasureType(periodPerformanceIndicatorAssigment.getMeasureType());
            disaggregationType = periodPerformanceIndicatorAssigment.getDisaggregationType();
        } else {
            indicatorExecution.setGeneralIndicator(generalIndicator);
            indicatorExecution.setDisaggregationType(generalIndicator.getDisaggregationType());
            indicatorExecution.setTarget(generalIndicator.getTarget());
            indicatorExecution.setIndicatorType(IndicatorType.GENERAL);
            indicatorExecution.setSituation(null);
            indicatorExecution.setMeasureType(generalIndicator.getMeasureType());
            disaggregationType = generalIndicator.getDisaggregationType();
        }

        Set<Canton> cantones = new HashSet<>();
        if (disaggregationType.equals(DisaggregationType.LOCATION) || disaggregationType.equals(DisaggregationType.AGE_LOCATION)
                || disaggregationType.equals(DisaggregationType.GENDER_LOCATION) || disaggregationType.equals(DisaggregationType.GENDER_AGE_LOCATION)
        ) {
            for (ProjectLocationAssigment projectLocationAssigment : project.getProjectLocationAssigments()) {
                // indicatorExecution.
                if (projectLocationAssigment.getState().equals(State.ACTIVE)) {
                    cantones.add(projectLocationAssigment.getLocation());
                }
            }
        }

        // genero los valores q se llenaran

        //this.indicatorValueService.

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

        this.indicatorValueService.createQuarters(values);


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


        for (Quarter quarter : this.indicatorValueService.createQuarters(values)) {
            indicatorExecution.addQuarter(quarter);
        }
//guarda

        this.projectService.saveOrUpdate(project);
        this.saveOrUpdate(indicatorExecution);
        for(IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment:indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments()){
            this.indicatorExecutionLocationAssigmenService.saveOrUpdate(indicatorExecutionLocationAssigment);
        }

        for(Quarter quarter:indicatorExecution.getQuarters()){
            this.quarterService.saveOrUpdate(quarter);
        }

        for (IndicatorValue value : values) {
            this.indicatorValueService.saveOrUpdate(value);
        }

        return indicatorExecution;


    }


    public List<IndicatorValue> createValuesForAllMonths() {

        return null;
    }


    protected IndicatorExecution saveOrUpdate(IndicatorExecution indicatorExecution) {
        if (indicatorExecution.getId() == null) {
            return this.indicatorExecutionDao.save(indicatorExecution);
        } else {
            return this.indicatorExecutionDao.update(indicatorExecution);
        }
    }
}
