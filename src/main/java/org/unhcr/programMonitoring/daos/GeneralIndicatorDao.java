package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.GeneralIndicator;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class GeneralIndicatorDao extends GenericDaoJpa<GeneralIndicator, Long> {

    public GeneralIndicatorDao() {
        super(GeneralIndicator.class, Long.class);
    }

    public List<GeneralIndicator> getByPeriodId(Long periodId) {
        String sql = "select distinct o from GeneralIndicator o where o.period.id =:periodId  order by o.description ";
        Query q = this.getEntityManager().createQuery(sql, GeneralIndicator.class).setParameter("periodId", periodId);
        return q.getResultList();
    }

    public GeneralIndicator getMainByPeriodId(Long periodId) {
        String sql = "select distinct o from GeneralIndicator o where o.period.id =:periodId and o.parent=TRUE  order by o.description ";
        Query q = this.getEntityManager().createQuery(sql, GeneralIndicator.class).setParameter("periodId", periodId);
        try {
            return (GeneralIndicator) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }


    public List<GeneralIndicator> getByPeriodIdAndParent(Long periodId, Boolean parent) {
        String sql = "select distinct o from GeneralIndicator o where o.period.id =:periodId and o.parent=:parent order by o.description ";
        Query q = this.getEntityManager().createQuery(sql, GeneralIndicator.class)
                .setParameter("periodId", periodId).setParameter("parent", parent);
        return q.getResultList();
    }
}
