package com.sagatechs.generics.template.security;


import com.sagatechs.generics.template.config.AdminConfig;
import com.sagatechs.generics.template.util.Constants;
import org.omnifaces.util.Faces;

import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("RedundantThrows")
@WebServlet(name = "adminLogoutServlet", urlPatterns = "/admin-logout")
public class LogoutServlet extends HttpServlet {
    @Inject
    AdminConfig adminConfig;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Faces.getSession().invalidate();
        ExternalContext ec = Faces.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + getLoginPage());
    }

    private String getLoginPage() {
        String loginPage = adminConfig.getLoginPage();
        if (loginPage == null || "".equals(loginPage)) {
            loginPage = Constants.DEFAULT_LOGIN_PAGE;
        }
        if (!loginPage.startsWith("/")) {
            loginPage = "/" + loginPage;
        }
        return loginPage;
    }
}