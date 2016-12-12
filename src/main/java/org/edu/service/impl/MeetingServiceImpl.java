package org.edu.service.impl;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.edu.dao.CategoryDao;
import org.edu.dao.MeetingDao;
import org.edu.dao.StationDao;
import org.edu.dao.UserDao;
import org.edu.model.Category;
import org.edu.model.Meeting;
import org.edu.model.Station;
import org.edu.model.User;
import org.edu.service.MeetingService;
import org.edu.util.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    MeetingDao meetingDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    StationDao stationDao;

    private Map<String, List<Meeting>> waitingUsersNewMeetings = new HashMap<>();

    @Override
    public void createMeeting(Meeting meeting, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        Station station = stationDao.findOne(meeting.getStation().getId());
        Category category = categoryDao.findOne(meeting.getCategory().getId());
        meeting.setAuthor(user);
        meeting.setStation(station);
        meeting.setCategory(category);
        //meeting.setCreateDate(Calendar.getInstance().getTime());
        meetingDao.create(meeting);
        addToNewMeetings(meeting, category);
        System.out.println("Meeting has created");
    }

    private void addToNewMeetings(Meeting meeting, Category category) {
        Set<User> subscribedUsers = category.getUsers();
        for (User user : subscribedUsers) {
            if (waitingUsersNewMeetings.get(user.getEmail()) != null) {
                waitingUsersNewMeetings.get(user.getEmail()).add(meeting);
            }
        }
    }

    @Override
    public void addUserToWaiting(String userName) {
        waitingUsersNewMeetings.put(userName, new ArrayList<>());
    }

    @Override
    public List<Meeting> getNewCreatedMeetings(String userName) {
        if (waitingUsersNewMeetings.get(userName) == null)
            return null;
        List<Meeting> newMeetings = new ArrayList<>(waitingUsersNewMeetings.get(userName));
        waitingUsersNewMeetings.get(userName).clear();
        return newMeetings;
    }

    @Override
    public void removeUserFromWaiting(String userName) {
        waitingUsersNewMeetings.remove(userName);
    }

    @Override
    public Meeting getMeetingById(long id) {

        return meetingDao.findOne(id);
    }

    @Override
    public boolean updateMeeting(Meeting meeting, Principal principal) {
        User principalUser = userDao.findByEmail(principal.getName());
        Category category = null;
        Station station = null;
        if (meeting.getCategory() != null) {
            category = categoryDao.findOne(meeting.getCategory().getId());
        }
        if (meeting.getStation() != null) {
            station = stationDao.findOne(meeting.getStation().getId());
        }

        Meeting checkingMeeting = meetingDao.findOne(meeting.getId());
        if (checkingMeeting != null && checkingMeeting.getAuthor().getId() == principalUser.getId()) {
            BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
            try {
                notNull.copyProperties(checkingMeeting, meeting);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            checkingMeeting.setAuthor(principalUser);
            if (category != null) {
                checkingMeeting.setCategory(category);
            }
            if (station != null) {
                checkingMeeting.setStation(station);
            }
            meetingDao.update(checkingMeeting);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMeeting(long id, Principal principal) {
        User principalUser = userDao.findByEmail(principal.getName());
        Meeting checkingMeeting = meetingDao.findOne(id);
        //check if meeting with this id exists and this user is the author
        if (checkingMeeting != null && checkingMeeting.getAuthor().getId() == principalUser.getId()) {
            checkingMeeting.getAuthor().getCreatedMeetings().remove(checkingMeeting);
            checkingMeeting.getSubscribedUsers().clear();
            checkingMeeting.getCategory().deleteMeeting(checkingMeeting);
            checkingMeeting.getStation().getMeetings().remove(checkingMeeting);
            meetingDao.delete(checkingMeeting);
            return true;
        }
        return false;
    }

    @Override
    public List<Meeting> getAllMeetings() {

        return meetingDao.findAll();
    }

    @Override
    public void subscribeMeeting(Meeting meeting, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        Meeting updatedMeeting = meetingDao.findOne(meeting.getId());
        updatedMeeting.addUser(user);
    }

    @Override
    public void unSubscribeMeeting(Meeting meeting, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        Meeting updatedMeeting = meetingDao.findOne(meeting.getId());
        updatedMeeting.deleteUser(user);
    }

    @Override
    public List<Meeting> getNewMeetings(int num, int pageNum) {
        return meetingDao.findNewMeetings(num, pageNum);
    }

    @Override
    public List<Meeting> getUpcomingMeetings(int num, int pageNum) {
        return meetingDao.findUpcomingMeetings(num, pageNum);
    }

    @Override
    public List<Meeting> getPopularMeetings(int num, int pageNum) {
        return meetingDao.findPopularMeetings(num, pageNum);
    }

    @Override
    public List<Meeting> getFeed(int num, int pageNum, String userName) {
        return meetingDao.findSubscribedCategoryMeetings(num, pageNum, userName);
    }

    @Override
    public long getMeetingsNumber() {
        return meetingDao.findMeetingsNumber();
    }

    @Override
    public long getUpcomingMeetingsNumber() {
        return meetingDao.findUpcomingMeetingsNumber();
    }

    @Override
    public long getPopularMeetingsNumber() {
        return meetingDao.findPopularMeetingsNumber();
    }

    @Override
    public long getFeedNumber(String userName) {
        return meetingDao.findSubscribedCategoryMeetingsNumber(userName);
    }

    @Override
    public List<Meeting> getMeetingBySearchString(String searchString, int pagenNum, int num) {
        return meetingDao.findMeetingBySearchString(searchString, pagenNum, num);
    }

    @Override
    public Long getMeetingBySearchStringNum(String searchString, int pageNum, int num) {
        return meetingDao.findMeetingBySearchStringNum(searchString, pageNum, num);
    }
}
