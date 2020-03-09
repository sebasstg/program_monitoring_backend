package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.ProjectImplementer;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ProjectImplementerDao extends GenericDaoJpa<ProjectImplementer, Long> {

    public ProjectImplementerDao() {
        super(ProjectImplementer.class, Long.class);
    }


    public ProjectImplementer getByUserId(Long userId){
        String sql ="select distinct o from ProjectImplementer o inner join o.users u  where  u.id = :userId ";
        Query q=this.getEntityManager().createQuery(sql, ProjectImplementer.class).setParameter("userId",userId);
        try {
            return (ProjectImplementer) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<ProjectImplementer> getByState(State state) {
        String sql = "select distinct o from ProjectImplementer o where o.state= :state";
        Query q = this.getEntityManager().createQuery(sql, ProjectImplementer.class).setParameter("state", state);
        return q.getResultList();

    }

    public List<ProjectImplementer> getByCode(String code) {
        String sql = "select distinct o from ProjectImplementer o where o.code= :code";
        Query q = this.getEntityManager().createQuery(sql, ProjectImplementer.class).setParameter("code", code);
        return q.getResultList();

    }

    public List<ProjectImplementer> getByDescription(String description) {
        String sql = "select distinct o from ProjectImplementer o where o.code= :description";
        Query q = this.getEntityManager().createQuery(sql, ProjectImplementer.class).setParameter("description", description);
        return q.getResultList();

    }


}
