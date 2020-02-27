package org.unhcr.programMonitoring.webServices.services;

import com.sagatechs.generics.webservice.service.UserRestEndpoint;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.model.ProjectImplementer;
import org.unhcr.programMonitoring.services.ProjectImplementerService;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/implementer")
@RequestScoped
public class ProjectImplementerEndpoint {


    private static final Logger LOGGER = Logger.getLogger(ProjectImplementerEndpoint.class);

    @Inject
    ProjectImplementerService  projectImplementerService;

    @Path("/user/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectImplementerWeb getImplementerByUserId(@PathParam("userId") Long userId){
        return this.projectImplementerService.getProjectImplementerWebByUserId(userId);
    }

}
