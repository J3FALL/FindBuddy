package org.edu.controller;

import org.edu.model.User;
import org.edu.model.dto.UserDTO;
import org.edu.service.UserService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(UserDTO userDTO) {
        User user = userService.registerNewUser(userDTO);
        System.out.println(user);
        if (user == null)
            return new ResponseEntity<GenericResponse>(new GenericResponse("fail"), HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<GenericResponse>(new GenericResponse("success"), HttpStatus.OK);
    }
}
