package org.unhcr.programMonitoring.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.PeriodDao;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;
import org.unhcr.programMonitoring.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PeriodService {

    private static final Logger LOGGER = Logger.getLogger(PeriodService.class);

    @Inject
    PeriodDao periodDao;

    public List<PeriodResumeWeb> getAllPeriodResumeWebOrderedByYear() {
        return this.periodDao.getAllPeriodResumeWebOrderedByYear();
    }

    public List<PeriodResumeWeb> getPeriodResumeWebByPeriodId(Long periodId) {
        return this.periodDao.getPeriodResumeWebByPeriodId(periodId);
    }

    public PeriodWeb getPeriodWebById(Long id) {
        Period period = this.periodDao.find(id);

        return this.periodToPeriodWeb(period);

    }

    public Period getById(Long id){
        return this.periodDao.find(id);
    }

    public PeriodWeb periodToPeriodWeb(Period period) {
        if (period == null) {
            return null;
        } else {
            return new PeriodWeb(period.getId(), period.getYear(), period.getState());
        }
    }

    public Long save(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb.getId() != null) {
            throw new GeneralAppException("El id del periodo debe ser null si se va a crear un nuevo periodo.", Response.Status.BAD_REQUEST.getStatusCode());
        }
        Period periodT = this.getByYear(periodWeb.getYear());
        if (periodWeb.getId() == null && periodT != null) {
            throw new GeneralAppException("Ya existe un periodo para el año " + periodWeb.getYear(), Response.Status.CONFLICT.getStatusCode());
        }

        Period period = this.periodWebToPeriod(periodWeb);

        this.validate(period);
        this.periodDao.save(period);
        return period.getId();
    }

    public List<PeriodWeb> getPeriodWebByState(State state) {
        List<Period> result = this.getByState(state);
        return this.periodsToPeriodWebs(result);

    }

    private List<PeriodWeb> periodsToPeriodWebs(List<Period> periodWebs) {
        List<PeriodWeb> r= new ArrayList<>();
        for(Period period: periodWebs){
            r.add(this.periodToPeriodWeb(period));
        }
        return r;
    }

    private List<Period> getByState(State state) {
        return this.periodDao.getByState(state);
    }
    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb.getId() == null) {
            throw new GeneralAppException("El id del periodo no puede ser null si se va a actualizar el periodo.", Response.Status.BAD_REQUEST.getStatusCode());
        }
        Period periodT = this.getByYear(periodWeb.getYear());
        if (!periodWeb.getId().equals(periodT.getId())) {
            throw new GeneralAppException("Ya existe un periodo para el año " + periodWeb.getYear(), Response.Status.CONFLICT.getStatusCode());
        }

        Period period = this.periodDao.find(periodWeb.getId());//this.periodWebToPeriod(periodWeb);
        period.setState(periodWeb.getState());
        period.setYear(periodWeb.getYear());
        this.validate(period);
        this.periodDao.save(period);
        return period.getId();
    }

    private void validate(Period period) throws GeneralAppException {
        if (period == null) {
            throw new GeneralAppException("El periodo es null", Response.Status.BAD_REQUEST.getStatusCode());
        } else {
            if (period.getYear() == null) {
                throw new GeneralAppException("El año es un dato obligatorio en el periodo", Response.Status.BAD_REQUEST.getStatusCode());
            }
            if (period.getState() == null) {
                throw new GeneralAppException("El estado es un dato obligatorio en el periodo", Response.Status.BAD_REQUEST.getStatusCode());
            }
            // compruebo q no haya 2 para el mismo año


        }
    }

    private Period getByYear(Integer year) {
        return this.periodDao.getByYear(year);
    }

    private Period periodWebToPeriod(PeriodWeb periodWeb) {
        if (periodWeb == null) {
            return null;
        }
        Period period = new Period();
        period.setId(periodWeb.getId());
        period.setYear(periodWeb.getYear());
        period.setState(periodWeb.getState());
        return period;
    }

    public Period find(long periodId) {
        return this.periodDao.find(periodId);
    }
}
