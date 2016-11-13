package org.edu.service;

import org.edu.dao.MeetingDao;
import org.edu.dao.UserDao;
import org.edu.model.Meeting;
import org.edu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Pavel on 13.11.2016.
 */
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    MeetingDao meetingDao;

    @Autowired
    UserDao userDao;

    @Override
    public void createMeeting(Meeting meeting, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        meeting.setAuthor(user);
        meetingDao.create(meeting);
        System.out.println("Meeting has created");
    }

    @Override
    public Meeting getMeetingById(long id) {
        return null;
    }

    @Override
    public boolean updateMeeting(Meeting meeting, Principal principal) {
        return false;
    }

    @Override
    public void removeMeeting(long id) {

    }

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingDao.findAll();
    }
}
