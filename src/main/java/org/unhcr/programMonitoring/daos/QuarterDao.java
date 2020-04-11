package org.unhcr.programMonitoring.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.programMonitoring.model.Canton;
import org.unhcr.programMonitoring.model.Quarter;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class QuarterDao extends GenericDaoJpa<Quarter, Long> {

    public QuarterDao() {
        super(Quarter.class, Long.class);
    }

}
