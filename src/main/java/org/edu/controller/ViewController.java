package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.User;
import org.edu.model.dto.MeetingDto;
import org.edu.service.MeetingService;
import org.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
public class ViewController {

    private static final int MEETING_ON_PAGE_NUM = 5;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingService meetingService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("username", user.getName());
            model.addAttribute("roles", user.getRoles());
        }
//        System.out.println(Converter.convert(meetingService.getUpcomingMeetings(MEETING_ON_PAGE_NUM, 0), MeetingDto.class));
        if (pageNum != null)
            model.addAttribute("meetings", Converter.convert(meetingService.getNewMeetings(MEETING_ON_PAGE_NUM, pageNum), MeetingDto.class));
        else
            model.addAttribute("meetings", Converter.convert(meetingService.getNewMeetings(MEETING_ON_PAGE_NUM, 0), MeetingDto.class));
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
    public String adminPage() {
        return "admin";
    }
}
