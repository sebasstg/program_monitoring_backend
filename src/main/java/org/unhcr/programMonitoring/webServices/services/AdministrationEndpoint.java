package org.unhcr.programMonitoring.webServices.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.services.*;
import org.unhcr.programMonitoring.webServices.model.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/administration")
@RequestScoped
public class AdministrationEndpoint {


    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(AdministrationEndpoint.class);

    @Inject
    RightGroupService rightGroupService;

    @Inject
    ObjetiveService objetiveService;

    @Inject
    OutputService outputService;

    @Inject
    PerformanceIndicatorService performanceIndicatorService;

    @Inject
    PeriodService periodService;

    @Inject
    SituationService situationService;

    @Inject
    ProjectImplementerService projectImplementerService;

    @Inject
    ProjectService projectService;

    @Inject
    PeriodPerformanceIndicatorAssigmentService periodPerformanceIndicatorAssigmentService;

    @Inject
    ProvinciaService provinciaService;

    @Inject
    CantonService cantonService;

    @Inject
    GeneralIndicatorService generalIndicatorService;


    @Path("/rightGroup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RightGroupWeb> getAllRighGroups() {
        return this.rightGroupService.getAllRightGroupWebOrderedByCode();
    }

    @Path("/rightGroup/byPeriodActives/{periodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RightGroupWeb> getRighGroupsActivesByPeriodoId(@PathParam("periodId") Long periodId) {
        return this.rightGroupService.getWebByStateAndPeriodId(periodId, State.ACTIVE);
    }

    @Path("/rightGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long createRighGroup(RightGroupWeb rightGroupWeb) throws GeneralAppException {
        return this.rightGroupService.save(rightGroupWeb);
    }

    @Path("/rightGroup")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateRightGroup(RightGroupWeb rightGroupWeb) throws GeneralAppException {
        return this.rightGroupService.update(rightGroupWeb);
    }

    @Path("/rightGroup/checkCode")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void righGroupCheckUniqueCode(StringUniqueCheckWeb stringUniqueCheckWeb) {

        //this.rightGroupService.checkUniqueCode(stringUniqueCheckWeb.getId(), stringUniqueCheckWeb.getValue() );
    }

    /**
     * objetives
     **/

    @Path("/objetive")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ObjetiveWeb> getAllObjetives() {
        return this.objetiveService.getAllObjetiveWebsOrderedByCode();
    }

    @Path("/objetive/byPeriodAndRightIdActives/{periodId}/{rightId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ObjetiveWeb> getObjetivesActivesByPeriodoIdAndRightId(@PathParam("periodId") Long periodId, @PathParam("rightId") Long rightId) {
        return this.objetiveService.getWebtByStateAndPeriodIdandRighId(periodId, State.ACTIVE, rightId);
    }

    @Path("/objetive")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createRighGroup(Objetive objetive) throws GeneralAppException {
        this.objetiveService.save(objetive);
    }

    @Path("/objetive")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateObjetive(Objetive objetive) throws GeneralAppException {
        this.objetiveService.update(objetive);
    }

    @Path("/objetive/rightGroup/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ObjetiveWeb> getObjetiveByRightId(@PathParam("id") Long id) {
        return this.objetiveService.geWebtByRightId(id);
    }

    /**
     * outputs
     **/

    @Path("/output/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public OutputWeb getOutputById(@PathParam("id") Long id) {
        return this.outputService.findWeb(id);
    }

    @Path("/output")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputWeb> getAllOutputs() {
        return this.outputService.getAllOutputWebOrderedByCode();
    }

    @Path("/output/byPeriodAndObjetiveIdActives/{periodId}/{objetiveId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputWeb> getOutputsActivesByPeriodoIdAndObjetiveId(@PathParam("periodId") Long periodId, @PathParam("objetiveId") Long objetiveId) {
        return this.outputService.getWebsByStateAndPeriodIdandObjetiveId(periodId, State.ACTIVE, objetiveId);
    }


    @Path("/output/byPeriodActives/{periodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputWeb> getOutputsActivesByPeriodoId(@PathParam("periodId") Long periodId) {
        return this.outputService.getWebsByStateAndPeriodId(periodId, State.ACTIVE);
    }

    @Path("/output")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createRighGroup(OutputWeb output) throws GeneralAppException {
        this.outputService.save(output);
    }

    @Path("/output")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateOutput(OutputWeb output) throws GeneralAppException {
        this.outputService.update(output);
    }

    @Path("/output/objetive/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputWeb> getOutputByObjetiveId(@PathParam("id") Long id) {
        return this.outputService.getWebByObjetiveId(id);
    }

    /**
     * PerformanceIndicator
     **/

    @Path("/performanceIndicator")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PerformanceIndicatorWeb> getAllPerformanceIndicators() {
        return this.performanceIndicatorService.getAllPerformanceIndicatorWebOrderedByCode();
    }

