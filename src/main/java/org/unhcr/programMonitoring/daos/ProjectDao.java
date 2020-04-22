package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.model.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ProjectDao extends GenericDaoJpa<Project, Long> {

    public ProjectDao() {
        super(Project.class, Long.class);
    }


    public List<Project> getByState(State state) {
        String sql = "select distinct o from Project o where o.state = :state ";
        Query q = this.getEntityManager().createQuery(sql, Period.class).setParameter("state", state);

        return q.getResultList();

    }

    public List<Project> getByNamePeriodAndImplementorId(String name, Long idPeriod, Long idProjectImplementer) {

        String sql = "select distinct o from Project o where o.name = :name and o.period.id=:idPeriod " +
                "and o.projectImplementer.id =:idProjectImplementer  order by o.code";
        Query q = this.getEntityManager().createQuery(sql, Project.class)
                .setParameter("name", name)
                .setParameter("idPeriod", idPeriod)
                .setParameter("idProjectImplementer", idProjectImplementer);

        return q.getResultList();
    }

    public List<Project> getByPeriodId(Long idPeriod) {

        String sql = "select distinct o from Project o where o.period.id=:idPeriod order by o.code";
        Query q = this.getEntityManager().createQuery(sql, Project.class)
                .setParameter("idPeriod", idPeriod);

        return q.getResultList();
    }

    public List<Project> getByPeriodIdAndImplementerId(Long idPeriod, Long implementerId) {

        String sql = "select distinct o from Project o where o.period.id=:idPeriod  order by o.code";
        String implementerFilter = " and o.projectImplementer.id=:implementerId ";

        if (implementerId != null) {
            sql = sql + implementerFilter;
        }
        Query q = this.getEntityManager().createQuery(sql, Project.class)
                .setParameter("idPeriod", idPeriod);
        if (implementerId != null) {
            q.setParameter("implementerId", implementerId);
        }

        return q.getResultList();
    }

    public List<Project> getByImplementerId(Long idImplementer) {
        String sql = "select distinct o from Project o where o.projectImplementer.id=:idImplementer  order by o.code";
        Query q = this.getEntityManager().createQuery(sql, Project.class)
                .setParameter("idImplementer", idImplementer);

        return q.getResultList();
    }


}
