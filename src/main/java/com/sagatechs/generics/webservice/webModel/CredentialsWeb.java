package com.sagatechs.generics.webservice.webModel;

import java.io.Serializable;

public class CredentialsWeb implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String pushToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }


}