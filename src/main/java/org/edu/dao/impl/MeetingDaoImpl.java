package org.edu.dao.impl;

import org.edu.dao.MeetingDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Meeting;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

/**
 * Created by Pavel on 13.11.2016.
 */
@Repository
@Transactional
public class MeetingDaoImpl extends AbstractHibernateDao<Meeting> implements MeetingDao {

    public MeetingDaoImpl() {
        super();
        setClazz(Meeting.class);
    }
}
