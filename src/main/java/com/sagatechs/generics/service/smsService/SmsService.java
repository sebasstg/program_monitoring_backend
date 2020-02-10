package com.sagatechs.generics.service.smsService;

import com.sagatechs.generics.service.smsService.model.SmsMessageWeb;
import com.sagatechs.generics.service.smsService.model.TokenMasiva;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;


@SuppressWarnings("FieldCanBeLocal")
@Singleton
public class SmsService {

    private static final Logger LOGGER=Logger.getLogger(SmsService.class);


    @SuppressWarnings("FieldCanBeLocal")
    private final String MAIN_PATH = "https://api.login-sms.com";

    // TODO PASAR A PARAMETROS
    // sms sudamericana


    @SuppressWarnings("FieldCanBeLocal")
    private final String CLIENT_ID = //"smsd";
     "ddq2vV677/ZrGlP";
    @SuppressWarnings("FieldCanBeLocal")
    private final String CLIENT_SECRET = //"LLW52";
    "1RTWxOsiCirxUsc";
    @SuppressWarnings("FieldCanBeLocal")
    private final String GRANT_TYPE = "client_credentials";

    //    //https://www.login-sms.com/login


    private ResteasyClient client ;
    private ResteasyWebTarget target ;
    private SmsInterfaceClient proxy ;


    private static TokenMasiva token = null;

    @PostConstruct
    public void init(){
        client = new ResteasyClientBuilder().build();
        target = client.target(UriBuilder.fromPath(MAIN_PATH));
        proxy = target.proxy(SmsInterfaceClient.class);
    }

    @SuppressWarnings("SameParameterValue")
    private String getToken(boolean renew) {
        if (token == null || renew) {
            token = proxy.getToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE);
        }
        return "Bearer "+token.getAccess_token();
    }

    @SuppressWarnings("unused")
    public void getContactGroups(){


        Response response = proxy.getContactGroups(getToken(false));

        LOGGER.info("--R"+response.getStatus());

    }

    @SuppressWarnings("unused")
    public void sendSimpleMessage(String phoneNumber, String content){

        if(!StringUtils.startsWith(phoneNumber,"593") && !StringUtils.startsWith(phoneNumber,"+593")){
            phoneNumber="+593"+phoneNumber;
        }

        SmsMessageWeb message = new SmsMessageWeb(phoneNumber, content);
        Response response = proxy.sendMessage(getToken(true), message);

        LOGGER.info("snet sms message "+content);

        LOGGER.info("snet sms message "+response.getStatus());
        LOGGER.info("snet sms message "+response.getEntity());



    }




}
