package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.User;
import org.edu.model.dto.CategoryDto;
import org.edu.model.dto.MeetingDto;
import org.edu.model.dto.StationDto;
import org.edu.service.CategoryService;
import org.edu.service.MeetingService;
import org.edu.service.StationService;
import org.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
@PropertySource({"classpath:ui.properties"})
public class ViewController {

    @Value("${meetings.per.page}")
    private int meetingOnPageNum;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StationService stationService;

    @RequestMapping(value = {"/new", "/"}, method = RequestMethod.GET)
    public String homePage(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        double pageCount = Math.ceil(meetingService.getMeetingsNumber() / (double) meetingOnPageNum);
        setMainVariablesToPage(principal, pageNum, "new", model, pageCount);
        model.addAttribute("meetings",
                Converter.convert(meetingService.getNewMeetings(meetingOnPageNum, pageNum == null ? 0 : pageNum), MeetingDto.class));
        return "home";
    }

    @RequestMapping(value = "/nearest", method = RequestMethod.GET)
    public String nearestMeetings(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        double pageCount = Math.ceil(meetingService.getUpcomingMeetingsNumber() / (double) meetingOnPageNum);
        setMainVariablesToPage(principal, pageNum, "nearest", model, pageCount);
        model.addAttribute("meetings",
                Converter.convert(meetingService.getUpcomingMeetings(meetingOnPageNum, pageNum == null ? 0 : pageNum), MeetingDto.class));
        return "home";
    }

    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public String popularMeetings(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        double pageCount = Math.ceil(meetingService.getPopularMeetingsNumber() / (double) meetingOnPageNum);
        setMainVariablesToPage(principal, pageNum, "popular", model, pageCount);
        model.addAttribute("meetings",
                Converter.convert(meetingService.getPopularMeetings(meetingOnPageNum, pageNum == null ? 0 : pageNum), MeetingDto.class));
        return "home";
    }

//    public String

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationPage() {
        return "registration";
    }

    @RequestMapping(value = "/admin")
    public String adminPage() {
        return "admin";
    }

    private void setMainVariablesToPage(Principal principal, Integer pageNum, String location, Model model, double pageCount) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("username", user.getName());
            model.addAttribute("roles", user.getRoles());
        }
//        double pageCount = Math.ceil(meetingService.findUpcomingMeetingsNumber() / (double) meetingOnPageNum);
        if (pageNum == null) {
            pageNum = 1;
        }
//        model.addAttribute("meetings", Converter.convert(meetingService.getUpcomingMeetings(meetingOnPageNum, pageNum), MeetingDto.class));
        model.addAttribute("currentPageNum", pageNum);
        if (pageNum > 1)
            model.addAttribute("leftArrowDisabled", false);
        else
            model.addAttribute("leftArrowDisabled", true);
        if (pageNum < pageCount)
            model.addAttribute("rightArrowDisabled", false);
        else
            model.addAttribute("rightArrowDisabled", true);
        model.addAttribute("active", pageNum);
        model.addAttribute("currentLocation", location);
        model.addAttribute("pageNum", pageCount);
    }

    @RequestMapping(value = "/meetings/create", method = RequestMethod.GET)
    public String createMeetingPage(Model model, Principal principal) {

        model.addAttribute("categories",
                Converter.convert(categoryService.getAllCategories(), CategoryDto.class));
        model.addAttribute("stations",
                Converter.convert(stationService.getAllStations(), StationDto.class));
        return "create_meeting";
    }
}
