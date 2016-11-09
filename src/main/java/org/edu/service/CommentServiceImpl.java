package org.edu.service;

import org.edu.dao.CommentDao;
import org.edu.dao.UserDao;
import org.edu.model.Comment;
import org.edu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserDao userDao;

    @Override
    public void createComment(Comment comment, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        comment.setAuthor(user);
        commentDao.create(comment);
    }
}
