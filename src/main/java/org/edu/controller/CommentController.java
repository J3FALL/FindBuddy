package org.edu.controller;

import org.edu.model.Comment;
import org.edu.model.dto.CommentDto;
import org.edu.service.CommentService;
import org.edu.util.GenericResponse;
import org.modelmapper.ModelMapper;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
@Transactional
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> createComment(Principal principal, @RequestBody CommentDto commentDto) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        }
        Comment comment = convertToEntity(commentDto);
        commentService.createComment(comment, principal);
        return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment:comments) {
            commentDtos.add(convertToDto(comment));
        }
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GenericResponse> updateComment(@RequestBody CommentDto commentDto, @PathVariable("id") long id, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        }
        Comment comment = convertToEntity(commentDto);
        comment.setId(id);
        boolean isSuccessUpdate = commentService.updateComment(comment, principal);
        if (isSuccessUpdate)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GenericResponse> deleteComment(@PathVariable("id") long id, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        }
        boolean isSuccessRemove = commentService.removeComment(id, principal);
        if (isSuccessRemove)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    private CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment convertToEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }


}
