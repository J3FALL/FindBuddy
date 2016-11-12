package org.edu.controller;

import org.edu.model.Comment;
import org.edu.service.CommentService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/comments")
@Transactional
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createComment(Principal principal, @RequestBody Comment comment) {
        if (principal == null) {
//            return ResponseEntity.badRequest().body("{\"message\" : \"Please login.\"}");
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }
        commentService.createComment(comment, principal);
//        return ResponseEntity.accepted().body("{\"message\" : \"Success.\"}");
        return ResponseEntity.accepted().body(new GenericResponse("Successful."));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getAllComments() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(HttpServletResponse response, @RequestBody Comment comment, @PathVariable("id") long id, Principal principal) {
        if (principal == null) {
//            return ResponseEntity.badRequest().body("{\"message\" : \"Please login.\"}");
//            response.sendRedirect("/login");
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }
        comment.setId(id);
        boolean isSuccess = commentService.updateComment(comment, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable("id") long id, Principal principal) {
        if (principal == null) {
//            return ResponseEntity.badRequest().body("{\"message\" : \"Please login.\"}");
//            response.sendRedirect("/login");
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }
        commentService.removeComment(id);
        return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.BAD_REQUEST);
    }


}
