package com.sagatechs.generics.webservice.webModel;

public class ChangePasswordResponse {
	private boolean usernameIsRegistered;

	private String verificationMedia ;

	public boolean isUsernameIsRegistered() {
		return usernameIsRegistered;
	}

	public void setUsernameIsRegistered(boolean usernameIsRegistered) {
		this.usernameIsRegistered = usernameIsRegistered;
	}

	public String getVerificationMedia() {
		return verificationMedia;
	}

	public void setVerificationMedia(String verificationMedia) {
		this.verificationMedia = verificationMedia;
	}
}
