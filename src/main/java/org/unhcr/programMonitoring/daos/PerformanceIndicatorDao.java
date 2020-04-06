package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.IndicatorType;
import org.unhcr.programMonitoring.model.PercentageType;
import org.unhcr.programMonitoring.model.PerformanceIndicator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class PerformanceIndicatorDao extends GenericDaoJpa<PerformanceIndicator, Long> {

    public PerformanceIndicatorDao() {
        super(PerformanceIndicator.class, Long.class);
    }


    public List<PerformanceIndicator> getAllOrderedByCode() {
        String sql = "select distinct o from PerformanceIndicator o order by o.description";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class);

        return q.getResultList();

    }


    public List<PerformanceIndicator> getMainOrderedByCode() {
        PercentageType percentageType = PercentageType.PERCENTAGE;
        String sql = "select distinct o from PerformanceIndicator o " +
                " where o.percentageType is null or  o.percentageType =:percentageType " +
                " order by o.description";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class);
        q.setParameter("percentageType",percentageType);

        return q.getResultList();

    }



    public List<PerformanceIndicator> getByDescription(String description) {
        String sql = "select distinct o from PerformanceIndicator o where o.description =:description order by o.description ";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class).setParameter("description", description);

        return q.getResultList();

    }


    public List<PerformanceIndicator> getByOutputId(Long id) {
        String sql = "select distinct o from PerformanceIndicator o where o.output.id =:id order by o.description ";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class).setParameter("id", id);
        return q.getResultList();
    }

    public List<PerformanceIndicator> getByStateAndPeriodIdandOutputId(Long periodoId, State state, Long outputId) {

        String sql = "select distinct pi from PeriodPerformanceIndicatorAssigment ppia  inner join ppia.performanceIndicator pi " +
                " inner join pi.output o inner join o.objetive ob inner join ob.rightGroup r " +
                "where ppia.period.id = :periodoId and ppia.state = :state and o.id = :outputId" +
                " order by pi.description ";

        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class);
        q.setParameter("state", state);
        q.setParameter("periodoId", periodoId);
        q.setParameter("outputId", outputId);

        return q.getResultList();
    }

    public List<PerformanceIndicator> getByStateAndPeriodIdandOutputIdAndIndicatorType(Long periodoId, State state, Long outputId, IndicatorType indicatorType) {

        String sql = "select distinct pi from PeriodPerformanceIndicatorAssigment ppia  inner join ppia.performanceIndicator pi " +
                " inner join pi.output o inner join o.objetive ob inner join ob.rightGroup r " +
                "where ppia.period.id = :periodoId and ppia.state = :state and o.id = :outputId " +
                " and pi.indicatorType =:indicatorType " +
                " order by pi.description ";

        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class);
        q.setParameter("state", state);
        q.setParameter("periodoId", periodoId);
        q.setParameter("outputId", outputId);
        q.setParameter("indicatorType", indicatorType);

        return q.getResultList();
    }

    public List<PerformanceIndicator> getByIndicatorType(IndicatorType indicatorType, State state) {
        String sql = "select distinct o from PerformanceIndicator o where o.indicatorType =:indicatorType and o.state =:state order by o.description";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class)
                .setParameter("indicatorType", indicatorType)
                .setParameter("state", state);
        return q.getResultList();
    }

    public List<PerformanceIndicator> getByOutputIdAndType(Long id, IndicatorType type) {
        String sql = "select distinct o from PerformanceIndicator o where o.output.id =:id and o.indicatorType =:type order by o.description ";
        Query q = this.getEntityManager().createQuery(sql, PerformanceIndicator.class)
                .setParameter("id", id)
                .setParameter("type",type);
        return q.getResultList();
    }
}
