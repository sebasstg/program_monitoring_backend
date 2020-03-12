package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.PeriodDao;
import org.unhcr.programMonitoring.daos.ProvinciaDao;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.Provincia;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;
import org.unhcr.programMonitoring.webServices.model.PeriodWeb;
import org.unhcr.programMonitoring.webServices.model.ProvinciaWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProvinciaService {

    private static final Logger LOGGER = Logger.getLogger(ProvinciaService.class);

    @Inject
    ProvinciaDao provinciaDao;

    public List<Provincia> getAllOrderedByName() {
        return this.provinciaDao.getAllOrderedByName();
    }

    public List<ProvinciaWeb> getAllWebOrderedByName() {
        return this.provinciasToProvinciaWebs(this.getAllOrderedByName());
    }

    public List<ProvinciaWeb> provinciasToProvinciaWebs(List<Provincia> provincias){
        List<ProvinciaWeb> r= new ArrayList<>();
        for(Provincia provincia:provincias){
            r.add(this.provinciaToProvinciaWeb(provincia));
        }
        return r;
    }

    public ProvinciaWeb provinciaToProvinciaWeb(Provincia provincia){
        if(provincia==null) return null;
        return new ProvinciaWeb(provincia.getId(),provincia.getCode(),provincia.getDescription(),provincia.getState());

    }

}
