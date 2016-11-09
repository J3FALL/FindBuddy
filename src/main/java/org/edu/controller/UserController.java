package org.edu.controller;

import org.edu.model.Comment;
import org.edu.service.CommentService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createComment(Principal principal, Comment comment) {
        if (principal == null) {
//            return ResponseEntity.badRequest().body("{\"message\" : \"Please login.\"}");
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }
        commentService.createComment(comment, principal);
//        return ResponseEntity.accepted().body("{\"message\" : \"Success.\"}");
        return ResponseEntity.accepted().body(new GenericResponse("Successful."));
    }
}
