package com.sagatechs.generics.webservice.service;

import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.exceptions.AuthorizationException;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;

@Path("/authentication")
@RequestScoped
public class UserRestEndpoint {

    private static final Logger LOGGER = Logger.getLogger(UserRestEndpoint.class);

    @Inject
    UserService userService;

    /**
     * Autentica usuarios
     *
     * @param credentials
     * @return
     * @throws AccessDeniedException
     */
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TokensWeb authenticateUser(CredentialsWeb credentials)  {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        String push = credentials.getPushToken();

        try {
            return userService.authenticateRest(username, password, push);
        } catch (Exception e) {
           /* if (ExceptionUtils.getRootCause(e) instanceof AuthorizationException) {
                throw new AuthorizationException(ExceptionUtils.getRootCauseMessage(e));
            } else {*/
                throw e;
           // }

        }
    }


    /**
     * Refresca nuevo token
     *
     * @param refreshToken
     * @return
     * @throws AccessDeniedException
     * @throws
     */
    @Path("/refresh")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TokensWeb refreshToken(RefreshTokenRequest refreshToken) {

        return userService.refreshToken(refreshToken.getRefresh_token());
    }


    /**
     * Quita tokens para que se deba autenticar nuevamente
     *
     * @param securityContext
     */
    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public void logout(@Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String username = principal.getName();
        userService.logout(username);
    }


    /**
     * Setea push token
     *
     * @param securityContext context de seguridad
     * @param pushToken       token de fcm
     */
    @Path("/setPushToken")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public void setPushToken(@Context SecurityContext securityContext, PushTokenWeb pushToken) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();

        String username = principal.getName();

        try {
            userService.setPushTokent(username, pushToken.getPushToken());
        } catch (Exception e) {
            if (ExceptionUtils.getRootCause(e) instanceof AuthorizationException) {
                throw new AccessDeniedException(ExceptionUtils.getRootCauseMessage(e));
            } else {
                LOGGER.error(ExceptionUtils.getRootCause(e));
                LOGGER.error(ExceptionUtils.getStackTrace(e));
                throw new GeneralAppException("Error desconocido", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            }

        }
    }

    @Path("/getUserData")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public UserDataWeb getUserData() throws GeneralAppException {
        return this.userService.getUserDataWeb();
    }

    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDataWeb> getAllUSers()  {
        return this.userService.getAllUsersDataWeb();
    }

    @Path("/user/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserDataWeb getUserByid(@PathParam("id")Long id)  {
        return this.userService.getWebById(id);
    }

    @Path("/updatePass/{id}/{pass}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Long updatePass(@PathParam("id")Long id,@PathParam("pass") String pass)  {
        return this.userService.updatePasswordUser(id,pass);
    }

    @Path("/resetPass/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Long resetPass(@PathParam("id")Long id)  {
        return this.userService.resetPasswordUser(id);
    }
    @Path("/user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long createUserByid(UserDataWeb userDataWeb)  {
        return this.userService.createUser(userDataWeb);
    }

    @Path("/user")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateUserByid(UserDataWeb userDataWeb)  {
        return this.userService.updateUser(userDataWeb);
    }


    @Path("/test}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void test()  {

        this.userService.setAllPasswords();

    }


    /**
     * Verifica si el usuario ya se ha registrdo según el tipo de verificación
     *
     * @param userVerificationRequest el texto usado para verificación, puede ser telefono, correo, etc
     * @return true si ya existe el usuario, y además envía codigo de verificación si no existe, false i no existe
     */

    @Path("/checkUser")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserVerificarionResponse verifyUserRegistration(UserVerificationRequest userVerificationRequest) throws GeneralAppException {
        // verificado
        UserVerificarionResponse reponse = new UserVerificarionResponse();
        boolean result = userService.verifyUserRegistration(userVerificationRequest.getUsername(), userVerificationRequest.getType());
        reponse.setUsernameIsRegistered(result);
        return reponse;
    }




    /**
     * Verifica el codigo para el registro
     *
     * @param codeVerificationCodeVerificarionRequest
     * @return
     */
    @Path("/codeVerification")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CodeVerificarionResponse verifyCodeUser(CodeVerificationRequest codeVerificationCodeVerificarionRequest) {
        CodeVerificarionResponse response = new CodeVerificarionResponse();
        boolean result = this.userService.codeVerification(codeVerificationCodeVerificarionRequest.getVerificator(),
                codeVerificationCodeVerificarionRequest.getCode());
        response.setCodeIsCorrect(result);
        return response;
    }


    /**
     * Cambia la contraseña
     *
     * @param userVerificationRequest
     * @return
     */
    @Path("/changePasswordSendCode")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ChangePasswordResponse changePasswordSendCode(UserVerificationRequest userVerificationRequest) {
        ChangePasswordResponse response = new ChangePasswordResponse();
        String media = this.userService.changePasswordSendCode(userVerificationRequest.getUsername(), userVerificationRequest.getType());
        if(StringUtils.isEmpty(media)){
            response.setUsernameIsRegistered(false);
        }else{
            response.setUsernameIsRegistered(true);
            response.setVerificationMedia(media);
        }

        return response;
    }

    /**
     * Cambia la contraseña
     *
     * @param changePasswordRequest
     * @return
     */
    @Path("/changePassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CodeVerificarionResponse changePassword(ChangePasswordRequest changePasswordRequest) throws GeneralAppException {
        //TODO GENERALIZAR
        CodeVerificarionResponse response = new CodeVerificarionResponse();
        boolean result = this.userService.changePasswordFromWsRequest(changePasswordRequest);
        response.setCodeIsCorrect(result);
        return response;
    }

    @Path("/testConexion")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void testConexion()  {
        return ;
    }



}