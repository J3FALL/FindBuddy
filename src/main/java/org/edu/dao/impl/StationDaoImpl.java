package org.edu.dao.impl;

import org.edu.dao.StationDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Station;
import org.springframework.stereotype.Repository;
/**
 * Created by Ленизка on 13.11.2016.
 */
@Repository
public class StationDaoImpl extends AbstractHibernateDao<Station> implements StationDao{

    public StationDaoImpl() {
        super();
        setClazz(Station.class);
    }
}
