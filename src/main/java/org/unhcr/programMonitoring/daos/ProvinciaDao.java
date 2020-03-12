package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.Provincia;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ProvinciaDao extends GenericDaoJpa<Provincia, Long> {

    public ProvinciaDao() {
        super(Provincia.class, Long.class);
    }


    public List<Provincia> getAllOrderedByName() {
        String sql = "select distinct o from Provincia o order by o.description";
        Query q = this.getEntityManager().createQuery(sql, Provincia.class);

            return q.getResultList();

    }

}
