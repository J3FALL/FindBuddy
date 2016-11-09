package org.edu.dao.impl;

import org.edu.dao.CommentDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDaoImpl extends AbstractHibernateDao<Comment> implements CommentDao {
}
