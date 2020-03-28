package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.RightGroupDao;
import org.unhcr.programMonitoring.model.RightGroup;
import org.unhcr.programMonitoring.webServices.model.RightGroupWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RightGroupService {

    private static final Logger LOGGER = Logger.getLogger(RightGroupService.class);

    @Inject
    RightGroupDao rightGroupDao;

    public RightGroup find(Long id) {
        return this.rightGroupDao.find(id);
    }

    private List<RightGroup> getAllOrderedByCode() {

        return this.rightGroupDao.getAllOrderedByCode();

    }

    public List<RightGroupWeb> getAllRightGroupWebOrderedByCode() {
        List<RightGroup> rightGroups = this.getAllOrderedByCode();
        return this.rightGroupsToRightGroupWebs(rightGroups);
    }

    private List<RightGroupWeb> rightGroupsToRightGroupWebs(List<RightGroup> rightGroups) {
        List<RightGroupWeb> result = new ArrayList<>();

        for (RightGroup rightGroup : rightGroups) {
            result.add(this.rightGroupToRightGroupWeb(rightGroup));
        }
        return result;
    }

    public RightGroupWeb rightGroupToRightGroupWeb(RightGroup rightGroup) {
        RightGroupWeb rightGroupWeb = null;
        if (rightGroup != null)
            rightGroupWeb = new RightGroupWeb(rightGroup.getId(), rightGroup.getCode(), rightGroup.getDescription(), rightGroup.getState());
        return rightGroupWeb;
    }

    public Long save(RightGroupWeb rightGroupWeb) throws GeneralAppException {
        RightGroup rightGroup = new RightGroup();
        rightGroup.setCode(rightGroupWeb.getCode());
        rightGroup.setDescription(rightGroupWeb.getDescription());
        rightGroup.setState(rightGroupWeb.getState());
        this.validate(rightGroupWeb);
        this.rightGroupDao.save(rightGroup);
        return rightGroup.getId();

    }


   /* private RightGroup save(RightGroup rightGroup) throws GeneralAppException {
        this.validate(rightGroup);
        return this.rightGroupDao.save(rightGroup);

    }*/

    public Long update(RightGroupWeb rightGroupWeb) throws GeneralAppException {
        if (rightGroupWeb.getId() == null) {
            throw new GeneralAppException("El id es un campo obligatorio para actualizar el derecho", Response.Status.BAD_REQUEST.getStatusCode());
        }
        RightGroup rightGroup = this.find(rightGroupWeb.getId());

        if (rightGroup == null) {
            throw new GeneralAppException("No se pudo encontrar el derecho con id: " + rightGroupWeb.getId(), Response.Status.BAD_REQUEST.getStatusCode());
        }

        this.validate(rightGroupWeb);

        rightGroup.setCode(rightGroupWeb.getCode());
        rightGroup.setDescription(rightGroupWeb.getDescription());
        rightGroup.setState(rightGroup.getState());


        this.update(rightGroup);
        return rightGroup.getId();
    }

    private RightGroup update(RightGroup rightGroup) throws GeneralAppException {

        return this.rightGroupDao.update(rightGroup);
    }


    private void validate(RightGroupWeb rightGroupWeb) throws GeneralAppException {
        if (StringUtils.isBlank(rightGroupWeb.getCode())) {
            throw new GeneralAppException("El código es un valor requerido");
        }
        if (StringUtils.isBlank(rightGroupWeb.getDescription())) {
            throw new GeneralAppException("La descripción es un valor requerido");
        }
        if (rightGroupWeb.getState() == null) {
            throw new GeneralAppException("El estado es un valor requerido");
        }
        List<RightGroup> result = new ArrayList<>();
        result.addAll(this.rightGroupDao.getByCode(rightGroupWeb.getCode()));

        if (rightGroupWeb.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con este código", Response.Status.CONFLICT.getStatusCode());
        } else if (rightGroupWeb.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (RightGroup rightGroupT : result) {
                if (rightGroupT.getId() != rightGroupWeb.getId()) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con este código", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
        result = new ArrayList<>();
        result.addAll(this.rightGroupDao.getByDescription(rightGroupWeb.getDescription()));

        if (rightGroupWeb.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
        } else if (rightGroupWeb.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (RightGroup rightGroupT : result) {
                if (rightGroupT.getId() != rightGroupWeb.getId()) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
    }


    private void checkUniqueCode(Long id, String code) throws GeneralAppException {
        List<RightGroup> result = this.rightGroupDao.getByCode(code);
        if (id == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con este código", Response.Status.CONFLICT.getStatusCode());
        } else if (id != null && CollectionUtils.isNotEmpty(result)) {
            for (RightGroup rightGroup : result) {
                if (id != rightGroup.getId()) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con este código", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
    }

    public List<RightGroup> getByStateAndPeriodId(Long periodoId, State state) {
        return this.rightGroupDao.getByStateAndPeriodId(periodoId, state);
    }

    public List<RightGroupWeb> getWebByStateAndPeriodId(Long periodoId, State state) {
        return this.rightGroupsToRightGroupWebs(this.rightGroupDao.getByStateAndPeriodId(periodoId, state));
    }
}
