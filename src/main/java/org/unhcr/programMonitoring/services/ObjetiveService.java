package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ObjetiveDao;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.model.RightGroup;
import org.unhcr.programMonitoring.webServices.model.ObjetiveWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ObjetiveService {

    private static final Logger LOGGER = Logger.getLogger(ObjetiveService.class);

    @Inject
    ObjetiveDao objetiveDao;

    @Inject
    RightGroupService rightGroupService;

    public List<Objetive> getAllOrderedByCode() {

        return this.objetiveDao.getAllOrderedByCode();

    }

    public List<ObjetiveWeb> getAllObjetiveWebsOrderedByCode() {

        List<Objetive> objetives = this.getAllOrderedByCode();

        return this.objetivesToObjetiveWebs(objetives);

    }

    public Objetive find(Long id) {
        return this.objetiveDao.find(id);
    }

    public Long save(Objetive objetive) throws GeneralAppException {
        this.validate(objetive);
        return this.objetiveDao.save(objetive).getId();

    }

    public Objetive save(ObjetiveWeb objetiveWeb) throws GeneralAppException {

        Objetive objetive = this.objetiveWebToObjetive(objetiveWeb);
        this.validate(objetive);
        return this.objetiveDao.save(objetive);

    }


    public Objetive update(Objetive objetive) throws GeneralAppException {
        this.validate(objetive);
        return this.objetiveDao.update(objetive);
    }

    public Long update(ObjetiveWeb oWeb) throws GeneralAppException {
        Objetive oOrg = this.find(oWeb.getId());
        if (oOrg == null) {
            throw new GeneralAppException("No se puedo encontrar el ojetivo con id :" + oWeb.getId(), Response.Status.NOT_FOUND.getStatusCode());
        }

        Objetive oNew = new Objetive();

        oOrg.setRightGroup(oNew.getRightGroup());
        oOrg.setDescription(oNew.getDescription());
        oOrg.setCode(oNew.getCode());
        oOrg.setState(oNew.getState());


        this.validate(oOrg);
        return this.objetiveDao.update(oOrg).getId();
    }


    public void validate(Objetive objetive) throws GeneralAppException {
        if (StringUtils.isBlank(objetive.getCode())) {
            throw new GeneralAppException("El código es un valor requerido");
        }
        if (objetive.getRightGroup() == null) {
            throw new GeneralAppException("El grupo de derechos es un valor requerido");
        }
        if (StringUtils.isBlank(objetive.getDescription())) {
            throw new GeneralAppException("La descripción es un valor requerido");
        }
        if (objetive.getState() == null) {
            throw new GeneralAppException("El estado es un valor requerido");
        }
        List<Objetive> result = new ArrayList<>();
        result.addAll(this.objetiveDao.getByCode(objetive.getCode()));

        if (objetive.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un objetivo con este código", Response.Status.CONFLICT.getStatusCode());
        } else if (objetive.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (Objetive objetiveT : result) {
                if (objetiveT.getId() != objetive.getId()) {
                    throw new GeneralAppException("Ya existe un objetivo con este código", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
        result = new ArrayList<>();
        result.addAll(this.objetiveDao.getByDescription(objetive.getDescription()));

        if (objetive.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
        } else if (objetive.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (Objetive objetiveT : result) {
                if (objetiveT.getId() != objetive.getId()) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
    }

    public ObjetiveWeb objetiveToObjetiveWeb(Objetive objetive) {
        if (objetive == null) return null;

        return new ObjetiveWeb(objetive.getId(), objetive.getCode(), objetive.getDescription(), objetive.getState(), this.rightGroupService.rightGroupToRightGroupWeb(objetive.getRightGroup()));
    }

    private List<ObjetiveWeb> objetivesToObjetiveWebs(List<Objetive> objetives) {
        List<ObjetiveWeb> result = new ArrayList<>();
        for (Objetive objetive : objetives) {

            result.add(this.objetiveToObjetiveWeb(objetive));
        }
        return result;

    }

    private Objetive objetiveWebToObjetive(ObjetiveWeb objetiveWeb) {
        Objetive o = new Objetive();
        o.setId(objetiveWeb.getId());
        o.setCode(objetiveWeb.getCode());
        o.setDescription(o.getDescription());
        RightGroup rightGroup = null;
        if (objetiveWeb.getRightGroupWeb() != null && objetiveWeb.getRightGroupWeb().getId() != null) {
            rightGroup = this.rightGroupService.find(objetiveWeb.getRightGroupWeb().getId());
        }
        o.setRightGroup(rightGroup);
        return o;
    }


    public List<ObjetiveWeb> geWebtByRightId(Long id) {
        return this.objetivesToObjetiveWebs(this.getByRightId(id));
    }

    private List<Objetive> getByRightId(Long id) {
        return this.objetiveDao.getByRightId(id);
    }

    public List<ObjetiveWeb  > getWebtByStateAndPeriodIdandRighId(Long periodoId, State state, Long rightId) {
        return this.objetivesToObjetiveWebs(this.getByStateAndPeriodIdandRighId(periodoId,state,rightId));
    }
    public List<Objetive> getByStateAndPeriodIdandRighId(Long periodoId, State state, Long rightId) {
        return this.objetiveDao.getByStateAndPeriodIdandRighId(periodoId,state,rightId);
    }
}
