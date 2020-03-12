package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.model.PerformanceIndicator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PerformanceIndicatorDao extends GenericDaoJpa<PerformanceIndicator, Long> {

    public PerformanceIndicatorDao() {
        super(PerformanceIndicator.class, Long.class);
    }


    public List<PerformanceIndicator> getAllOrderedByCode() {
        String sql = "select distinct o from PerformanceIndicator o order by o.code";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class);

        return q.getResultList();

    }


    public List<PerformanceIndicator> getByCode(String code) {
        String sql = "select distinct o from PerformanceIndicator o where o.code =:code";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class).setParameter("code", code);

        return q.getResultList();

    }

    public List<PerformanceIndicator> getByDescription(String description) {
        String sql = "select distinct o from PerformanceIndicator o where o.description =:description";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class).setParameter("description", description);

        return q.getResultList();

    }


    public List<PerformanceIndicator> getByOutputId(Long id) {
        String sql = "select distinct o from PerformanceIndicator o where o.output.id =:id";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class).setParameter("id", id);
        return q.getResultList();
    }

    public List<PerformanceIndicator> getByStateAndPeriodIdandOutputId(Long periodoId, State state, Long outputId) {

        String sql = "select distinct pi from PeriodPerformanceIndicatorAssigment ppia  inner join ppia.performanceIndicator pi " +
                " inner join pi.output o inner join o.objetive ob inner join ob.rightGroup r " +
                "where ppia.period.id = :periodoId and ppia.state = :state and o.id = :outputId";

        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class);
        q.setParameter("state", state);
        q.setParameter("periodoId", periodoId);
        q.setParameter("outputId", outputId);

        return q.getResultList();
    }
}
