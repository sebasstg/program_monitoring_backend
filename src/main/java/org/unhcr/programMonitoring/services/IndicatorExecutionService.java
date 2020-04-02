package org.unhcr.programMonitoring.services;

import org.unhcr.programMonitoring.daos.IndicatorExecutionDao;
import org.unhcr.programMonitoring.model.IndicatorExecution;
import org.unhcr.programMonitoring.model.IndicatorExecutionLocationAssigment;
import org.unhcr.programMonitoring.model.PerformanceIndicator;
import org.unhcr.programMonitoring.webServices.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

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

    public List<IndicatorExecution> getByProjectId(Long projectId) {
        return this.indicatorExecutionDao.getByProjectId(projectId);
    }

    private IndicatorExecutionWeb indicatorExecutionToIndicatorExecutionWeb(IndicatorExecution indicatorExecution) {
        if (indicatorExecution == null) return null;

        ProjectWeb project = this.projectService.projectToProjectWeb(indicatorExecution.getProject());
        PerformanceIndicatorWeb performanceIndicator=this.performanceIndicatorService.performanceIndicatorToPerformanceIndicatorWeb(indicatorExecution.getPerformanceIndicator());
        OutputWeb outputWeb= this.outputService.outputToOutputWeb(indicatorExecution.getOutput());
        List<IndicatorExecutionLocationAssigmentWeb> locations = this.indicatorExecutionLocationAssigmenService.indicatorExecutionLocationAssigmentsToIndicatorExecutionLocationAssigmentWebs(indicatorExecution.getPerformanceIndicatorExecutionLocationAssigments());
        SituationWeb situation = this.situationService.situationToSituationWeb(indicatorExecution.getSituation());
        IndicatorExecutionWeb i = new IndicatorExecutionWeb(indicatorExecution.getId(), indicatorExecution.getAttachmentDescription(), project,
                performanceIndicator, outputWeb, indicatorExecution.getState(), indicatorExecution.getDisaggregationType(), indicatorExecution.getTarget(),
                indicatorExecution.getTotalExecution(), indicatorExecution.getExecutionPercentage(), indicatorExecution.getIndicatorType(),
                locations, situation, indicatorExecution.getMeasureType());
        return i;
    }
}
