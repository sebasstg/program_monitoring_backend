package org.unhcr.programMonitoring.webServices.services;

import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.model.RightGroup;
import org.unhcr.programMonitoring.services.ProjectImplementerService;
import org.unhcr.programMonitoring.services.RightGroupService;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
}
