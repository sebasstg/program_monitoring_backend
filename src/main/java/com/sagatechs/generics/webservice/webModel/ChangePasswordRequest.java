package com.sagatechs.generics.webservice.webModel;

public class  ChangePasswordRequest {
	private String code;

	private VerificationType type;

	private String verificator;

	private String newPassword;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public VerificationType getType() {
		return type;
	}

	public void setType(VerificationType type) {
		this.type = type;
	}

	public String getVerificator() {
		return verificator;
	}

	public void setVerificator(String verificator) {
		this.verificator = verificator;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


}
