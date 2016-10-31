package org.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {

    @RequestMapping("/registration")
    public String regisrationPage() {
        return "registration";
    }

//    @RequestMapping("/perform_registration")
//    public String registerUser()
}
