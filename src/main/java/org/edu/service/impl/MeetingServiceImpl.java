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
import java.util.List;

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
        System.out.println("Meeting has created");
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
}
