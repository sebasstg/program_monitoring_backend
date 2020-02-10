package com.sagatechs.generics.security.views;


import com.sagatechs.generics.template.util.Constants;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@RequestScoped
public class LogoutView implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public void doLogout() throws IOException {
        if (Faces.getRequestCookie("saga-username") != null) {
            Faces.removeResponseCookie("saga-username", null);
            Faces.removeResponseCookie("saga-username", null);
        }


        String loginPage = Constants.DEFAULT_LOGIN_PAGE;

        loginPage = "/" + loginPage;
        Faces.getSession().invalidate();
        ExternalContext ec = Faces.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + loginPage);
    }

}