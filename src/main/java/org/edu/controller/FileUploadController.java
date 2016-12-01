package org.edu.controller;

import org.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;


@Controller
public class FileUploadController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/upload_image", method = RequestMethod.POST)
    public String uploadUserImage(Model model, Principal principal, @RequestParam("file") MultipartFile photo) throws IOException {
        userService.uploadPhoto(photo, principal);
        model.addAttribute("image", userService.getUserByEmail(principal.getName()).getPhoto());
        return "upload";
    }

    @RequestMapping(value = "/upload_image", method = RequestMethod.GET)
    public String uploadPage() {
        return "upload";
    }
}
