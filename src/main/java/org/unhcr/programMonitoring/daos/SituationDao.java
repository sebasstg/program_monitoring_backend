package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.Situation;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class SituationDao extends GenericDaoJpa<Situation, Long> {

    public SituationDao() {
        super(Situation.class, Long.class);
    }


    public List<Situation> getByState(State state) {
        String sql = "select distinct o from Situation o where o.state= :state";
        Query q = this.getEntityManager().createQuery(sql, Situation.class).setParameter("state", state);
        return q.getResultList();

    }

    public List<Situation> getByCode(String code) {
        String sql = "select distinct o from Situation o where o.code= :code";
        Query q = this.getEntityManager().createQuery(sql, Situation.class).setParameter("code", code);
        return q.getResultList();

    }

    public List<Situation> getByDescription(String description) {
        String sql = "select distinct o from Situation o where o.code= :description";
        Query q = this.getEntityManager().createQuery(sql, Situation.class).setParameter("description", description);
        return q.getResultList();

    }

}
