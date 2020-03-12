package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class CantonDao extends GenericDaoJpa<Canton, Long> {

    public CantonDao() {
        super(Canton.class, Long.class);
    }

    public List<Canton> getByProvinciaIdOrderedByName(Long provinciaId) {
        String sql = "select distinct o from Canton o where o.provincia.id =:provinciaId  order by o.description";
        Query q = this.getEntityManager().createQuery(sql, Canton.class).setParameter("provinciaId",provinciaId);
        return q.getResultList();

    }
}
