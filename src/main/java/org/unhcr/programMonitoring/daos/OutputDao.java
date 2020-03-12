package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.model.Output;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class OutputDao extends GenericDaoJpa<Output, Long> {

    public OutputDao() {
        super(Output.class, Long.class);
    }


    public List<Output> getAllOrderedByCode() {
        String sql = "select distinct o from Output o order by o.code";
        Query q = this.getEntityManager().createQuery(sql, Output.class);

        return q.getResultList();

    }


    public List<Output> getByCode(String code) {
        String sql = "select distinct o from Output o where o.code =:code";
        Query q = this.getEntityManager().createQuery(sql, Output.class).setParameter("code", code);

        return q.getResultList();

    }

    public List<Output> getByDescription(String description) {
        String sql = "select distinct o from Output o where o.description =:description";
        Query q = this.getEntityManager().createQuery(sql, Output.class).setParameter("description", description);

        return q.getResultList();

    }


    public List<Output> getByObjetiveId(Long id) {
        String sql = "select distinct o from Output o where o.objetive.id =:id";
        Query q = this.getEntityManager().createQuery(sql, Output.class).setParameter("id", id);
        return q.getResultList() ;
    }

    public List<Output> getByStateAndPeriodIdandObjetiveId(Long periodoId, State state, Long objetiveId) {

        String sql = "select distinct o from PeriodPerformanceIndicatorAssigment ppia  inner join ppia.performanceIndicator pi " +
                " inner join pi.output o inner join o.objetive ob inner join ob.rightGroup r " +
                "where ppia.period.id = :periodoId and ppia.state = :state and ob.id = :objetiveId";

        Query q = this.getEntityManager().createQuery(sql, Output.class);
        q.setParameter("state", state);
        q.setParameter("periodoId", periodoId);
        q.setParameter("objetiveId", objetiveId);

        return q.getResultList();
    }
}
