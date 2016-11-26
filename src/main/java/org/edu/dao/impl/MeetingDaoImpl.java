package org.edu.dao.impl;

import org.edu.dao.MeetingDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Meeting;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
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
        List<Meeting> newMeetings =
                session.createNativeQuery("select * " +
                        "from meetings " +
                        "where create_date between now() - interval '7 days' and now()" +
                        "order by create_date desc")
                        .addEntity(Meeting.class)
                        .setMaxResults(num)
                        .setFirstResult((pageNum - 1) * num)
                        .getResultList();
        return newMeetings;
    }

    @Override
    public List<Meeting> findUpcomingMeetings(int num, int pageNum) {
        Session session = getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Meeting> upcomingMeetings = session.createNativeQuery(
                "select * from meetings " +
                        "where start_date > now() " +
                        "ORDER BY start_date - now()")
                .addEntity(Meeting.class)
                .setMaxResults(num)
                .setFirstResult((pageNum - 1) * num)
                .getResultList();
        return upcomingMeetings;
    }

    @Override
    public List<Meeting> findPopularMeetings(int num, int pageNum) {
        @SuppressWarnings("unchecked")
        List<Meeting> meetings = getCurrentSession()
                .createNativeQuery(
                        "select meetings.*, count_user.users_num from meetings left join " +
                                "(select meeting_id, count(user_id) as users_num from meeting_user group by meeting_id) as count_user " +
                        "on meetings.id = count_user.meeting_id " +
                        "order by count_user.users_num")
                .addEntity(Meeting.class)
                .setMaxResults(num)
                .setFirstResult((pageNum - 1) * num)
                .getResultList();
        return meetings;
    }

    @Override
    public long findMeetingsNumber() {
        long count = (Long) getCurrentSession().createNativeQuery(
                "select count(*) as count from meetings " +
                        "where create_date between now() - interval '7 days' and now()")
                .addScalar("count", StandardBasicTypes.LONG)
                .uniqueResult();
        return count;
    }

    @Override
    public long findUpcomingMeetingsNumber() {
        long count = (Long) getCurrentSession().createNativeQuery(
                "select count(*) from Meeting " +
                        "where start_date > now()")
                .addEntity(Meeting.class)
                .iterate()
                .next();
        return count;
    }

    @Override
    public long findPopularMeetingsNumber() {
        long count = (Long) getCurrentSession()
                .createNativeQuery(
                        "select count(*) as count from meetings left join " +
                                "(select meeting_id, count(user_id) as users_num from meeting_user group by meeting_id) as count_user " +
                                "on meetings.id = count_user.meeting_id")
                .addScalar("count", StandardBasicTypes.LONG)
                .uniqueResult();
        return count;
    }
}
