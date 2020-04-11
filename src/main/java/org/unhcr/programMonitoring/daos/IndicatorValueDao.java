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


}
