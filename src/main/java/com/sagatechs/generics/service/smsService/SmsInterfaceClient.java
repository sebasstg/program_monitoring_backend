package com.sagatechs.generics.service.smsService;

import com.sagatechs.generics.service.smsService.model.SmsMessageWeb;
import com.sagatechs.generics.service.smsService.model.TokenMasiva;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//@Path("")
@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface SmsInterfaceClient {


    @POST
    @Path("/token")
    public TokenMasiva getToken(@FormParam("client_id") String clientId, @FormParam("client_secret") String clientSecret , @FormParam("grant_type") String grantType);

    @GET
    @Path("/contacts-groups")
    public Response getContactGroups(@HeaderParam("Authorization") String tokenBearer);

    @POST
    @Path("/messages/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(@HeaderParam(HttpHeaders.AUTHORIZATION) String bearerToken, SmsMessageWeb message);



}
