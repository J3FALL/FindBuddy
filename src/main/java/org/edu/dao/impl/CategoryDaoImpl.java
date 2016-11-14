package org.edu.dao.impl;

import org.edu.dao.CategoryDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl extends AbstractHibernateDao<Category> implements CategoryDao {
}