package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.IndicatorExecution;
import org.unhcr.programMonitoring.model.IndicatorType;

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



    public List<IndicatorExecution> getByProjectId(Long projectId) {

        String sql = " select DISTINCT o from IndicatorExecution o " +

                " where o.project.id =:projectId ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }


    public List<IndicatorExecution> getPerformanceIndicatorByProjectIdAndState(Long projectId, State state) {

        String sql = " select DISTINCT o from IndicatorExecution o " +
                " inner join fetch o.project pr " +
                " inner join fetch o.output ou " +
                " inner join fetch ou.objetive ob " +
                " inner join fetch ob.rightGroup rg " +
                " inner join fetch o.performanceIndicator i " +
                " left outer join fetch o.performanceIndicatorExecutionLocationAssigments l " +
                " inner join fetch o.situation s " +
                " left outer  join fetch o.indicatorValues v " +
                " where pr.id =:projectId and o.performanceIndicator IS NOT null and o.state=:state" +
                " order by rg.code, ob.code, ou.code, o.attachmentDescription ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicators(Long projectId) {

        String sql = " select distinct o from IndicatorExecution o inner join fetch o.generalIndicator gi inner join fetch o.quarters q " +
                " where o.project.id =:projectId and o.indicatorType=:indicatorType order by gi.parent desc, o.id asc, q.quarterNumber asc ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("indicatorType", IndicatorType.GENERAL);
        List<IndicatorExecution> r = q.getResultList();
        return r;
    }

    public List<IndicatorExecution> getGeneralIndicatorsAndState(Long projectId,State state) {

        String sql = " select distinct o from IndicatorExecution o left outer join fetch o.generalIndicator gi left outer join fetch o.quarters q " +
                " where o.project.id =:projectId and o.indicatorType=:indicatorType " +
                " and o.state =: state " +
                "order by gi.parent desc, o.id asc, q.quarterNumber asc ";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("state", state);
        q.setParameter("indicatorType", IndicatorType.GENERAL);
        List<IndicatorExecution> r = q.getResultList();
        return r;
    }

    public IndicatorExecution getMainGeneralIndicators(Long projectId) {
        State state = State.ACTIVE;
        boolean parent=true;

        String sql = " select distinct o from IndicatorExecution o " +
                " where o.project.id =:projectId and  o.generalIndicator.parent=:parent and o.state=:state";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("state", state);
        q.setParameter("parent",parent);
        try {
            return (IndicatorExecution) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public IndicatorExecution getByGeneralIndicatorIdAndProjectId(Long generalIndicatorId, Long projectId) {
        String sql = " select distinct o from IndicatorExecution o inner join fetch o.indicatorValues " +
                " where o.generalIndicator.id =:generalIndicatorId and o.project.id =:projectId";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("generalIndicatorId", generalIndicatorId);
        q.setParameter("projectId", projectId);
        return (IndicatorExecution) q.getSingleResult();

    }


    public List<IndicatorExecution> getByPerformanceIndicatorIdAndPeriodId(Long performanceIndicatorId, Long periodId) {
        String sql = " select distinct o from IndicatorExecution o " +
                " where o.performanceIndicator.id =:performanceIndicatorId and o.project.period.id =:periodId";

        Query q = this.getEntityManager().createQuery(sql, IndicatorExecution.class);
        q.setParameter("performanceIndicatorId", performanceIndicatorId);
        q.setParameter("performanceIndicatorId", performanceIndicatorId);
        return q.getResultList();

    }
}
