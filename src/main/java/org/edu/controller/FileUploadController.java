package org.edu.controller;

import org.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;


@RestController
public class FileUploadController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/upload_image", method = RequestMethod.POST)
    public String uploadUserPhoto(Principal principal, @RequestParam("file") MultipartFile photo) throws IOException {
        if (photo.isEmpty())
            return "File is empty";
        String path = userService.uploadPhoto(photo, principal);
        if (path == null) {
            return "Not valid image";
        }
        return userService.uploadPhoto(photo, principal);
    }

    @RequestMapping(value = "/delete_image", method = RequestMethod.DELETE)
    public void deleteUserPhoto(Principal principal) {
        userService.deletePhoto(principal);
    }
}
