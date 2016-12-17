package org.edu.dao.impl;

import org.edu.dao.UserDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Comment;
import org.edu.model.Meeting;
import org.edu.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class UserDaoImpl extends AbstractHibernateDao<User> implements UserDao {

    public UserDaoImpl() {
        super();
        setClazz(User.class);
    }

    @Override
    public User findByName(String name) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        Session session =  getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " where email = :email");
        query.setParameter("email", email);
        User user = (User) query.uniqueResult();
        return user;
    }

    @Override
    public List<Meeting> findSubscriptions(long userId, int pageNum, int num) {
        @SuppressWarnings("unchecked")
        List<Meeting> subscriptions = getCurrentSession()
                .createNativeQuery(
                        "select meetings.* " +
                                "from users " +
                                "inner join meeting_user " +
                                "on users.id = meeting_user.user_id " +
                                "inner join meetings " +
                                "on meetings.id = meeting_user.meeting_id " +
                                "where users.id = :a " +
                                "order by create_date desc"
                )
                .setParameter("a", userId)
                .addEntity(Meeting.class)
                .setMaxResults(num)
                .setFirstResult((pageNum - 1) * num)
                .getResultList();
        System.out.println(subscriptions.size());
        return subscriptions;
    }

    @Override
    public Long findSubscriptionsNum(long userId, int pageNum, int num) {
        return (Long) getCurrentSession()
                .createNativeQuery(
                        "select count(meetings.*) " +
                                "from users " +
                                "inner join meeting_user " +
                                "on users.id = meeting_user.user_id " +
                                "inner join meetings " +
                                "on meetings.id = meeting_user.meeting_id " +
                                "where users.id = :a "
                )
                .setParameter("a", userId)
                .addScalar("count", StandardBasicTypes.LONG)
                .uniqueResult();
    }
}
