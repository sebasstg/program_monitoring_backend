package org.unhcr.programMonitoring.webServices.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
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
    PeriodPerformanceIndicatorAssigmentService periodPerformanceIndicatorAssigmentService;

    @Path("/rightGroup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RightGroupWeb> getAllRighGroups() {
        return this.rightGroupService.getAllRightGroupWebOrderedByCode();
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
    public void righGroupCheckUniqueCode(StringUniqueCheckWeb stringUniqueCheckWeb) throws GeneralAppException {

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
    public List<ObjetiveWeb> getObjetiveByRightId(@PathParam("id") Long id) throws GeneralAppException {
        return this.objetiveService.geWebtByRightId(id);
    }

    /**
     * outputs
     **/

    @Path("/output")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputWeb> getAllOutputs() {
        return this.outputService.getAllOutputWebOrderedByCode();
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
    public List<OutputWeb> getOutputByObjetiveId(@PathParam("id") Long id) throws GeneralAppException {
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
    public List<PerformanceIndicatorWeb> getPerformanceIndicatorsByOutputId(@PathParam("id") Long id) throws GeneralAppException {
        return this.performanceIndicatorService.getWebByOutputId(id);
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

    @Path("/period/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PeriodWeb getById(@PathParam("id") Long id) throws GeneralAppException {
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







}
