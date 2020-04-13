package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.IndicatorExecutionLocationAssigment;
import org.unhcr.programMonitoring.model.PeriodPerformanceIndicatorAssigment;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
public class IndicatorExecutionLocationAssigmentDao extends GenericDaoJpa<IndicatorExecutionLocationAssigment, Long> {

    public IndicatorExecutionLocationAssigmentDao() {
        super(IndicatorExecutionLocationAssigment.class, Long.class);
    }

    public IndicatorExecutionLocationAssigment getByIndicatorExecutionIdAndLocationId(Long indicatorExecutionId, Long locationId) {
        String sql = "select distinct o from IndicatorExecutionLocationAssigment o  where o.indicatorExecution.id =:indicatorExecutionId and o.indicatorExecution.id =:locationId ";
        Query q = this.getEntityManager().createQuery(sql, IndicatorExecutionLocationAssigment.class)
                .setParameter("indicatorExecutionId", indicatorExecutionId)
                .setParameter("locationId", locationId);
        ;
        try {
            return (IndicatorExecutionLocationAssigment) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}
