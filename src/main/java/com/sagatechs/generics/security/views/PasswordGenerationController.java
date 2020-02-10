package com.sagatechs.generics.security.views;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.servicio.UserService;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("unused")
@Named
@ViewScoped
public class PasswordGenerationController implements Serializable {
// TODO implementar
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(PasswordGenerationController.class);

	@Inject
	private UserService userService;

	private String nuevoUsuarioUsername;

	@Email(message = "El correo no es válido")
	private String nuevoUsuarioEmail;

	private boolean sentEmail = false;

	@NotBlank(message = "El código es obligatorio")
	private String code;

	private String password;
	
	@PostConstruct
	public void init() {
		
		
	}

	@SuppressWarnings("unused")
	public void generarContrasenia() {
		LOGGER.debug("username: " + this.nuevoUsuarioUsername);
		LOGGER.debug("email: " + this.nuevoUsuarioEmail);
		try {
			this.nuevoUsuarioEmail = this.userService.sendSecurityCodeForPasswordChange(this.nuevoUsuarioUsername);
			if (StringUtils.isNotBlank(nuevoUsuarioEmail)) {
				this.sentEmail = true;
				FacesContext context = FacesContext.getCurrentInstance();
				// context.getExternalContext().getFlash().setKeepMessages(true);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Código de seguridad enviado",
						"Por favor revise su correo"));
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"!!El usuario no se encuentra habilitado en el sistema!!",
								"Por favor comuníquese con el administrador del sistema."));
			}
		} catch (GeneralAppException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			// context.getExternalContext().getFlash().setKeepMessages(true);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "!!Error!!", e.getMessage()));
		}
	}

	@SuppressWarnings("unused")
	public void guardarContrasenia() throws IOException {
		LOGGER.debug("username: " + this.nuevoUsuarioUsername);
		LOGGER.debug("email: " + this.nuevoUsuarioEmail);
		LOGGER.debug("password: " + this.password);
		LOGGER.debug("code: " + this.code);

		try {
			this.userService.changePassworWithSecurityCode(this.nuevoUsuarioEmail, this.code, this.password);
			FacesContext context = FacesContext.getCurrentInstance();
			// context.getExternalContext().getFlash().setKeepMessages(true);
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Por favor ingrese al sistema con su nueva vontraseña",
							"Por favor ingrese al sistema con su nueva contraseña"));
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		} catch (GeneralAppException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"!!El usuario no se encuentra habilitado en el sistema!!",
							"Por favor comuníquese con el administrador del sistema."));
		}

	}

	public String getNuevoUsuarioUsername() {
		return nuevoUsuarioUsername;
	}

	public void setNuevoUsuarioUsername(String nuevoUsuarioUsername) {
		this.nuevoUsuarioUsername = nuevoUsuarioUsername;
	}

	public String getNuevoUsuarioEmail() {
		return nuevoUsuarioEmail;
	}

	public void setNuevoUsuarioEmail(String nuevoUsuarioEmail) {
		this.nuevoUsuarioEmail = nuevoUsuarioEmail;
	}

	public boolean isSentEmail() {
		return sentEmail;
	}

	public void setSentEmail(boolean sentEmail) {
		this.sentEmail = sentEmail;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}