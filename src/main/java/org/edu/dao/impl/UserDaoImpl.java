package org.edu.dao.impl;

import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository
public class UserDaoImpl extends AbstractHibernateDao<User> implements org.edu.dao.UserDao {

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

}
