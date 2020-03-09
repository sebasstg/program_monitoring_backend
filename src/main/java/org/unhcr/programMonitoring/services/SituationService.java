package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.PeriodDao;
import org.unhcr.programMonitoring.daos.SituationDao;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.Situation;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;
import org.unhcr.programMonitoring.webServices.model.PeriodWeb;
import org.unhcr.programMonitoring.webServices.model.SituationWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
public class SituationService {

    private static final Logger LOGGER = Logger.getLogger(SituationService.class);

    @Inject
    SituationDao situationDao;

    public List<SituationWeb> getAll() {
        return this.situationsToSituationsWeb(this.situationDao.findAll());
    }



    public Long save(SituationWeb situationWeb) throws GeneralAppException {
        if(situationWeb.getId()!=null){
            throw  new GeneralAppException("El id es un dato conflictivo para crear una situación", Response.Status.BAD_REQUEST.getStatusCode());
        }
        this.validate(situationWeb);
        Situation situation = this.situationWebToSituation(situationWeb);
        this.situationDao.save(situation);
        return situation.getId();
    }

    public Long update(SituationWeb situationWeb) throws GeneralAppException {
        if(situationWeb.getId()==null){
            throw  new GeneralAppException("El id es un dato necesario para actualizar una situación", Response.Status.BAD_REQUEST.getStatusCode());
        }
        this.validate(situationWeb);
        Situation situation = this.situationWebToSituation(situationWeb);
        this.situationDao.update(situation);
        return situation.getId();
    }

    private void validate(SituationWeb situationWeb) throws GeneralAppException {
        if(StringUtils.isBlank(situationWeb.getDescription())){
            throw  new GeneralAppException("La descripción es un dato requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if(StringUtils.isBlank(situationWeb.getCode())){
            throw  new GeneralAppException("El código es un dato requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if(situationWeb.getState()==null){
            throw  new GeneralAppException("Estado es un dato requerido", Response.Status.BAD_REQUEST.getStatusCode());
        }

        // reviso si hay repetidos
        //code
        List<Situation> byCodes = this.situationDao.getByCode(situationWeb.getCode());
        if(situationWeb.getId()==null && CollectionUtils.isNotEmpty(byCodes)){
            throw  new GeneralAppException("Ya existe una situación con el código: "+situationWeb.getCode(), Response.Status.CONFLICT.getStatusCode());
        }
        if(situationWeb.getId()!=null && CollectionUtils.isNotEmpty(byCodes)){
            for(Situation situation:byCodes){
                if(!situationWeb.getId().equals(situation.getId())){
                    new GeneralAppException("Ya existe una situación con el código: "+situationWeb.getCode(), Response.Status.CONFLICT.getStatusCode());
                }
            }

        }

        //code
        List<Situation> byDescription = this.situationDao.getByDescription(situationWeb.getDescription());
        if(situationWeb.getId()==null && CollectionUtils.isNotEmpty(byDescription)){
            throw  new GeneralAppException("Ya existe una situación con el el nombre: "+situationWeb.getDescription(), Response.Status.CONFLICT.getStatusCode());
        }
        if(situationWeb.getId()!=null && CollectionUtils.isNotEmpty(byDescription)){
            for(Situation situation:byCodes){
                if(!situationWeb.getId().equals(situation.getId())){
                    throw  new GeneralAppException("Ya existe una situación con el el nombre: "+situationWeb.getDescription(), Response.Status.CONFLICT.getStatusCode());
                }
            }
        }

    }

    public List<SituationWeb> getByState(State state) {
        return this.situationsToSituationsWeb(this.situationDao.getByState(state));
    }


    public SituationWeb situationToSituationWeb(Situation situation) {
        if (situation == null) return null;
        return new SituationWeb(situation.getId(), situation.getCode(), situation.getDescription(), situation.getState());
    }

    public List<SituationWeb> situationsToSituationsWeb(List<Situation> situations) {
        List<SituationWeb> result = new ArrayList<>();
        for (Situation situation : situations) {
            result.add(this.situationToSituationWeb(situation));
        }
        return result;
    }

    public Situation situationWebToSituation(SituationWeb situationWeb) {
        if (situationWeb == null) return null;

        Situation situation = new Situation();
        situation.setId(situationWeb.getId());
        situation.setCode(situationWeb.getCode());
        situation.setDescription(situationWeb.getDescription());
        situation.setState(situationWeb.getState());
        return situation;

    }

    public SituationWeb getSituationWebById(Long id) {
        return this.situationToSituationWeb(this.situationDao.find(id));
    }
}
