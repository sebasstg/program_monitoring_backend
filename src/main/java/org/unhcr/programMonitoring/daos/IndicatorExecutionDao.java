package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.IndicatorExecution;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class IndicatorExecutionDao extends GenericDaoJpa<IndicatorExecution, Long> {

    public IndicatorExecutionDao() {
        super(IndicatorExecution.class, Long.class);
    }


    public List<IndicatorExecution> getPerformanceIndicatorByProjectIdAndIndicatorId(Long projectId, Long indicatorId) {
        String sql = " select DISTINCT o from IndicatorExecution o " +
                " where o.project.id =:projectId and o.performanceIndicator.id =:indicatorId ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("indicatorId", indicatorId);
        return q.getResultList();
    }
    public List<IndicatorExecution> getPerformanceIndicatorByProjectId(Long projectId) {

        String sql = " select DISTINCT o from IndicatorExecution o " +
                " inner join fetch o.project pr " +
                " inner join fetch o.output ou " +
                " inner join fetch ou.objetive ob " +
                " inner join fetch ob.rightGroup rg " +
                " inner join fetch o.performanceIndicator i " +
                " left outer join fetch o.performanceIndicatorExecutionLocationAssigments l " +
                " inner join fetch o.situation s " +
                " left outer  join fetch o.indicatorValues v " +
                " where pr.id =:projectId and o.performanceIndicator IS NOT null " +
                " order by rg.code, ob.code, ou.code, o.attachmentDescription ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicators(Long projectId) {

        String sql = " select o from IndicatorExecution o " +
                " inner join fetch o.project pr " +
                " inner join fetch o.generalIndicator i " +
                " left outer join fetch o.performanceIndicatorExecutionLocationAssigments l " +
                " left outer join fetch i.subGeneralIndicators si " +
                " left outer  join fetch o.indicatorValues v " +
                " where pr.id =:projectId and o.generalIndicator IS NOT null ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }
}
