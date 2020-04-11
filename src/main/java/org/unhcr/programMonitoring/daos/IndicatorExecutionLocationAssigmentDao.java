package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.IndicatorExecutionLocationAssigment;

import javax.ejb.Stateless;

@Stateless
public class IndicatorExecutionLocationAssigmentDao extends GenericDaoJpa<IndicatorExecutionLocationAssigment, Long> {

    public IndicatorExecutionLocationAssigmentDao() {
        super(IndicatorExecutionLocationAssigment.class, Long.class);
    }



}
