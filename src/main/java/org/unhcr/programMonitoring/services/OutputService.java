package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.OutputDao;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.model.Output;
import org.unhcr.programMonitoring.webServices.model.OutputWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OutputService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(OutputService.class);

    @Inject
    OutputDao outputDao;

    @Inject
    ObjetiveService objetiveService;

    public List<OutputWeb> getAllOutputWebOrderedByCode() {

        List<Output> outputs = this.getAllOrderedByCode();
        return this.outputsToOutputWebs(outputs);
    }

    private List<Output> getAllOrderedByCode() {
        return this.outputDao.getAllOrderedByCode();
    }

    public OutputWeb findWeb(Long id) {
        return this.outputToOutputWeb(this.find(id));
    }

    public Output find(Long id) {
        return this.outputDao.find(id);
    }

    public Long save(Output output) {

        return this.outputDao.save(output).getId();

    }

    public Long save(OutputWeb outputWeb) throws GeneralAppException {
        this.validate(outputWeb);
        Output output = this.outputWebToOutput(outputWeb);
        this.save(output);
        return output.getId();

    }


    public Output update(Output output) {

        return this.outputDao.update(output);
    }

    public Long update(OutputWeb oWeb) throws GeneralAppException {
        Output oOrg = this.find(oWeb.getId());
        if (oOrg == null) {
            throw new GeneralAppException("No se puedo encontrar el output con id :" + oWeb.getId(), Response.Status.NOT_FOUND.getStatusCode());
        }
        this.validate(oWeb);
        Output oNew = this.outputWebToOutput(oWeb);
        oOrg.setObjetive(oNew.getObjetive());
        oOrg.setDescription(oNew.getDescription());
        oOrg.setCode(oNew.getCode());
        oOrg.setState(oNew.getState());

        this.outputDao.update(oOrg);
        return oOrg.getId();

    }


    public void validate(OutputWeb outputWeb) throws GeneralAppException {
        if (StringUtils.isBlank(outputWeb.getCode())) {
            throw new GeneralAppException("El código es un valor requerido");
        }
        if (outputWeb.getObjetiveWeb() == null) {
            throw new GeneralAppException("El grupo de derechos es un valor requerido");
        }
        if (StringUtils.isBlank(outputWeb.getDescription())) {
            throw new GeneralAppException("La descripción es un valor requerido");
        }
        if (outputWeb.getState() == null) {
            throw new GeneralAppException("El estado es un valor requerido");
        }
        List<Output> result = new ArrayList<>(this.outputDao.getByCode(outputWeb.getCode()));

        if (outputWeb.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un output con este código", Response.Status.CONFLICT.getStatusCode());
        } else if (outputWeb.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (Output outputT : result) {
                if (!outputT.getId().equals(outputWeb.getId())) {
                    throw new GeneralAppException("Ya existe un output con este código", Response.Status.CONFLICT.getStatusCode());
                }
            }
        }
        result = new ArrayList<>(this.outputDao.getByDescription(outputWeb.getDescription()));

        if (outputWeb.getId() == null && CollectionUtils.isNotEmpty(result)) {
            throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
        } else if (outputWeb.getId() != null && CollectionUtils.isNotEmpty(result)) {
            for (Output outputT : result) {
                if (!outputT.getId().equals(outputWeb.getId()))
                    throw new GeneralAppException("Ya existe un Grupo de derechos con esta descripción", Response.Status.CONFLICT.getStatusCode());
            }
        }
    }

    public OutputWeb outputToOutputWeb(Output output) {
        if (output == null) {
            return null;
        } else {
            return new OutputWeb(output.getId(), output.getCode(), output.getDescription(), output.getState(), this.objetiveService.objetiveToObjetiveWeb(output.getObjetive()));
        }
    }

    private List<OutputWeb> outputsToOutputWebs(List<Output> outputs) {
        List<OutputWeb> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(outputs)) {
            for (Output output : outputs) {
                result.add(this.outputToOutputWeb(output));
            }
        }
        return result;
    }

    private Output outputWebToOutput(OutputWeb outputWeb) {
        Output o = new Output();
        o.setId(outputWeb.getId());
        o.setCode(outputWeb.getCode());
        o.setState(outputWeb.getState());
        o.setDescription(outputWeb.getDescription());
        Objetive objetive = null;
        if (outputWeb.getObjetiveWeb() != null) {
            objetive = this.objetiveService.find(outputWeb.getObjetiveWeb().getId());
        }
        o.setObjetive(objetive);
        return o;
    }


    public List<OutputWeb> getWebByObjetiveId(Long id) {
        return this.outputsToOutputWebs(this.getByObjetiveId(id));
    }

    private List<Output> getByObjetiveId(Long id) {
        return this.outputDao.getByObjetiveId(id);
    }

    public List<OutputWeb> getWebsByStateAndPeriodIdandObjetiveId(Long periodoId, State state, Long objetiveId) {
        return this.outputsToOutputWebs(this.getByStateAndPeriodIdandObjetiveId(periodoId, state, objetiveId));
    }

    private List<Output> getByStateAndPeriodIdandObjetiveId(Long periodoId, State state, Long objetiveId) {
        return this.outputDao.getByStateAndPeriodIdandObjetiveId(periodoId, state, objetiveId);
    }

    private List<Output> getByStateAndPeriodId(Long periodoId, State state) {
        return this.outputDao.getByStateAndPeriodId(periodoId, state);
    }

    public List<OutputWeb> getWebsByStateAndPeriodId(Long periodId, State state) {
        return this.outputsToOutputWebs(this.getByStateAndPeriodId(periodId, state));
    }


}
