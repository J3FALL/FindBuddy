package org.edu.dao.impl;

import org.edu.dao.MeetingDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Meeting;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MeetingDaoImpl extends AbstractHibernateDao<Meeting> implements MeetingDao {

    public MeetingDaoImpl() {
        super();
        setClazz(Meeting.class);
    }

    @Override
    public List<Meeting> findNewMeetings(int num, int pageNum) {
        Session session = getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Meeting> newMeetings = session.createQuery("from Meeting u order by createDate desc")
                .setMaxResults(num)
                .setFirstResult(pageNum * num)
                .getResultList();
        return newMeetings;
    }

    @Override
    public List<Meeting> findUpcomingMeetings(int num, int pageNum) {
        Session session = getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Meeting> upcomingMeetings =
                session.createNativeQuery("select * from meetings " +
                        "where start_date > now() " +
                        "ORDER BY start_date - now()")
                        .addEntity(Meeting.class)
                        .setMaxResults(num)
                        .setFirstResult(pageNum * num)
                        .getResultList();
        return upcomingMeetings;
    }

    @Override
    //TODO: realize this
    public List<Meeting> findPopularMeetings(int num, int pageNum) {
        return null;
    }
}
