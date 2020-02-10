package com.sagatechs.generics.webservice.webModel;

public class UserVerificationRequest {

	/**
	 * puede ser numero de telefono, correo, usuario dependiendo del tipo para enviar código de verificación
	 */
	private String username;

	private VerificationType type;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public VerificationType getType() {
		return type;
	}

	public void setType(VerificationType type) {
		this.type = type;
	}
}
