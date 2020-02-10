package com.sagatechs.generics.security.views;


import com.sagatechs.generics.security.credentials.UsernamePasswordRolesCredential;
import org.jboss.logging.Logger;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Named
@SessionScoped
public class LoginView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LoginView.class);

	@SuppressWarnings("CdiInjectionPointsInspection")
	@Inject
	private SecurityContext securityContext;


	@Inject
	private FacesContext facesContext;

	@SuppressWarnings("CdiInjectionPointsInspection")
	@Inject
	private ExternalContext externalContext;

	private String username;

	private String password;

	private boolean remember;

	private Set<String> roles = new HashSet<>();


	@PostConstruct
	public void init() {

	}

	public void login() {
		switch (continueAuthentication()) {
			case SEND_CONTINUE:
				facesContext.responseComplete();
				break;
			case SEND_FAILURE:
				Messages.addError(null, "Error en el nombre de usuario o contraseña");
				externalContext.getFlash().setKeepMessages(true);
				break;
			case SUCCESS:
				externalContext.getFlash().setKeepMessages(true);
						addMessage("Bienvenido " + username + " ",FacesMessage.SEVERITY_INFO);
				if (remember) {
					storeCookieCredentials(username, password);
				}
				Faces.redirect("index.xhtml");
				break;
			case NOT_DONE:
				Messages.addError(null, "Error en el nombre de usuario o contraseña");
		}
	}

	private AuthenticationStatus continueAuthentication() {
		return securityContext.authenticate((HttpServletRequest) externalContext.getRequest(),
				(HttpServletResponse) externalContext.getResponse(),
				AuthenticationParameters.withParams().rememberMe(remember)
						.credential(new UsernamePasswordRolesCredential(username, password)));
	}

	private void storeCookieCredentials(final String email, final String password) {
		Faces.addResponseCookie("saga-username", email, 1800);//store for 30min
		Faces.addResponseCookie("saga-pass", password, 1800);//store for 30min
	}



	@SuppressWarnings("unused")
	public boolean isLoggedIn() {
		return securityContext.getCallerPrincipal() != null;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	@SuppressWarnings("unused")
	public String getCurrentUser() {
		return securityContext.getCallerPrincipal() != null ? securityContext.getCallerPrincipal().getName() : "";
	}

	@SuppressWarnings("unused")
	public static void addDetailMessage(String message, FacesMessage.Severity severity) {

		FacesMessage facesMessage = Messages.create("").detail(message).get();
		if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
			facesMessage.setSeverity(severity);
		}
		Messages.add(null, facesMessage);
	}

	public static void addMessage(String message, FacesMessage.Severity severity) {

		FacesMessage facesMessage = Messages.create(message).detail("").get();
		if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
			facesMessage.setSeverity(severity);
		}
		Messages.add(null, facesMessage);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@SuppressWarnings("unused")
	public Set<String> getRoles() {
		return roles;
	}

	@SuppressWarnings("unused")
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}