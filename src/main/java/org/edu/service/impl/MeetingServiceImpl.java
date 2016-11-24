package org.edu.service.impl;

import org.edu.dao.CategoryDao;
import org.edu.dao.MeetingDao;
import org.edu.dao.StationDao;
import org.edu.dao.UserDao;
import org.edu.model.Category;
import org.edu.model.Meeting;
import org.edu.model.Station;
import org.edu.model.User;
import org.edu.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userDao.findByEmail(principal.getName());
        if (user.getId() == meeting.getAuthor().getId()) {
            meetingDao.update(meeting);
            return true;
        }
        return false;
    }

    @Override
    public void removeMeeting(long id) {
        
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
