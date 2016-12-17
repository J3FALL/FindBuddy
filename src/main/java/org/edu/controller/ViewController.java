package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Category;
import org.edu.model.Comment;
import org.edu.model.Meeting;
import org.edu.model.User;
import org.edu.model.dto.CategoryDto;
import org.edu.model.dto.CommentDto;
import org.edu.model.dto.MeetingDto;
import org.edu.model.dto.UserDto;
import org.edu.service.CategoryService;
import org.edu.service.CommentService;
import org.edu.service.MeetingService;
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
import java.util.Collections;
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
    private CommentService commentService;

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
            if (user != null) {
                model.addAttribute("username", user.getName());
                model.addAttribute("roles", user.getRoles());
            }
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
        setHeaderVariables(model, principal);
        User user;
        if (id != 0)
            user = userService.getUserById(id);
        else
            user = userService.getUserByEmail(principal.getName());
        model.addAttribute("id", id);
        int userMeetingsNum = user.getCreatedMeetings().size();
        int userCommentsNum = user.getComments().size();
        int userCategoriesNum = user.getCategories().size();
        model.addAttribute("userMeetingsNum", userMeetingsNum);
        model.addAttribute("userCommentsNum", userCommentsNum);
        model.addAttribute("userCategoriesNum", userCategoriesNum);
        model.addAttribute("userName", user.getName() + " " + user.getSurname());
        model.addAttribute("photo", user.getPhoto() == null ? "no_photo.png" : user.getPhoto());
        if (location == null)
            location = "info";
        model.addAttribute("currentLocation", location);
        switch (location) {
            case "categories":
                List<Category> userCategories = new ArrayList<>(user.getCategories());
                setCategoriesVariables(model, principal, userCategories, "categories");
                break;
            case "meetings":
                if (pageNum == null)
                    pageNum = 1;
                List<Meeting> userMeetings = new ArrayList<>(user.getCreatedMeetings());
                double pageCount = Math.ceil(userMeetingsNum / (double) meetingOnPageNum);
                List<MeetingDto> userMeetingsDtos;
                if ((pageNum - 1) * meetingOnPageNum >= userMeetingsNum) {
                    userMeetingsDtos = new ArrayList<>();
                } else if ((pageNum - 1) * meetingOnPageNum + meetingOnPageNum >= userMeetingsNum) {
                    userMeetingsDtos = Converter.convert(userMeetings.subList((pageNum - 1) * meetingOnPageNum,
                            userMeetings.size()), MeetingDto.class);
                } else {
                    userMeetingsDtos = Converter.convert(userMeetings.subList((pageNum - 1) * meetingOnPageNum,
                            (pageNum - 1) * meetingOnPageNum + meetingOnPageNum), MeetingDto.class);
                }
                setMeetingVariables(model, userMeetingsDtos, pageNum, location, pageCount);
                break;
            case "info":
                model.addAttribute("user", Converter.convert(user, UserDto.class));
                break;
            case "comments":
                List<Comment> comments = new ArrayList<>(user.getComments());
                model.addAttribute("comments", Converter.convert(comments, CommentDto.class));
                break;
        }
        return "user_page";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model,@RequestParam(required = false) String error) {
        if (error != null)
            model.addAttribute("error", true);
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
        setHeaderVariables(model, principal);
        return "create_meeting";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String uploadPage(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        setHeaderVariables(model, principal);
        model.addAttribute("photo", user.getPhoto() == null ? "no_photo.png" : user.getPhoto());
        model.addAttribute("user", Converter.convert(user, UserDto.class));
        return "settings";
    }

    @RequestMapping(value = "/meeting/{id}", method = RequestMethod.GET)
    public String getMeetingPage(Model model, @PathVariable(value = "id") long id,
                                 @RequestParam(required = false, name = "location") String currentLocation,
                                 Principal principal) {
        setHeaderVariables(model, principal);
        if (currentLocation == null)
            currentLocation = "comments";
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null)
            return "access_denied";
        boolean alreadySubscribed = false;
        boolean isAuthor = false;
        boolean meetingHasAuthor = meeting.getAuthor() != null;
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            alreadySubscribed = meeting.getSubscribedUsers().contains(user);
            isAuthor = meetingHasAuthor && meeting.getAuthor().equals(user);
        }
        switch (currentLocation) {
            case "comments":
                List<Comment> meetingComments = new ArrayList<>(meeting.getComments());
                Collections.sort(meetingComments, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                model.addAttribute("comments", Converter.convert(meetingComments, CommentDto.class));
                break;
            case "subscribers":
                List<User> meetingSubscribers = new ArrayList<>(meeting.getSubscribedUsers());
                model.addAttribute("subscribers", Converter.convert(meetingSubscribers, UserDto.class));
        }
        model.addAttribute("meeting", Converter.convert(meeting, MeetingDto.class));
        model.addAttribute("currentLocation", currentLocation);
        model.addAttribute("alreadySubscribed", alreadySubscribed);
        model.addAttribute("isAuthor", isAuthor);
        //model.addAttribute("user", Converter.convert(user, UserDto.class));
        return "meeting";
    }

    @RequestMapping(value = "/meeting/{id}/settings", method = RequestMethod.GET)
    public String meetingSettingPage(Model model, Principal principal, @PathVariable("id") Long id) {
        setHeaderVariables(model, principal);
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting != null) {
            if (principal.getName() != null && meeting.getAuthor().equals(userService.getUserByEmail(principal.getName())))
                model.addAttribute("meeting", Converter.convert(meeting, MeetingDto.class));
            else return "access_denied";
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "meeting_settings";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchMeeting(Model model, @RequestParam(name = "searchString", required = false) String searchString,
                              @RequestParam(name = "pageNum",required = false) Integer pageNum,
                                Principal principal) {
        setHeaderVariables(model, principal);
        if (pageNum == null)
            pageNum = 1;
        List<Meeting> meetings = meetingService.getMeetingBySearchString(searchString, pageNum, meetingOnPageNum);
        double pageCount =
                Math.ceil(meetingService.getMeetingBySearchStringNum(searchString, pageNum, meetingOnPageNum) / (double) meetingOnPageNum);
        if (meetings != null) {
            setMeetingVariables(model, Converter.convert(meetings, MeetingDto.class), pageNum, "search", pageCount);
            System.out.println(pageCount);
        }
        model.addAttribute("searchString", searchString);
        return "search_result";
    }

    @RequestMapping(value = "/meetings_category", method = RequestMethod.GET)
    public String getMeetingsByCategory(Model model, @RequestParam(name = "category", required = false) Long categoryId,
                                        @RequestParam(name = "pageNum", required = false) Integer pageNum,
                                        Principal principal) {
        setHeaderVariables(model, principal);
        Category category = categoryService.getCategoryById(categoryId);
        if (pageNum == null)
            pageNum = 1;
        List<Meeting> meetings = meetingService.getMeetingByCategory(categoryId, pageNum, meetingOnPageNum);
        double pageCount =
                Math.ceil(meetingService.getMeetingByCategoryNum(categoryId, pageNum, meetingOnPageNum) / (double) meetingOnPageNum);
        if (meetings != null) {
            setMeetingVariables(model, Converter.convert(meetings, MeetingDto.class), pageNum, "meetings_category", pageCount);
        }
        model.addAttribute("category", categoryId);
        model.addAttribute("categoryName", category.getName());
        return "meetings_category";
    }

    @RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
    public String getSubscriptions(Model model, @RequestParam(name = "pageNum", required = false) Integer pageNum,
                                   Principal principal) {
        setHeaderVariables(model, principal);
        User user = userService.getUserByEmail(principal.getName());

        if (pageNum == null)
            pageNum = 1;
        List<Meeting> subscriptions = userService.getSubscriptions(user.getId(), pageNum, meetingOnPageNum);
        double pageCount =
                Math.ceil(userService.getSubscriptionsNum(user.getId(), pageNum, meetingOnPageNum) / (double) meetingOnPageNum);
        if (subscriptions != null) {
            setMeetingVariables(model, Converter.convert(subscriptions, MeetingDto.class), pageNum, "subscriptions", pageCount);
        }
        return "subscriptions";
    }
}
