package org.edu.dao.impl;

import org.edu.dao.IUserDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao extends AbstractHibernateDao<User> implements IUserDao {

    public UserDao() {
        super();

        setClazz(User.class);
    }

    @Override
    public User findByName(String name) {
        Session session =  getCurrentSession();
        Query query = session.createQuery("from " + User.class.getName() + " where name = :name");
        query.setParameter("name", name);
        User user = (User) query.uniqueResult();
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

}
