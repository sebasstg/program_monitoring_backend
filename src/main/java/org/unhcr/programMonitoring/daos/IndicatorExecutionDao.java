package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.IndicatorExecution;
import org.unhcr.programMonitoring.model.Output;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class IndicatorExecutionDao extends GenericDaoJpa<IndicatorExecution, Long> {

    public IndicatorExecutionDao() {
        super(IndicatorExecution.class, Long.class);
    }



    public List<IndicatorExecution> getByProjectId(Long projectId) {

        String sql = " select o from IndicatorExecution o " +
                " inner join fetch o.project pr " +
                " inner join fetch o.output ou " +
                " inner join fetch o.performanceIndicator i " +
                " left outer join fetch o.performanceIndicatorExecutionLocationAssigments l " +
                " inner join fetch o.situation s " +
                " left outer  join fetch o.indicatorValues v " +
                " where pr.id =:projectId";

        Query q = this.getEntityManager().createQuery(sql, Output.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }
}
