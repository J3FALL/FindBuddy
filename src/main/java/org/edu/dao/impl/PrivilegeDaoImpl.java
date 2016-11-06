package org.edu.dao.impl;

import org.edu.dao.PrivilegeDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Privilege;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class PrivilegeDaoImpl extends AbstractHibernateDao<Privilege> implements PrivilegeDao {

    public PrivilegeDaoImpl(){
        super();

        setClazz(Privilege.class);
    }

    @Override
    public Privilege findByName(String name) {
        Session session = getCurrentSession();
        Query query = session.createQuery("from " + clazz.getName() + " where name = :name");
        query.setParameter("name", name);
        Privilege privilege = (Privilege) query.uniqueResult();
        return privilege;
    }
}
