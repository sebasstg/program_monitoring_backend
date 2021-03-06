package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.Objetive;
import org.unhcr.programMonitoring.model.Period;
import org.unhcr.programMonitoring.webServices.model.PeriodResumeWeb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PeriodDao extends GenericDaoJpa<Period, Long> {

    public PeriodDao() {
        super(Period.class, Long.class);
    }


    public Period getByYear(Integer year) {
        String sql = "select distinct o from Period o where o.year= :year";
        Query q = this.getEntityManager().createQuery(sql, Period.class).setParameter("year", year);
        try {
            return (Period) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Period> getByState(State state) {
        String sql = "select distinct o from Period o where o.state = :state order by o.year desc";
        Query q = this.getEntityManager().createQuery(sql, Period.class).setParameter("state", state);

        return q.getResultList();

    }

    public List<PeriodResumeWeb> getAllPeriodResumeWebOrderedByYear() {
        String sql = "SELECT " +
                "       p.id as id, " +
                "       p.state as state, " +
                "       p.year as year " +
                "    ,Count(pr.id) as numberOfProjects " +
                "    ,Count(i.id) as numberOfAsignedIndicators  " +
                "   FROM " +
                "       program_monitoring.periods AS p  " +
                "   LEFT OUTER JOIN " +
                "       program_monitoring.period_performance_indicator_assigments AS i  " +
                "           ON i.period_id = p.id and i.state ='ACTIVE'  " +
                "   LEFT OUTER JOIN " +
                "       program_monitoring.projects AS pr  " +
                "           ON pr.period_id = p.id and pr.state ='ACTIVE'  " +
                "   GROUP BY " +
                "       p.id, " +
                "       p.state, " +
                "       p.year  " +
                "   ORDER BY " +
                "       p.year";
        Query q = this.getEntityManager().createNativeQuery(sql, "periodResumeWebMapping");

        return q.getResultList();

    }

    public List<PeriodResumeWeb> getPeriodResumeWebByPeriodId(Long periodId) {
        String sql = "SELECT " +
                "       p.id as id, " +
                "       p.state as state, " +
                "       p.year as year " +
                "    ,Count(distinct pr.id) as numberOfProjects " +
                "    ,Count(distinct i.id) as numberOfAsignedIndicators  " +
                "   FROM " +
                "       program_monitoring.periods AS p  " +
                "   LEFT OUTER JOIN " +
                "       program_monitoring.period_performance_indicator_assigments AS i  " +
                "           ON i.period_id = p.id and i.state ='ACTIVE'  " +
                "   LEFT OUTER JOIN " +
                "       program_monitoring.projects AS pr  " +
                "           ON pr.period_id = p.id and pr.state ='ACTIVE'  " +
                "   WHERE pr.period_id =:periodId" +
                "   GROUP BY " +
                "       p.id, " +
                "       p.state, " +
                "       p.year  " +
                "   ORDER BY " +
                "       p.year";
        Query q = this.getEntityManager().createNativeQuery(sql, "periodResumeWebMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();

    }

}
