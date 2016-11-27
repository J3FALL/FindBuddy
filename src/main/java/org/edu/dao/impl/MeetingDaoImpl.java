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
                                "where create_date between now() - interval '7 days' and now() " +
                                "order by count_user.users_num")
                .addEntity(Meeting.class)
                .setMaxResults(num)
                .setFirstResult((pageNum - 1) * num)
                .getResultList();
        return meetings;
    }

    @Override
    public List<Meeting> findSubscribedCategoryMeetings(int num, int pageNum, String userName) {
        @SuppressWarnings("unchecked")
        List<Meeting> meetings = getCurrentSession()
                .createNativeQuery(
                        "select meetings.* " +
                                "from categories " +
                                "inner join category_users " +
                                "on categories.id = category_users.category_id " +
                                "inner join users " +
                                "on category_users.user_id = users.id " +
                                "inner join meetings " +
                                "on categories.id = meetings.category_id " +
                                "where users.email = :user_name and create_date between now() - interval '7 days' and now()" +
                                "order by create_date desc"
                )
                .setParameter("user_name", userName)
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

    @Override
    public long findSubscribedCategoryMeetingsNumber(String userName) {
        long count = (Long) getCurrentSession()
                .createNativeQuery("select count(*) as count " +
                        "from categories " +
                        "inner join category_users " +
                        "on categories.id = category_users.category_id " +
                        "inner join users " +
                        "on category_users.user_id = users.id " +
                        "inner join meetings " +
                        "on categories.id = meetings.category_id " +
                        "where users.name = :user_name and create_date between now() - interval '7 days' and now()")
                .setParameter("user_name", userName)
                .addScalar("count", StandardBasicTypes.LONG)
                .uniqueResult();
        return count;
    }
}
