package org.edu.service.impl;

import org.edu.dao.CommentDao;
import org.edu.dao.MeetingDao;
import org.edu.dao.UserDao;
import org.edu.model.Comment;
import org.edu.model.User;
import org.edu.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MeetingDao meetingDao;

    @Override
    public Comment createComment(Comment comment, Principal principal) {
        User author = userDao.findByEmail(principal.getName());
        comment.setAuthor(author);
        comment.setDate(LocalDateTime.now());
        comment.setMeeting(meetingDao.findOne(comment.getMeeting().getId()));
        commentDao.create(comment);
        return comment;
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
        User principalUser = userDao.findByEmail(principal.getName());
        Comment updatedComment = commentDao.findOne(comment.getId());
        if (updatedComment != null && principalUser.getId() == updatedComment.getAuthor().getId()) {
            updatedComment.setText(comment.getText());
            commentDao.update(updatedComment);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeComment(long id, Principal principal) {
        User user = commentDao.findOne(id).getAuthor();
        User principalUser = userDao.findByEmail(principal.getName());
        if (principalUser.getId() != user.getId())
            return false;
        Set<Comment> comments = user.getComments();
        for (Comment comment : comments) {
            if (comment.getId() == id) {
                comments.remove(comment);
                return true;
            }
        }
        return false;
    }

//    @Override
//    public List<Comment> getCommentByMeeting(Meeting meeting) {
//
//    }
}
