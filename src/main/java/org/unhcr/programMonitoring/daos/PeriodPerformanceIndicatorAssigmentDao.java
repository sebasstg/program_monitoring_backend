package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.PeriodPerformanceIndicatorAssigment;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PeriodPerformanceIndicatorAssigmentDao extends GenericDaoJpa<PeriodPerformanceIndicatorAssigment, Long> {

    public PeriodPerformanceIndicatorAssigmentDao() {
        super(PeriodPerformanceIndicatorAssigment.class, Long.class);
    }


    public List<PeriodPerformanceIndicatorAssigment> getByPeriodId(Long periodId) {
        String sql = "select distinct o from PeriodPerformanceIndicatorAssigment o left outer join fetch o.performanceIndicator p left outer join fetch o.period" +
                //" left outer  join fetch o. " +
                " where o.period.id= :id order by o.performanceIndicator.code";
        Query q = this.getEntityManager().createQuery(sql, Period.class).setParameter("id", periodId);
        return q.getResultList();
    }

    public PeriodPerformanceIndicatorAssigment getByPeriodIdAndPerformanceIndicatorId(Long periodId, Long performanceIndicatorId) {
        String sql = "select distinct o from PeriodPerformanceIndicatorAssigment o left outer join fetch o.performanceIndicator p left outer join fetch o.period" +
                //" left outer  join fetch o. " +
                " where o.period.id= :id  and o.performanceIndicator.id= :performanceIndicatorId" +
                " order by o.performanceIndicator.code";
        Query q = this.getEntityManager().createQuery(sql, PeriodPerformanceIndicatorAssigment.class).setParameter("id", periodId).setParameter("performanceIndicatorId",performanceIndicatorId);
        try {
            return (PeriodPerformanceIndicatorAssigment) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }


}
