package org.edu.service;

import org.edu.dao.CommentDao;
import org.edu.dao.UserDao;
import org.edu.model.Comment;
import org.edu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

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

    @Override
    public List<Comment> getAllComments() {
        return commentDao.findAll();
    }

    @Override
    public Comment getCommentById(long id) {
        return commentDao.findOne(id);
    }

    @Override
    public boolean updateComment(Comment comment, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        if (user.getId() == comment.getAuthor().getId()) {
            commentDao.update(comment);
            return true;
        }
        return false;
    }

    @Override
    public void removeComment(long id) {
        User user = commentDao.findOne(id).getAuthor();
        List<Comment> comments = user.getComments();
        for (Comment comment : comments) {
            if (comment.getId() == id) {
                comments.remove(comment);
                break;
            }
        }
    }
}
