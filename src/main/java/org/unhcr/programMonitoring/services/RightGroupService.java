package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ProjectImplementerDao;
import org.unhcr.programMonitoring.daos.RightGroupDao;
import org.unhcr.programMonitoring.model.ProjectImplementer;
import org.unhcr.programMonitoring.model.RightGroup;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
public class RightGroupService {

    private static final Logger LOGGER = Logger.getLogger(RightGroupService.class);

    @Inject
    RightGroupDao rightGroupDao;

    public List<RightGroup> getAllOrderedByCode() {

        return this.rightGroupDao.getAllOrderedByCode();

    }

    public RightGroup save(RightGroup rightGroup) throws GeneralAppException {
        this.validate(rightGroup);
        return this.rightGroupDao.save(rightGroup);

    }


    public RightGroup update(RightGroup rightGroup) throws GeneralAppException {
        this.validate(rightGroup);
        return this.rightGroupDao.update(rightGroup);
    }


    public void validate(RightGroup rightGroup) throws GeneralAppException {
        if (StringUtils.isBlank(rightGroup.getCode())) {
            throw new GeneralAppException("El código es un valor requerido");
        }
        if (StringUtils.isBlank(rightGroup.getDescription())) {
            throw new GeneralAppException("La descripción es un valor requerido");
        }
        if (rightGroup.getState() == null) {
            throw new GeneralAppException("El estado es un valor requerido");
        }
        List<RightGroup> result = new ArrayList<>();
        result.addAll(this.rightGroupDao.getByCode(rightGroup.getCode()));

        if (rightGroup.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con este código", Response.Status.CONFLICT.getStatusCode());
        } else if (rightGroup.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (RightGroup rightGroupT : result) {
                if (rightGroupT.getId() != rightGroup.getId()) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con este código", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
        result = new ArrayList<>();
        result.addAll(this.rightGroupDao.getByDescription(rightGroup.getDescription()));

        if (rightGroup.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
        } else if (rightGroup.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (RightGroup rightGroupT : result) {
                if (rightGroupT.getId() != rightGroup.getId()) {
                    throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
    }


    public void checkUniqueCode(Long id, String code) throws GeneralAppException {
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
}