    @Path("/performanceIndicator/byPeriodAndOutputIdActives/{periodId}/{outputId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PerformanceIndicatorWeb> getPerformanceActivesByPeriodoIdAndOutputId(@PathParam("periodId") Long periodId, @PathParam("outputId") Long objetiveId) {
        return this.performanceIndicatorService.getWebsByStateAndPeriodIdandOutputId(periodId, State.ACTIVE, objetiveId);
    }

    @Path("/performanceIndicator/byPeriodAndOutputIdAndIndicatorTypeActives/{periodId}/{outputId}/{indicatorType}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PerformanceIndicatorWeb> getPerformanceActivesByPeriodoIdAndOutputId(@PathParam("periodId") Long periodId,
                                                                                     @PathParam("outputId") Long objetiveId,
                                                                                     @PathParam("indicatorType") IndicatorType indicatorType) {
        return this.performanceIndicatorService.getWebByStateAndPeriodIdandOutputIdAndIndicatorType(periodId, State.ACTIVE, objetiveId, indicatorType);
    }

    @Path("/performanceIndicator/byType/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PerformanceIndicatorWeb> getPerformanceActivesByIndicatorType(@PathParam("type") IndicatorType indicatorType) {
        return this.performanceIndicatorService.getWebsByIndicatorType(indicatorType, State.ACTIVE);
    }

    @Path("/performanceIndicator")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createRighGroup(PerformanceIndicatorWeb performanceIndicator) throws GeneralAppException {
        this.performanceIndicatorService.save(performanceIndicator);
    }

    @Path("/performanceIndicator")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateOutput(PerformanceIndicatorWeb performanceIndicator) throws GeneralAppException {
        this.performanceIndicatorService.update(performanceIndicator);
    }

    @Path("performanceIndicator/output/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PerformanceIndicatorWeb> getPerformanceIndicatorsByOutputId(@PathParam("id") Long id)  {
        return this.performanceIndicatorService.getWebByOutputId(id);
    }

    @Path("performanceIndicator/output/{id}/type/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PerformanceIndicatorWeb> getPerformanceIndicatorsByOutputIdAndType(@PathParam("id") Long id, @PathParam("type") IndicatorType type)  {
        return this.performanceIndicatorService.getWebByOutputIdAndType(id, type);
    }

    /**
     * Period
     **/
    @Path("/period/resume")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodResumeWeb> getAllPeriodsResumeWeb() {
        return this.periodService.getAllPeriodResumeWebOrderedByYear();
    }

    @Path("/period/resume/byPeriod/{periodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodResumeWeb> getPeriodsResumeWebByPeriodId(@PathParam("periodId") Long periodId) {
        return this.periodService.getPeriodResumeWebByPeriodId(periodId);
    }


    @Path("/period/actives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodWeb> getActivePeriodsWeb() {
        return this.periodService.getPeriodWebByState(State.ACTIVE);
    }

    @Path("/period/inactives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodWeb> getInactivePeriodsWeb() {
        return this.periodService.getPeriodWebByState(State.INACTIVE);
    }

    @Path("/period/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PeriodWeb getById(@PathParam("id") Long id) {
        return this.periodService.getPeriodWebById(id);
    }


    @Path("/period")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long savePeriodWeb(PeriodWeb periodWeb) throws GeneralAppException {
        return this.periodService.save(periodWeb);
    }

    @Path("/period")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updatePeriod(PeriodWeb periodWeb) throws GeneralAppException {
        return this.periodService.update(periodWeb);
    }


    //PeriodPerformanceIndicatorAssigment
    @Path("/periodPerformanceIndicatorAssigment/byPeriodAndOutputIdAndIndicatorTypeActives/{periodId}/{outputId}/{indicatorType}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodPerformanceIndicatorAssigmentWeb> getPeriodPerformanceIndicatorAssigmentByPeriodId(@PathParam("periodId") Long periodId,
                                                                                                         @PathParam("outputId") Long outputId,
                                                                                                         @PathParam("indicatorType") IndicatorType indicatorType) {
        return this.periodPerformanceIndicatorAssigmentService.getWebByStateAndPeriodIdandOutputIdAndIndicatorType(periodId, outputId, indicatorType, State.ACTIVE);
    }


    @Path("/periodPerformanceIndicatorAssigment/period/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodPerformanceIndicatorAssigmentWeb> getPeriodPerformanceIndicatorAssigmentByPeriodId(@PathParam("id") Long periodId) {
        return this.periodPerformanceIndicatorAssigmentService.getWebByPeriodId(periodId);
    }

    @Path("/periodPerformanceIndicatorAssigment")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long savePeriodPerformanceIndicatorAssigment(PeriodPerformanceIndicatorAssigmentWeb periodPerformanceIndicatorAssigmentWeb) throws GeneralAppException {
        return this.periodPerformanceIndicatorAssigmentService.save(periodPerformanceIndicatorAssigmentWeb);
    }

    @Path("/periodPerformanceIndicatorAssigment")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updatePeriodPerformanceIndicatorAssigment(PeriodPerformanceIndicatorAssigmentWeb periodPerformanceIndicatorAssigmentWeb) throws GeneralAppException {
        return this.periodPerformanceIndicatorAssigmentService.update(periodPerformanceIndicatorAssigmentWeb);
    }

