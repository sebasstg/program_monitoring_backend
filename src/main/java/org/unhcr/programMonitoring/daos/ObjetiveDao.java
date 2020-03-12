package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.model.RightGroup;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ObjetiveDao extends GenericDaoJpa<Objetive, Long> {

    public ObjetiveDao() {
        super(Objetive.class, Long.class);
    }


    public List<Objetive> getAllOrderedByCode() {
        String sql = "select distinct o from Objetive o order by o.code";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class);

        return q.getResultList();

    }


    public List<Objetive> getByCode(String code) {
        String sql = "select distinct o from Objetive o where o.code =:code";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class).setParameter("code", code);

        return q.getResultList();

    }

    public List<Objetive> getByDescription(String description) {
        String sql = "select distinct o from Objetive o where o.description =:description";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class).setParameter("description", description);

        return q.getResultList();

    }


    public List<Objetive> getByRightId(Long id) {
        String sql = "select distinct o from Objetive o where o.rightGroup.id =:id";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class).setParameter("id", id);

       return q.getResultList();
    }

    public List<Objetive> getByStateAndPeriodIdandRighId(Long periodoId, State state, Long rightId) {

        String sql = "select distinct ob from PeriodPerformanceIndicatorAssigment ppia  inner join ppia.performanceIndicator pi " +
                " inner join pi.output o inner join o.objetive ob inner join ob.rightGroup r " +
                "where ppia.period.id = :periodoId and ppia.state = :state and r.id = :rightId";

        Query q = this.getEntityManager().createQuery(sql, Objetive.class);
        q.setParameter("state", state);
        q.setParameter("periodoId", periodoId);
        q.setParameter("rightId", rightId);

        return q.getResultList();
    }
}
