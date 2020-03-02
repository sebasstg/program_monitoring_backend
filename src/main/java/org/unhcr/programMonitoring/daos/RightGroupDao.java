package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
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


}
