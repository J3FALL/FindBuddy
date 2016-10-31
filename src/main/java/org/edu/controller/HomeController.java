package org.edu.controller;

import org.edu.model.User;
import org.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.getUser(email);
            model.addAttribute("username", user.getName());
            model.addAttribute("roles", user.getRoles());
        }
        return "index";
    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String homePage(Model model) {
//        return "index";
//    }
}