    /**
     * Situation
     **/
    @Path("/situation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SituationWeb> getAllSituationsResumeWeb() {
        return this.situationService.getAll();
    }

    @Path("/situation/actives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SituationWeb> getSituationsActivesWeb() {
        return this.situationService.getByState(State.ACTIVE);
    }

    @Path("/situation/inactives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SituationWeb> getSituationsInactivesWeb() {
        return this.situationService.getByState(State.INACTIVE);
    }

    @Path("/situation/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SituationWeb getSituationById(@PathParam("id") Long id) {
        return this.situationService.getSituationWebById(id);
    }


    @Path("/situation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long saveSituationWeb(SituationWeb situationWeb) throws GeneralAppException {
        return this.situationService.save(situationWeb);
    }

    @Path("/situation")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateSituation(SituationWeb situationWeb) throws GeneralAppException {
        return this.situationService.update(situationWeb);
    }


    /**
     * ProjectImplementer
     **/
    @Path("/projectImplementer")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectImplementerWeb> getAllProjectImplementersWeb() {
        return this.projectImplementerService.getAll();
    }

    @Path("/projectImplementer/actives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectImplementerWeb> getProjectImplementersActivesWeb() {
        return this.projectImplementerService.getByState(State.ACTIVE);
    }

    @Path("/projectImplementer/inactives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectImplementerWeb> getProjectImplementersInactivesWeb() {
        return this.projectImplementerService.getByState(State.INACTIVE);
    }

    @Path("/projectImplementer/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectImplementerWeb getProjectImplementerById(@PathParam("id") Long id) {
        return this.projectImplementerService.getProjectImplementerWebById(id);
    }


    @Path("/projectImplementer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long saveProjectImplementerWeb(ProjectImplementerWeb projectImplementerWeb) throws GeneralAppException {
        return this.projectImplementerService.save(projectImplementerWeb);
    }

    @Path("/projectImplementer")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateProjectImplementer(ProjectImplementerWeb projectImplementerWeb) throws GeneralAppException {
        return this.projectImplementerService.update(projectImplementerWeb);
    }


    /**
     * Project
     **/
    @Path("/project")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectWeb> getAllProjectsWeb() {
        return this.projectService.getAllWeb();
    }

    @Path("/project/actives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectWeb> getProjectsActivesWeb() {
        return this.projectService.getWebByState(State.ACTIVE);
    }

    @Path("/project/inactives")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectWeb> getProjectsInactivesWeb() {
        return this.projectService.getWebByState(State.INACTIVE);
    }

    @Path("/project/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectWeb getProjectById(@PathParam("id") Long id) {
        return this.projectService.getWebById(id);
    }

    @Path("/project/resume/byPeriod/{periodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectResumeWeb> getProjecResumetByPeriodId(@PathParam("periodId") Long periodId) {
        return this.projectService.getResumeWebByPeriodId(periodId);
    }


    @Path("/project")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long saveProjectWeb(ProjectWeb projectWeb) throws GeneralAppException {
        return this.projectService.save(projectWeb);
    }

    @Path("/project")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateProject(ProjectWeb projectWeb) throws GeneralAppException {
        return this.projectService.update(projectWeb);
    }

    @Path("/provincia")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProvinciaWeb> getAllProvinciaWebOrderedByName() {
        return this.provinciaService.getAllWebOrderedByName();
    }

    @Path("/canton/provincia/{provinciaId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonWeb> getCantonesWebByProvinciaIdOrderedByName(@PathParam("provinciaId") Long provinciaId) {
        return this.cantonService.getWebByProvinciaOrderedByName(provinciaId);
    }

    /**
     * PeriodPerformanceIndicatorAssigmentWeb
     **/
    @Path("/periodPerformanceIndicatorAssigmentWeb/byPeriodIdAndPerformanceIndicatorId/{periodId}/{performanceIndicatorId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PeriodPerformanceIndicatorAssigmentWeb getWebByPeriodIdAndPerformanceIndicatorId(@PathParam("periodId") Long periodId, @PathParam("performanceIndicatorId") Long performanceIndicatorId) {
        return this.periodPerformanceIndicatorAssigmentService.getWebByPeriodIdAndPerformanceIndicatorId(periodId, performanceIndicatorId);
    }


    /**
     * General Indicator
     */


    @Path("/generalIndicator/byPeriod/{periodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GeneralIndicatorWeb> getGeneralIndicatorByPeriod(@PathParam("periodId") Long periodId) {
        return this.generalIndicatorService.getWebByPeriodId(periodId);
    }

    @Path("/generalIndicator")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long createGeneralIndicator(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        return this.generalIndicatorService.save(generalIndicatorWeb);
    }

    @Path("/generalIndicator")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateGeneralIndicator(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        return this.generalIndicatorService.update(generalIndicatorWeb);
    }

}
