package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.ProjectImplementer;
import org.unhcr.programMonitoring.model.RightGroup;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class RightGroupDao extends GenericDaoJpa<RightGroup, Long> {

    public RightGroupDao() {
        super(RightGroup.class, Long.class);
    }


    public List<RightGroup> getAllOrderedByCode() {
        String sql = "select distinct o from RightGroup o order by o.code";
        Query q = this.getEntityManager().createQuery(sql, RightGroup.class);

        return q.getResultList();

    }


    public List<RightGroup> getByCode(String code) {
        String sql = "select distinct o from RightGroup o where o.code =:code";
        Query q = this.getEntityManager().createQuery(sql, RightGroup.class).setParameter("code", code);

        return q.getResultList();

    }

    public List<RightGroup> getByDescription(String description) {
        String sql = "select distinct o from RightGroup o where o.description =:description";
        Query q = this.getEntityManager().createQuery(sql, RightGroup.class).setParameter("description", description);

        return q.getResultList();

    }


    public List<RightGroup> getByStateAndPeriodId(Long periodoId, State state) {

        String sql = "select distinct r from PeriodPerformanceIndicatorAssigment ppia  inner join ppia.performanceIndicator pi " +
                " inner join pi.output o inner join o.objetive ob inner join ob.rightGroup r " +
                "where ppia.period.id = :periodoId and ppia.state = :state " +
                " order by r.code ";

        Query q = this.getEntityManager().createQuery(sql, RightGroup.class);
        q.setParameter("state", state);
        q.setParameter("periodoId", periodoId);

        return q.getResultList();
    }

}
