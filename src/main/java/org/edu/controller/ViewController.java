package org.edu.controller;

import org.edu.model.User;
import org.edu.service.StorageService;
import org.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;


@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @Autowired
    StorageService storageService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("username", user.getName());
            model.addAttribute("roles", user.getRoles());
        }
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String regisrationPage() {
        return "registration";
    }

    @RequestMapping(value = "/admin")
    public String adminPage(){
        return "admin";
    }
}
