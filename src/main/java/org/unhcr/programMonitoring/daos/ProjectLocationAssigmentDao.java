package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.ProjectLocationAssigment;
import org.unhcr.programMonitoring.model.Provincia;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ProjectLocationAssigmentDao extends GenericDaoJpa<ProjectLocationAssigment, Long> {

    public ProjectLocationAssigmentDao() {
        super(ProjectLocationAssigment.class, Long.class);
    }


    public List<ProjectLocationAssigment> getByProjectId(Long projectId) {
        String sql = "select distinct o from ProjectLocationAssigment o " +
                " inner join fetch o.location c " +
                " inner join fetch c.provincia p " +
                " where o.project.id =:projectId" +
                " order by p.description, c.description ";
        Query q = this.getEntityManager().createQuery(sql, ProjectLocationAssigment.class);
        q.setParameter("projectId",projectId);
        return q.getResultList();
    }

    public List<ProjectLocationAssigment> getByProjectIdAndCantonId(Long projectId, Long cantonId) {
        String sql = "select distinct o from ProjectLocationAssigment o " +
                " inner join fetch o.location c " +
                " inner join fetch c.provincia p " +
                " where o.project.id =:projectId and o.location.id =:cantonId" +
                " order by p.description, c.description ";
        Query q = this.getEntityManager().createQuery(sql, ProjectLocationAssigment.class);
        q.setParameter("projectId",projectId);
        q.setParameter("cantonId",cantonId);
        return q.getResultList();
    }

}
