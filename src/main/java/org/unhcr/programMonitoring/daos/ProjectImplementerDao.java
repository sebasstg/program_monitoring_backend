package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.ProjectImplementer;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

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


}
