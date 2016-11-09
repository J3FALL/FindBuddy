package org.edu.service;

import org.edu.model.Comment;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface CommentService {

    void createComment(Comment comment, Principal principal);
}
