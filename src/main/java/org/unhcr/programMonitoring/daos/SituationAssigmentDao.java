package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.SituationAssigment;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class SituationAssigmentDao extends GenericDaoJpa<SituationAssigment, Long> {

    public SituationAssigmentDao() {
        super(SituationAssigment.class, Long.class);
    }



    public List<SituationAssigment> getByState(State state) {
        String sql = "select distinct o from SituationAssigment o where o.state = :state ";
        Query q = this.getEntityManager().createQuery(sql, SituationAssigment.class).setParameter("state", state);

        return q.getResultList();

    }

}
