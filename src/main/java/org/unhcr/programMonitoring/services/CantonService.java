package org.unhcr.programMonitoring.services;

import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.CantonDao;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.webServices.model.CantonWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CantonService {

    private static final Logger LOGGER = Logger.getLogger(CantonService.class);

    @Inject
    CantonDao cantonDao;

    @Inject
    ProvinciaService provinciaService;

    public List<Canton> getByProvinciaOrderedByName(Long provinciaId) {
        return this.cantonDao.getByProvinciaIdOrderedByName(provinciaId);
    }

    public List<CantonWeb> getWebByProvinciaOrderedByName(Long provinciaId) {
        return this.cantonsToCantonWebs(this.getByProvinciaOrderedByName(provinciaId));
    }

    public List<CantonWeb> cantonsToCantonWebs(List<Canton> cantons){
        List<CantonWeb> r= new ArrayList<>();
        for(Canton canton:cantons){
            r.add(this.cantonToCantonWeb(canton));
        }
        return r;
    }

    public CantonWeb cantonToCantonWeb(Canton canton){
        if(canton==null) return null;
        return new CantonWeb(canton.getId(),canton.getCode(),canton.getDescription(),canton.getState(), this.provinciaService.provinciaToProvinciaWeb(canton.getProvincia()));

    }

    public Canton find(Long id) {
        return this.cantonDao.find(id);
    }
}
