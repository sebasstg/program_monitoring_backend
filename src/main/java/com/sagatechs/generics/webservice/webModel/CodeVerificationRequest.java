package com.sagatechs.generics.webservice.webModel;

public class CodeVerificationRequest {
	private String code;
	
	private String verificator;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVerificator() {
		return verificator;
	}

	public void setVerificator(String verificator) {
		this.verificator = verificator;
	}
}
