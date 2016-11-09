package org.edu.dao.impl;

import org.edu.dao.UserDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Comment;
import org.edu.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
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
}
