package org.edu.service;

import org.edu.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public interface CommentService {

    void createComment(Comment comment, Principal principal);

    List<Comment> getAllComments();

    Comment getCommentById(long id);

    boolean updateComment(Comment comment, Principal principal);

    void removeComment(long id);


//    List<Comment> getCommentByMeeting(Meeting meeting);
}
