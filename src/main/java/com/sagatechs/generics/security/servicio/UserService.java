package com.sagatechs.generics.security.servicio;

import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.exceptions.AuthorizationException;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.service.EmailService;
import com.sagatechs.generics.utils.SecurityUtils;
import com.sagatechs.generics.webservice.webModel.*;
import com.sagatechs.generics.service.smsService.SmsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.apache.commons.collections4.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import javax.crypto.SecretKey;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;


@Stateless
public class UserService implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);


    @Inject
    UserDao userDao;

    @Inject
    SecurityUtils securityUtils;

    @Inject
    EmailService emailService;

    @Inject
    SmsService smsService;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    SecurityContext securityContext;

    @Inject
    com.sagatechs.generics.utils.StringUtils stringUtils;

    private static final int EXPIRATION_TIME_SECONDS = 86400;// 6400;
    private static final int EXPIRATION_TIME_SECONDS_REFRESH = 86400 * 7;// 6400;

    private static final String SECRET_KEY = "xaE5cHuY4NCQm0v_BnsE93x3aa6tcRNUDJBKnHKUqhagrMIeTALKwkYHYPr77dBbPddJ5o207mWaF1ibL3zdDkDBv5MywlcPfu3_Awy2zDbCTDp6pZm-h245ZuC-ieVsDvBi3c1X15YEvmiqsE4BTKKQiHraIzT9kPwO2cqNJFfQPFMu_TWXeSpU14fLG5uFip2MltirPJLAeYS2kB4x--PLacTNo9Tb9zW3d0Il768xLOgPpdBqNkwUwLKrPtfXOl5mgXbv2l6G2k3z-JIysZJlRnDCTKp4R8Vvucp3i8p4e5UadenCT2Bl6qPMyYpXfS2j8jv08unn5xQiwkusiQ";

    private SecretKey key = null;

    public static final String salt = "NwhZ2MFDH0JDXmUSM8q5JydFiVg";

    private static Map<String, String> codeValidationCache = new HashMap<>();


    @SuppressWarnings("unused")
    private String codeSenderType = "email";

    /**
     * Persiste un nuevo usuario
     *
     * @param user usuario ya asignado roles y contraseñas
     */
    public void saveOrUpdate(User user) {
        if (user.getId() == null) {
            this.userDao.save(user);
        } else {
            this.userDao.update(user);
        }

    }

    /**
     * Verifica las credenciales del usuario, por nombre de usuario y contraseña
     *
     * @param username
     * @param password
     * @return
     */
    public boolean verifyUsernamePassword(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return false;
        }

        // obtengo el hash del pass enviado
        byte[] hashedPass = this.securityUtils.hashPasswordByte(password, salt);
        User user = this.userDao.findByUserNameAndPassword(username, hashedPass, State.ACTIVE);

        return user != null;
    }


    /**
     * Obtiene los tipos de roles asignados a un usuario, que tengan la asignación y el role con el estado dado
     *
     * @param username
     * @param state
     * @return
     */
    @SuppressWarnings("WeakerAccess")
    public Set<RoleType> getRolesByUsernameAndState(String username, State state) {
        Set<RoleType> rolesSet;
        List<RoleType> roleTypes = this.userDao.getRoleTypesByUsernameAndState(username, state);

        if (CollectionUtils.isNotEmpty(roleTypes)) {
            rolesSet = new HashSet<>(roleTypes);
        } else {
            rolesSet = new HashSet<>();
        }

        return rolesSet;
    }

    /**
     * Obtiene los nombres de roles asignados a un usuario, que tengan la asignación y el role con el estado dado
     *
     * @param username
     * @param state
     * @return
     */
    public Set<String> getRolesNamesByUsername(String username, State state) {
        Set<String> rolesName = new HashSet<>();
        Set<RoleType> rolesSet = this.getRolesByUsernameAndState(username, state);

        if (CollectionUtils.isNotEmpty(rolesSet)) {
            for (RoleType roleType : rolesSet) {
                rolesName.add(roleType.name());
            }
        }
        return rolesName;
    }

    /**
     * Envía codigo de seguridad para cambiar la contraseña
     *
     * @param username
     * @throws GeneralAppException
     */
    public String sendSecurityCodeForPasswordChange(String username) throws GeneralAppException {

        // recupero un usuario
        if (StringUtils.isBlank(username)) {
            throw new GeneralAppException("El nombre de usuario es incorrecto: " + username,
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        User user = this.userDao.findByUserName(username);
        if (user == null) {
            throw new GeneralAppException("No se encontró un usuario con el nombre de usuario: " + username,
                    Response.Status.NOT_FOUND.getStatusCode());

        }

        if (user.getState().equals(State.INACTIVE)) {
            throw new GeneralAppException("El usuario " + user + " ha sido desactivado por el administrador del sistema. Comuníquese con el administrador del sistema.",
                    Response.Status.NOT_FOUND.getStatusCode());

        }

        // genero el codigo pongo en cahce y envio al correo
        String securityCode = this.securityUtils.generateRandomCode();

        // guardo en el cache

        UserService.codeValidationCache.put(username, securityCode);

        // envio al correo
        //TODO
        this.sendEmailSecurityCodeForPasswordChange(username, securityCode);

        return username;
    }

    /**
     * Envía mensaje a correo electrónico para envio de código de seguridad
     *
     * @param email
     * @param securityCode
     */
    private void sendEmailSecurityCodeForPasswordChange(String email, String securityCode) {

        this.emailService.sendEmailMessage(email, "Código de seguridad temporal",
                "Se ha solicitado el cambio de contraseña para su usuario. El código de seguridad es: " + securityCode
                        + ". En caso de no haber solicitado el cambio de contraseña, informar al administrador del sistema.");

    }

    /**
     * Setea nuevo pass y verifica codigo de seguridad
     *
     * @param username
     * @param securityCode
     * @param newPassword
     * @throws GeneralAppException
     */
    public void changePassworWithSecurityCode(String username, String securityCode, String newPassword)
            throws GeneralAppException {
        // recupero un usuario
        if (StringUtils.isBlank(username) || StringUtils.isBlank(securityCode) || StringUtils.isBlank(newPassword)) {
            throw new GeneralAppException("Los datos no son correctos", Response.Status.BAD_REQUEST.getStatusCode());
        }

        User user = this.userDao.findByUserName(username);
        if (user == null) {
            throw new GeneralAppException("No se encontró un usuario con el nombre de usuario: " + username,
                    Response.Status.NOT_FOUND.getStatusCode());

        }

        // vetifico el codigo de seguridad
        String codigoSeguridadCache = UserService.codeValidationCache.get(username);
        if (!codigoSeguridadCache.equals(securityCode)) {
            throw new GeneralAppException("Los datos no son correctos", Response.Status.FORBIDDEN.getStatusCode());
        }
        // ya que esta verificado, reseteo el pass

        byte[] hashedPass = this.securityUtils.hashPasswordByte(newPassword, UserService.salt);
        user.setPassword(hashedPass);
        this.userDao.update(user);
        // quito del cache
        UserService.codeValidationCache.remove(username);
    }


    /**
     * Autentica Rests y genera tolens
     *
     * @param username
     * @param password
     * @param pushToken id de aplicativo para push
     * @return tokens
     * @throws AccessDeniedException
     */
    public TokensWeb authenticateRest(String username, String password, String pushToken) {

        if (this.verifyUsernamePassword(username, password)) {


            TokensWeb generatedTokens = generateTokens(username);

            renewTokens(username, generatedTokens, pushToken);

            return generatedTokens;
        } else {
            throw new AuthorizationException(
                    "Acceso denegado. Por favor ingrese correctamente el nombre de usuario y contraseña.");
        }

    }

    /**
     * Renueva tokens en el usuario
     *
     * @param username
     * @param generatedTokens
     * @param pushToken
     * @return
     */
    @SuppressWarnings("UnusedReturnValue")
    private User renewTokens(String username, TokensWeb generatedTokens, String pushToken) {

        User user = this.userDao.findByUserName(username);

        user.setAccessToken(generatedTokens.getAccess_token());
        user.setRefreshToken(generatedTokens.getRefresh_token());
        if (StringUtils.isNotBlank(pushToken)) {
            user.setAppUserId(pushToken);
        }

        this.userDao.update(user);
        return user;
    }

    /**
     * Generara estructura web para tokens
     *
     * @param username
     * @return estructura web para tokens
     */

    private TokensWeb generateTokens(String username) {
        Set<String> rolesActivos = this.getRolesNamesByUsername(username, State.ACTIVE);

        TokensWeb generatedTokens = new TokensWeb();
        generatedTokens.setAccess_token(issueToken(username, rolesActivos, false));
        generatedTokens.setRefresh_token(issueToken(username, rolesActivos, true));
        generatedTokens.setExpires_in(EXPIRATION_TIME_SECONDS);
        generatedTokens.setUsername(username);
        generatedTokens.setRoles(rolesActivos.toArray(new String[0]));

        return generatedTokens;
    }

    /**
     * Crea tokens para nomfre de usuario
     *
     * @param username
     * @param roles
     * @param refreshToken
     * @return
     */
    @SuppressWarnings("WeakerAccess")
    public String issueToken(String username, Set<String> roles, boolean refreshToken) {

        Date now = new Date();
        return Jwts.builder()
                // .serializeToJsonWith(serializer)// (1)
                .setSubject(username) // (2)
                .setIssuedAt(now).setExpiration(getExpirationDate(now, refreshToken)).claim("roles", roles)
                .claim("refreshToken", refreshToken).signWith(getSecretKey()).compact();
    }

    public String validateToken(String token) {
        if (StringUtils.isBlank(token)) {

            return null;
        }

        // obtengo el usuario
        Jws<Claims> jws = Jwts.parser() // (1)
                // .deserializeJsonWith(deserializer)
                .setSigningKey(getSecretKey()) // (2)
                .parseClaimsJws(token); // (3)

        // hasta ahi se valida el token
        String username = jws.getBody().getSubject();
        // veo si es de refresh
        Boolean isRefresh = (Boolean) jws.getBody().get("refreshToken");
        if (StringUtils.isBlank(username) || isRefresh == null) {

            return null;
        }

        // obtengo nuestro repartidor
        User user = this.findByUserName(username);
        String storedAccessToken = user != null ? user.getAccessToken() : null;
        String storedRefresAccessToken = user != null ? user.getRefreshToken() : null;

        if (isRefresh) {
            if (StringUtils.isEmpty(storedRefresAccessToken) || !token.equals(storedRefresAccessToken)) {
                return null;
            }

        } else {
            if (StringUtils.isEmpty(storedAccessToken) || !token.equals(storedAccessToken)) {
                return null;
            }
        }

        return username;

    }

    /**
     * Crea la clave secret de jwt
     *
     * @return
     */
    private SecretKey getSecretKey() {

        try {
            if (key == null) {
                key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            }
            return key;
        } catch (WeakKeyException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Genera fecha de exìracion de token
     *
     * @param date
     * @param refreshToken
     * @return
     */
    private Date getExpirationDate(Date date, boolean refreshToken) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        if (refreshToken) {
            // cal.add(Calendar.MINUTE, 3);
            cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS_REFRESH);
            // return null;
        } else {
            cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS); // adds one 24
        }

        return cal.getTime(); // returns new date object, one hour in the future
    }


    /**
     * Envia codigo de verificación a telefono
     *
     * @param phone
     * @param code
     */
    @SuppressWarnings("unused")
    private void sendVerificationCodeToSMS(String phone, String code) {
        String message = "BIENVENIDO A SUDAMERICANA: TU CODIGO DE VERIFICACION ES: "
                + code + ".";
        this.smsService.sendSimpleMessage(phone, message);
        this.sendVerificationCodeToEmail("saga@yopmail.com", code);
    }

    /**
     * Envía codigo de verificación a correo
     *
     * @param email
     * @param code
     */
    private void sendVerificationCodeToEmail(String email, String code) {
        String htmlMessage = "<p>Bienvenido a Saga:</p><p>&nbsp;</p><p>Tu c&oacute;digo de verificaci&oacute;n es: <b>"
                + code + "</b></p>";
        this.emailService.sendEmailMessage(email, "Código de verificación", htmlMessage);
    }


    /**
     * Verifica si el usuario ya se ha registrdo según el tipo de verificación
     *
     * @param userVerificator el texto usado para verificación, puede ser telefono, correo, etc
     * @return true si ya existe el usuario, y además envía codigo de verificación si no existe, false i no existe
     */
    public boolean verifyUserRegistration(String userVerificator, VerificationType type) throws GeneralAppException {

        if (StringUtils.isEmpty(userVerificator) || type == null) {
            throw new GeneralAppException("No se envió verificador de usuario o tipo", Response.Status.BAD_REQUEST.getStatusCode());
        }

        switch (type) {
            case EMAIL:
                return verifyEmailRegistration(userVerificator);
            case PHONE:
                return verifyPhoneRegistration(userVerificator);
            case USERNAME:
                return verifyUsernamelRegistration(userVerificator);
            default:
                throw new GeneralAppException("Tipo de verificador no implementado", Response.Status.BAD_REQUEST.getStatusCode());
        }

    }

    /**
     * verifica si ya existe un usuario con el correo
     *
     * @param email
     * @return
     */
    private boolean verifyEmailRegistration(String email) {
        User user = this.userDao.getByEmail(email);
        // si existe el usuario, devuelvo true
        if (user != null) {
            return true;
        } else {

            String code = this.securityUtils.generateRandomCode();
            UserService.codeValidationCache.put(email, code);
            this.sendVerificationCodeToEmail(email, code);
            return false;
        }
    }

    private boolean verifyUsernamelRegistration(String username) {

        User user = this.userDao.findByUserName(username);
        // si existe el usuario, devuelvo true
        if (user != null) {
            return true;
        } else {

            String code = this.securityUtils.generateRandomCode();
            UserService.codeValidationCache.put(username, code);
            //noinspection ConstantConditions
            this.sendVerificationCodeToEmail(user.getEmail(), code);
            return false;
        }
    }


    /**
     * Verifica si existe un usuario con el telefono dado, si no existe envia codigo de verificación
     *
     * @param phone
     * @return
     */
    private boolean verifyPhoneRegistration(String phone) {
        User user = this.userDao.getByPhone(phone);
        // si existe el usuario, devuelvo true
        if (user != null) {
            return true;
        } else {
            String code = this.securityUtils.generateRandomCode();
            UserService.codeValidationCache.put(phone, code);

            // this.sendVerificationCodeToSMS(phone, code);
            // TODO manda a correo provicionalmente
            this.sendVerificationCodeToEmail("saga@yopmail.com", code);
            LOGGER.debug(code);
            return false;
        }
    }

    /**
     * Renueva tokens
     *
     * @param refreshToken
     * @return
     */
    public TokensWeb refreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {

            throw new AccessDeniedException("Permiso denegado. Por favor vuelga a ingresar al sistema");
        }
        // obtengo el usuario

        Jws<Claims> jws;
        try {
            jws = Jwts.parser() // (1)
                    .setSigningKey(getSecretKey()) // (2)
                    .parseClaimsJws(refreshToken);

            // hasta ahi se valida el token
            String username = jws.getBody().getSubject();
            // veo si es de refresh
            Boolean isRefresh = (Boolean) jws.getBody().get("refreshToken");
            if (StringUtils.isBlank(username) || isRefresh == null || !isRefresh) {

                throw new AccessDeniedException("Permiso denegado. Por favor vuelga a ingresar al sistema");
            }

            TokensWeb generatedTokens = generateTokens(username);

            renewTokens(username, generatedTokens, null);
            return generatedTokens;
        } catch (Exception e) {
            LOGGER.error("Error en validación de token");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new AccessDeniedException("Permiso denegado. Por favor vuelga a ingresar al sistema");
        }
    }

    // TODO como logout si el token no verifico en bdd
    public void logout(String username) {
        TokensWeb tokens = new TokensWeb();
        renewTokens(username, tokens, null);
    }

    /**
     * Verifica el codigo de seguridad enviado al correo
     *
     * @param verificator
     * @param code
     * @return
     */
    public boolean codeVerification(String verificator, String code) {
        // busco en cache
        String savedCode = UserService.codeValidationCache.get(verificator);
        if (savedCode == null) {
            return false;
        } else {
            return savedCode.equals(code);
        }
    }

    public void codeVerificationRemove(String verificator) {

        UserService.codeValidationCache.remove(verificator);

    }


    public boolean changePasswordFromWsRequest(ChangePasswordRequest changePasswordRequest) throws GeneralAppException {
        // verifico el codigo

        // si fue correcto, actualizo el password
        if (this.codeVerification(changePasswordRequest.getVerificator(),
                changePasswordRequest.getCode())) {
            // recupero por email
            User user;
            switch (changePasswordRequest.getType()) {

                case EMAIL:
                    user = this.userDao.getByEmail(changePasswordRequest.getVerificator());
                    break;
                case USERNAME:
                    //noinspection UnusedAssignment
                    user = this.userDao.findByUserName(changePasswordRequest.getVerificator());
                case PHONE:
                    user = this.userDao.getByPhone(changePasswordRequest.getVerificator());
                    break;
                default:
                    throw new GeneralAppException("Tipo de verificación no implementada", Response.Status.BAD_REQUEST.getStatusCode());
            }


            if (user == null) {

                throw new GeneralAppException("Usuario no encontrado", Response.Status.BAD_REQUEST.getStatusCode());
            }
            // seteo nuevo password
            byte[] passwordEncp = this.securityUtils.hashPasswordByte(changePasswordRequest.getNewPassword(), UserService.salt);
            user.setPassword(passwordEncp);
            // persisto
            this.userDao.update(user);
            this.codeVerificationRemove(changePasswordRequest.getVerificator());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Envio un correo sy ya est registrado
     *
     * @param userVerificator puede ser username, correo o numero de fono
     * @param type
     * @return pista de medio de envio
     */
    public String changePasswordSendCode(String userVerificator, VerificationType type) {
        User user;
        switch (type) {
            case USERNAME:
                user = this.userDao.findByUserName(userVerificator);
                break;
            case EMAIL:
                user = this.userDao.getByEmail(userVerificator);
                break;
            case PHONE:
                user = this.userDao.getByPhone(userVerificator);
                break;
            default:
                user = null;
                break;
        }


        // si existe el usuario, devuelvo true

        String code = this.securityUtils.generateRandomCode();
        UserService.codeValidationCache.put(userVerificator, code);

        String media = null;
        if (user == null) {
            return null;
        } else {
            switch (type) {
                case USERNAME:
                    this.sendVerificationCodeToEmail(user.getEmail(), code);
                    media = stringUtils.maskEmail(user.getEmail());
                    break;
                case EMAIL:
                    this.sendVerificationCodeToEmail(userVerificator, code);
                    media = stringUtils.maskEmail(userVerificator);
                    break;
                case PHONE:
                    ////TODO  PROVICIONAL CON CORREO
                    this.sendVerificationCodeToEmail("saga@yopmail.com", code);
                    this.sendVerificationCodeToSMS(userVerificator,code);
                    media = stringUtils.maskPhone(user.getPhoneNumber());
                    break;

            }


            return media;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public User findByUserName(String username) {
        return this.userDao.findByUserName(username);
    }

    public void setPushTokent(String username, String pushToken) throws GeneralAppException {
        // recupero el usuario
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(pushToken)) {
            User user = this.findByUserName(username);
            if (user != null) {
                user.setAppUserId(pushToken);
                this.saveOrUpdate(user);
            } else {
                throw new GeneralAppException("Usuario no encontrado", Response.Status.NOT_FOUND.getStatusCode());
            }
        } else {
            throw new GeneralAppException("Solicitud inválida, usuario o pushToken inválidos", Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    public UserDataWeb getUserDataWeb() throws GeneralAppException {
        Principal principal = this.securityContext.getCallerPrincipal();
        if (principal == null) {
            throw new GeneralAppException("Este usuario no está autorizado para ver la data de usuario", Response.Status.UNAUTHORIZED.getStatusCode());
        }
        String username = principal.getName();
        User user = this.findByUserName(username);
        if (user == null) {
            // no debería pasar
            throw new GeneralAppException("Usuario no encontrado", Response.Status.NOT_FOUND.getStatusCode());
        } else {
            UserDataWeb userWeb = new UserDataWeb();
            userWeb.setNombre(user.getUsername());
            userWeb.setCorreo(user.getEmail());
            userWeb.setUsername(user.getUsername());
            userWeb.setTelefono(user.getPhoneNumber());
            return userWeb;
        }
    }
}
