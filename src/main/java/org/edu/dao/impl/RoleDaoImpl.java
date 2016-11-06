package org.edu.dao.impl;

import org.edu.dao.RoleDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Role;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository
public class RoleDaoImpl extends AbstractHibernateDao<Role> implements RoleDao{

    public RoleDaoImpl() {
        super();

        setClazz(Role.class);
    }

    @Override
    public Role findByName(String name) {
        Session session = getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " where name = :name");
        query.setParameter("name", name);
        Role role = (Role) query.uniqueResult();
        return role;
    }
}
