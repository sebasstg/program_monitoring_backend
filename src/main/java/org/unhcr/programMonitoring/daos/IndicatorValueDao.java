package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.IndicatorValue;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class IndicatorValueDao extends GenericDaoJpa<IndicatorValue, Long> {

    public IndicatorValueDao() {
        super(IndicatorValue.class, Long.class);
    }

    public List<IndicatorValue> getSubIndicatorValuesByGeneralIndicatorIdAndProjectId(Long generalIndicatorId, Long projectId){
        String sql = " select distinct o from IndicatorValue o inner join fetch o.indicatorExecution ie inner join fetch ie.generalIndicator ge  inner join fetch ge.mainIndicator mi" +
                " where ie.project.id =:projectId and mi.id =:generalIndicatorId";

        Query q = this.getEntityManager().createQuery(sql, IndicatorValue.class);
        q.setParameter("generalIndicatorId", generalIndicatorId);
        q.setParameter("projectId", projectId);
        return q.getResultList();

    }
}
