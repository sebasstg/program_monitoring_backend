package com.github.adminfaces.template.bean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 15/04/18.
 */
@Named
@SessionScoped
public class ControlSidebarMB implements Serializable {

    private String template;

    private boolean darkSidebar;
    
    private boolean defaultTemplateSelected;

    @PostConstruct
    public void init() {
        setDefaultTemplate();
        darkSidebar = true;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplateTop() {
        template = "/WEB-INF/templates/template-top.xhtml";
    }

    public void setDefaultTemplate() {
        template = "/WEB-INF/templates/template.xhtml";
    }

    public void toggleTemplate() {
        if(isDefaultTemplate()) {
            setTemplateTop();
        } else {
            setDefaultTemplate();
        }
    }

    public boolean isDefaultTemplate() {
        return template != null && template.endsWith("template.xhtml");
    }

    public boolean isDefaultTemplateSelected() {
        return isDefaultTemplate();
    }

    public void setDefaultTemplateSelected(boolean defaultTemplateSelected) {
        this.defaultTemplateSelected = defaultTemplateSelected;
    }
    

    public boolean isDarkSidebar() {
        return darkSidebar;
    }

    public void setDarkSidebar(boolean darkSidebar) {
        this.darkSidebar = darkSidebar;
    }
}
