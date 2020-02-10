/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sagatechs.generics.template.showcase.bean;

import com.sagatechs.generics.template.bean.LayoutMB;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;

/**
 *
 * @author rmpestano
 */
@SuppressWarnings("unused")
@Specializes
@SessionScoped
public class ShowcaseLayoutMB extends LayoutMB {
    
    private boolean flat;
    private boolean borderless;
    private boolean materialButtons;
    
    @PostConstruct
    public void init() {
        super.init();
        flat = false;
        borderless = false;
    }

    public boolean isFlat() {
        return flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public boolean isBorderless() {
        return borderless;
    }

    public void setBorderless(boolean borderless) {
        this.borderless = borderless;
    }
    
    public boolean isMaterialButtons() {
        return materialButtons;
    }

    public void setMaterialButtons(boolean materialButtons) {
        this.materialButtons = materialButtons;
    }
    
}
