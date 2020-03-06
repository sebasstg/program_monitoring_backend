package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.Objetive;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ObjetiveDao extends GenericDaoJpa<Objetive, Long> {

    public ObjetiveDao() {
        super(Objetive.class, Long.class);
    }


    public List<Objetive> getAllOrderedByCode() {
        String sql = "select distinct o from Objetive o order by o.code";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class);

        return q.getResultList();

    }


    public List<Objetive> getByCode(String code) {
        String sql = "select distinct o from Objetive o where o.code =:code";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class).setParameter("code", code);

        return q.getResultList();

    }

    public List<Objetive> getByDescription(String description) {
        String sql = "select distinct o from Objetive o where o.description =:description";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class).setParameter("description", description);

        return q.getResultList();

    }


    public List<Objetive> getByRightId(Long id) {
        String sql = "select distinct o from Objetive o where o.rightGroup.id =:id";
        Query q = this.getEntityManager().createQuery(sql, Objetive.class).setParameter("id", id);

       return q.getResultList();
    }
}
