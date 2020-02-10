package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

//import com.sagatechs.care.model.Fingerprint;

@SuppressWarnings("ALL")
@Path("test")
public class TestEndpoint {

	private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);


	@Path("test")
	@GET
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)

	public String test1() throws GeneralAppException {
		return "ya !!";
	}

}

