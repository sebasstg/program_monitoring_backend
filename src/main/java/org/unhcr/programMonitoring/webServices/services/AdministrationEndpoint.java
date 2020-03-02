package org.unhcr.programMonitoring.webServices.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.model.RightGroup;
import org.unhcr.programMonitoring.services.ProjectImplementerService;
import org.unhcr.programMonitoring.services.RightGroupService;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;
import org.unhcr.programMonitoring.webServices.model.StringUniqueCheckWeb;

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


    @Path("/rightGroup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RightGroup> getAllRighGroups(){
        return this.rightGroupService.getAllOrderedByCode();
    }

    @Path("/rightGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createRighGroup(RightGroup rightGroup) throws GeneralAppException {
         this.rightGroupService.save(rightGroup);
    }

    @Path("/rightGroup")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateRightGroup(RightGroup rightGroup) throws GeneralAppException {
        this.rightGroupService.update(rightGroup);
    }

    @Path("/rightGroup/checkCode")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void righGroupCheckUniqueCode(StringUniqueCheckWeb stringUniqueCheckWeb) throws GeneralAppException {

        this.rightGroupService.checkUniqueCode(stringUniqueCheckWeb.getId(), stringUniqueCheckWeb.getValue() );
    }
}
