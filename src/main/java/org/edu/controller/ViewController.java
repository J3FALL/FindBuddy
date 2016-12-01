package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Category;
import org.edu.model.Meeting;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
        List<Meeting> newMeetings = meetingService.getNewMeetings(meetingOnPageNum, pageNum == null ? 0 : pageNum);
        List<MeetingDto> newMeetingDtos = Converter.convert(newMeetings, MeetingDto.class);
        setHeaderVariables(model, principal);
        setMeetingVariables(model, newMeetingDtos, pageNum, "new", pageCount);
        return "home";
    }

    @RequestMapping(value = "/nearest", method = RequestMethod.GET)
    public String nearestMeetings(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        double pageCount = Math.ceil(meetingService.getUpcomingMeetingsNumber() / (double) meetingOnPageNum);
        List<Meeting> upcomingMeetings = meetingService.getUpcomingMeetings(meetingOnPageNum, pageNum == null ? 0 : pageNum);
        List<MeetingDto> upcomingMeetingDtos = Converter.convert(upcomingMeetings, MeetingDto.class);
        setHeaderVariables(model, principal);
        setMeetingVariables(model, upcomingMeetingDtos, pageNum, "nearest", pageCount);
        return "home";
    }

    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public String popularMeetings(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        double pageCount = Math.ceil(meetingService.getPopularMeetingsNumber() / (double) meetingOnPageNum);
        List<Meeting> popularMeetings = meetingService.getPopularMeetings(meetingOnPageNum, pageNum == null ? 0 : pageNum);
        List<MeetingDto> popularMeetingDtos = Converter.convert(popularMeetings, MeetingDto.class);
        setHeaderVariables(model, principal);
        setMeetingVariables(model, popularMeetingDtos, pageNum, "popular", pageCount);
        return "home";
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String subscribedCategoryMeetings(Model model, Principal principal, @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        double pageCount = Math.ceil(meetingService.getFeedNumber(principal.getName()) / (double) meetingOnPageNum);
        List<Meeting> userFeedMeetings = meetingService.getFeed(meetingOnPageNum, pageNum == null ? 1 : pageNum, principal.getName());
        List<MeetingDto> userFeedMeetingDtos = Converter.convert(userFeedMeetings, MeetingDto.class);
        setHeaderVariables(model, principal);
        setMeetingVariables(model, userFeedMeetingDtos, pageNum, "feed", pageCount);
        meetingService.addUserToWaiting(principal.getName());
        return "feed";
    }

    private void setHeaderVariables(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("username", user.getName());
            model.addAttribute("roles", user.getRoles());
            model.addAttribute("userId", user.getId());
        }
    }

    private void setMeetingVariables(Model model, List<MeetingDto> meetingDtos,
                                     Integer pageNum, String location, double pageCount) {
        if (pageNum == null) {
            pageNum = 1;
        }
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
        model.addAttribute("meetings", meetingDtos);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String showAllCategories(Model model, Principal principal) {
        setHeaderVariables(model, principal);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        Set<Category> userCategories = null;
        if (principal != null) {
            userCategories = userService.getUserByEmail(principal.getName()).getCategories();
        }
        model.addAttribute("userCategories", userCategories);
        setCategoriesVariables(model, principal, categories, "categories");
        return "categories";
    }

    private void setCategoriesVariables(Model model, Principal principal, List<Category> categories, String location) {
        model.addAttribute("categories", categories);
        Set<Category> userCategories = null;
        if (principal != null) {
            userCategories = userService.getUserByEmail(principal.getName()).getCategories();
        }
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("currentLocation", location);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String showUserPage(Model model, Principal principal, @PathVariable("id") long id,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        User user = userService.getUserById(id);
        model.addAttribute("id", id);
        int userMeetingsNum = user.getCreatedMeetings().size();
        int userCommentsNum = user.getComments().size();
        int userCategoriesNum = user.getCategories().size();
        model.addAttribute("userMeetingsNum", userMeetingsNum);
        model.addAttribute("userCommentsNum", userCommentsNum);
        model.addAttribute("userCategoriesNum", userCategoriesNum);
        model.addAttribute("userName", user.getName() + " " + user.getSurname());
        model.addAttribute("photo", user.getPhoto());
        setHeaderVariables(model, principal);
        if (location == null)
            location = "info";
        switch (location) {
            case "categories":
                List<Category> userCategories = new ArrayList<>(user.getCategories());
                setCategoriesVariables(model, principal, userCategories, "categories");
                break;
            case "meetings":
                if (pageNum == null)
                    pageNum = 1;
                List<Meeting> userMeetings = new ArrayList<>(user.getCreatedMeetings());
                double pageCount = Math.ceil(userMeetings.size() / (double) meetingOnPageNum);
                List<MeetingDto> userMeetingsDtos;
                if ((pageNum - 1) * meetingOnPageNum >= userMeetings.size()) {
                    userMeetingsDtos = new ArrayList<>();
                }
                else if ((pageNum - 1) * meetingOnPageNum + meetingOnPageNum >= userMeetings.size()) {
                    userMeetingsDtos =  Converter.convert(userMeetings.subList((pageNum - 1) * meetingOnPageNum,
                            userMeetings.size()), MeetingDto.class);
                }
                else {
                    userMeetingsDtos = Converter.convert(userMeetings.subList(pageNum - 1,
                            pageNum + meetingOnPageNum), MeetingDto.class);
                }
                setMeetingVariables(model, userMeetingsDtos, pageNum, location, pageCount);
                break;
            case "info":
                model.addAttribute("user", user);
                model.addAttribute("currentLocation", "info");
                break;
            case "comments":
                model.addAttribute("currentLocation", "comments");
                setHeaderVariables(model, principal);
        }
        return "user_page";
    }

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

    @RequestMapping(value = "/meetings/create", method = RequestMethod.GET)
    public String createMeetingPage(Model model, Principal principal) {

        model.addAttribute("categories",
                Converter.convert(categoryService.getAllCategories(), CategoryDto.class));
        model.addAttribute("stations",
                Converter.convert(stationService.getAllStations(), StationDto.class));
        setHeaderVariables(model, principal);
        return "create_meeting";
    }
}
