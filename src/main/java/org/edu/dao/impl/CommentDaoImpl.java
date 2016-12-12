package org.edu.dao.impl;

import org.edu.dao.CommentDao;
import org.edu.dao.common.AbstractHibernateDao;
import org.edu.model.Comment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CommentDaoImpl extends AbstractHibernateDao<Comment> implements CommentDao {
    public CommentDaoImpl() {
        super();
        setClazz(Comment.class);
    }

    //    @Override
//    public Comment findById(long id) {
//        Session session = getCurrentSession();
//        Query query =
//    }
}
