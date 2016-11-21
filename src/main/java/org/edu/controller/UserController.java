package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Category;
import org.edu.model.Comment;
import org.edu.model.User;
import org.edu.model.dto.CategoryDto;
import org.edu.model.dto.CommentDto;
import org.edu.model.dto.UserDto;
import org.edu.service.UserService;
import org.edu.util.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController{

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> registerUser(@RequestBody UserDto newUser) {
        User user = Converter.convert(newUser, User.class);
        user = userService.createUser(user);
        if (user == null)
            return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUser(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(Converter.convert(user, UserDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GenericResponse> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        }
        User user = Converter.convert(userDto, User.class);
        user.setId(id);
        boolean isSuccess = userService.updateUser(user, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable("id") long id, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        }
        boolean isSuccess = userService.deleteUser(id, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<CommentDto>> getUserComments(@PathVariable("id") long id) {
        Set<Comment> comments = userService.getUserComments(id);
        if (comments == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment:comments) {
            commentDtos.add(Converter.convert(comment, CommentDto.class));
        }
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/categories", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getUserCategories(@PathVariable("id") long id){
        Set<Category> categories = userService.getUserCategories(id);
        if (categories == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category:categories) {
            categoryDtos.add(Converter.convert(category, CategoryDto.class));
        }
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

//    @RequestMapping(value = "/upload_image")
//    public void uploadUserImage(Principal principal, @RequestParam("file")MultipartFile file) {
////        ProfileImageDto profileImageDto = new ProfileImageDto();
////        profileImageDto.setFile(file);
////        try {
////            imageUtil.processProfileImage(profileImageDto, principal.getName());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        fileStorageService.store(file);
//    }
}
